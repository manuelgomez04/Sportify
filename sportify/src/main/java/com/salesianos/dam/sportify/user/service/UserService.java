package com.salesianos.dam.sportify.user.service;

import com.salesianos.dam.sportify.user.dto.CreateUserRequest;
import com.salesianos.dam.sportify.user.dto.EditUserDto;
import com.salesianos.dam.sportify.user.error.ActivationExpiredException;
import com.salesianos.dam.sportify.user.model.Role;
import com.salesianos.dam.sportify.user.model.User;
import com.salesianos.dam.sportify.user.repo.UserRepository;
import com.salesianos.dam.sportify.util.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;


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


    public User editUser(User username, EditUserDto updatedUser) {

    return userRepository.findFirstByUsername(username.getUsername())
            .map(user -> {
                user.setPassword(passwordEncoder.encode(updatedUser.password()));
                user.setEmail(updatedUser.email());
                user.setNombre(updatedUser.nombre());
                user.setFechaNacimiento(updatedUser.fechaNacimiento());
                return userRepository.save(user);
            })
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));





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

}
