package com.salesianos.dam.sportify.user.service;

import com.salesianos.dam.sportify.equipo.dto.FollowEquipoRequest;
import com.salesianos.dam.sportify.equipo.model.Equipo;
import com.salesianos.dam.sportify.equipo.repo.EquipoRepository;
import com.salesianos.dam.sportify.equipo.service.EquipoService;
import com.salesianos.dam.sportify.error.EquipoNotFoundException;
import com.salesianos.dam.sportify.error.UserNotFoundException;
import com.salesianos.dam.sportify.user.dto.CreateUserRequest;
import com.salesianos.dam.sportify.user.dto.EditUserDto;
import com.salesianos.dam.sportify.user.error.ActivationExpiredException;
import com.salesianos.dam.sportify.user.model.Role;
import com.salesianos.dam.sportify.user.model.User;
import com.salesianos.dam.sportify.user.repo.UserRepository;
import com.salesianos.dam.sportify.util.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final EquipoRepository equipoRepository;


    @Value("${activation.duration}")
    private int activationDuration;

    public User createUser(CreateUserRequest createUserRequest) {
        User user = User.builder()
                .username(createUserRequest.username())
                .password(passwordEncoder.encode(createUserRequest.password()))
                .email(createUserRequest.email())
                .roles(Set.of(Role.USER))
                .activationToken(generateRandomActivationCode())
                .build();

        try {
            // Enviar el código de verificación por correo electrónico
            emailService.sendVerificationEmail(createUserRequest.email(), user.getActivationToken());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al enviar el correo de activación");
        }

        return userRepository.save(user);
    }

    public User createWriter(CreateUserRequest createUserRequest) {
        User user = User.builder()
                .username(createUserRequest.username())
                .password(passwordEncoder.encode(createUserRequest.password()))
                .email(createUserRequest.email())
                .roles(Set.of(Role.WRITER, Role.USER))
                .activationToken(generateRandomActivationCode())
                .build();

        try {
            // Enviar el código de verificación por correo electrónico
            emailService.sendVerificationEmail(createUserRequest.email(), user.getActivationToken());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al enviar el correo de activación");
        }

        return userRepository.save(user);
    }

    public User createAdmin(CreateUserRequest createUserRequest) {
        User user = User.builder()
                .username(createUserRequest.username())
                .password(passwordEncoder.encode(createUserRequest.password()))
                .email(createUserRequest.email())
                .roles(Set.of(Role.ADMIN, Role.USER, Role.WRITER))
                .activationToken(generateRandomActivationCode())
                .build();

        try {
            // Enviar el código de verificación por correo electrónico
            emailService.sendVerificationEmail(createUserRequest.email(), user.getActivationToken());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al enviar el correo de activación");
        }

        return userRepository.save(user);
    }


    public User editMe(User username, EditUserDto updatedUser) {

        return userRepository.findFirstByUsername(username.getUsername())
                .map(user -> {
                    if (updatedUser.password() != null) {
                        user.setPassword(passwordEncoder.encode(updatedUser.password()));
                    }
                    if (updatedUser.email() != null) {
                        user.setEmail(updatedUser.email());
                    }
                    if (updatedUser.nombre() != null) {
                        user.setNombre(updatedUser.nombre());
                    }
                    if (updatedUser.fechaNacimiento() != null) {
                        user.setFechaNacimiento(updatedUser.fechaNacimiento());
                    }
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    public User editUser(String username, EditUserDto updatedUser) {

        return userRepository.findFirstByUsername(username)
                .map(user -> {
                    if (updatedUser.password() != null) {
                        user.setPassword(passwordEncoder.encode(updatedUser.password()));
                    }
                    if (updatedUser.email() != null) {
                        user.setEmail(updatedUser.email());
                    }
                    if (updatedUser.nombre() != null) {
                        user.setNombre(updatedUser.nombre());
                    }
                    if (updatedUser.fechaNacimiento() != null) {
                        user.setFechaNacimiento(updatedUser.fechaNacimiento());
                    }
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    @Transactional
    public void deleteUser(String user) {


        Optional<User> u = userRepository.findFirstByUsername(user);

        if (u.isPresent()) {
            u.get().setDeleted(true);
            userRepository.save(u.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
    }


    public String generateRandomActivationCode() {
        return UUID.randomUUID().toString();
    }

    public User activateAccount(String token) {

        return userRepository.findByActivationToken(token)
                .filter(user -> ChronoUnit.MINUTES.between(Instant.now(), user.getCreatedAt()) - activationDuration < 0)
                .map(user -> {
                    user.setEnabled(true);
                    user.setActivationToken(null);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new ActivationExpiredException("El código de activación no existe o ha caducado"));
    }

    @Transactional
    public User seguirEquipo(String username, FollowEquipoRequest nombreEquipo) {
        User user = userRepository.findFirstByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado", HttpStatus.NOT_FOUND));

        Equipo equipo = equipoRepository.findByNombreNoEspacio(nombreEquipo.nombreEquipo())
                .orElseThrow(() -> new EquipoNotFoundException("Equipo no encontrado", HttpStatus.NOT_FOUND));

        user.addEquipo(equipo);

        return userRepository.save(user);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public User getAuthenticatedUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Hibernate.initialize(user.getEquiposSeguidos());

        return user;
    }

}
