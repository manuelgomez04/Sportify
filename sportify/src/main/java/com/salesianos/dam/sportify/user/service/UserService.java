package com.salesianos.dam.sportify.user.service;

import com.salesianos.dam.sportify.deporte.dto.FollowDeporteRequest;
import com.salesianos.dam.sportify.deporte.model.Deporte;
import com.salesianos.dam.sportify.deporte.repo.DeporteRepository;
import com.salesianos.dam.sportify.equipo.dto.FollowEquipoRequest;
import com.salesianos.dam.sportify.equipo.model.Equipo;
import com.salesianos.dam.sportify.equipo.repo.EquipoRepository;
import com.salesianos.dam.sportify.error.*;
import com.salesianos.dam.sportify.files.model.FileMetadata;
import com.salesianos.dam.sportify.files.service.StorageService;
import com.salesianos.dam.sportify.liga.dto.FollowLigaRequest;
import com.salesianos.dam.sportify.liga.model.Liga;
import com.salesianos.dam.sportify.liga.repo.LigaRepository;
import com.salesianos.dam.sportify.user.dto.CreateUserRequest;
import com.salesianos.dam.sportify.user.dto.EditUserDto;
import com.salesianos.dam.sportify.user.dto.GetUserNoAsociacionesDto;
import com.salesianos.dam.sportify.user.error.ActivationExpiredException;
import com.salesianos.dam.sportify.user.model.Role;
import com.salesianos.dam.sportify.user.model.User;
import com.salesianos.dam.sportify.user.repo.UserRepository;
import com.salesianos.dam.sportify.util.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
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
    private final DeporteRepository deporteRepository;
    private final LigaRepository ligaRepository;
    private final StorageService storageService;

    @Value("${activation.duration}")
    private int activationDuration;

    public User createUser(CreateUserRequest createUserRequest, MultipartFile profileImage) {

        FileMetadata fileMetadata = storageService.store(profileImage);
        String imageUrl = fileMetadata.getFilename();

        User user = User.builder()
                .username(createUserRequest.username())
                .password(passwordEncoder.encode(createUserRequest.password()))
                .email(createUserRequest.email())
                .roles(Set.of(Role.USER))
                .nombre(createUserRequest.nombre())
                .fechaNacimiento(createUserRequest.fechaNacimiento())
                .deleted(false)
                .activationToken(generateRandomActivationCode())
                .profileImage(imageUrl)
                .build();

        try {
            emailService.sendVerificationEmail(createUserRequest.email(), user.getActivationToken());
        } catch (Exception e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al enviar el correo de activación");
        }

        return userRepository.save(user);
    }

    public User createWriter(CreateUserRequest createUserRequest, MultipartFile profileImage) {
        FileMetadata fileMetadata = storageService.store(profileImage);
        String imageUrl = fileMetadata.getFilename();

        User user = User.builder()
                .username(createUserRequest.username())
                .password(passwordEncoder.encode(createUserRequest.password()))
                .email(createUserRequest.email())
                .roles(Set.of(Role.WRITER, Role.USER))
                .activationToken(generateRandomActivationCode())
                .nombre(createUserRequest.nombre())
                .fechaNacimiento(createUserRequest.fechaNacimiento())
                .profileImage(imageUrl)
                .build();

        try {
            emailService.sendVerificationEmail(createUserRequest.email(), user.getActivationToken());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al enviar el correo de activación");
        }

        return userRepository.save(user);
    }

    public User createAdmin(CreateUserRequest createUserRequest, MultipartFile profileImage) {
        FileMetadata fileMetadata = storageService.store(profileImage);
        String imageUrl = fileMetadata.getFilename();

        User user = User.builder()
                .username(createUserRequest.username())
                .password(passwordEncoder.encode(createUserRequest.password()))
                .email(createUserRequest.email())
                .roles(Set.of(Role.ADMIN, Role.USER, Role.WRITER))
                .activationToken(generateRandomActivationCode())
                .nombre(createUserRequest.nombre()).fechaNacimiento(createUserRequest.fechaNacimiento())
                .profileImage(imageUrl)
                .build();

        try {
            emailService.sendVerificationEmail(createUserRequest.email(), user.getActivationToken());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al enviar el correo de activación");
        }

        return userRepository.save(user);
    }

    @Transactional
    public User editMe(User username, EditUserDto updatedUser, MultipartFile profileImage) {

        return userRepository.findFirstByUsername(username.getUsername())
                .map(user -> {
                    // Guardar la imagen solo si se ha subido una nueva
                    if (profileImage != null && !profileImage.isEmpty()) {
                        FileMetadata fileMetadata = storageService.store(profileImage);
                        String imageUrl = fileMetadata.getFilename();
                        user.setProfileImage(imageUrl);
                    }
                    if (updatedUser.email() != null && !updatedUser.email().equals(user.getEmail())) {
                        // Comprobar si el email ya existe en otro usuario
                        if (userRepository.existsByEmail(updatedUser.email())) {
                            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está en uso");
                        }
                        user.setEmail(updatedUser.email());
                    }
                    if (updatedUser.password() != null) {
                        user.setPassword(passwordEncoder.encode(updatedUser.password()));
                    }
                    if (updatedUser.nombre() != null) {
                        user.setNombre(updatedUser.nombre());
                    }
                    if (updatedUser.fechaNacimiento() != null) {
                        user.setFechaNacimiento(updatedUser.fechaNacimiento());
                    }
                    Hibernate.initialize(user.getEquiposSeguidos());
                    Hibernate.initialize(user.getDeportesSeguidos());
                    Hibernate.initialize(user.getLigasSeguidas());

                    return user;

                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

    }

    @Transactional
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
                    Hibernate.initialize(user.getEquiposSeguidos());
                    Hibernate.initialize(user.getDeportesSeguidos());
                    Hibernate.initialize(user.getLigasSeguidas());

                    return user;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    @Transactional
    public void deleteUser(String user) {
        Optional<User> u = userRepository.findFirstByUsername(user);

        if (u.isPresent()) {
            u.get().setDeleted(true);
            u.get().setEmail(null);
            userRepository.save(u.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
    }

    @Transactional
    public void deleteMe(GetUserNoAsociacionesDto user) {
        Optional<User> u = userRepository.findFirstByUsername(user.username());
        if (u.isPresent()) {
            u.get().setDeleted(true);
            u.get().setEmail(null);
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

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public User getAuthenticatedUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Hibernate.initialize(user.getEquiposSeguidos());
        Hibernate.initialize(user.getDeportesSeguidos());
        Hibernate.initialize(user.getLigasSeguidas());

        return user;
    }

    @Transactional
    public User seguirEquipo(String username, FollowEquipoRequest nombreEquipo) {
        User user = userRepository.findFirstByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado", HttpStatus.NOT_FOUND));

        Equipo equipo = equipoRepository.findByNombreNoEspacio(nombreEquipo.nombreEquipo())
                .orElseThrow(() -> new EquipoNotFoundException("Equipo no encontrado", HttpStatus.NOT_FOUND));

        Hibernate.initialize(user.getDeportesSeguidos());
        Hibernate.initialize(user.getLigasSeguidas());

        user.addEquipo(equipo);

        return userRepository.save(user);
    }

    @Transactional
    public User dejarDeSeguirEquipo(String username, FollowEquipoRequest nombreEquipo) {
        User user = userRepository.findFirstByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado", HttpStatus.NOT_FOUND));

        Equipo equipo = equipoRepository.findByNombreNoEspacio(nombreEquipo.nombreEquipo())
                .orElseThrow(() -> new EquipoNotFoundException("Equipo no encontrado", HttpStatus.NOT_FOUND));

        Hibernate.initialize(user.getDeportesSeguidos());
        Hibernate.initialize(user.getLigasSeguidas());

        user.removeEquipo(equipo);

        return userRepository.save(user);
    }

    @Transactional
    public User seguirDeporte(String username, FollowDeporteRequest nombreDeporte) {
        User user = userRepository.findFirstByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado", HttpStatus.NOT_FOUND));

        Deporte deporte = deporteRepository.findByNombreNoEspacio(nombreDeporte.nombreDeporte())
                .orElseThrow(() -> new DeporteNotFoundException("Deporte no encontrado", HttpStatus.NOT_FOUND));

        Hibernate.initialize(user.getEquiposSeguidos());
        Hibernate.initialize(user.getLigasSeguidas());

        user.addDeporte(deporte);

        return userRepository.save(user);
    }

    @Transactional
    public User dejarDeSeguirDeporte(String username, FollowDeporteRequest nombreDeporte) {
        User user = userRepository.findFirstByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado", HttpStatus.NOT_FOUND));

        Deporte deporte = deporteRepository.findByNombreNoEspacio(nombreDeporte.nombreDeporte())
                .orElseThrow(() -> new DeporteNotFoundException("Deporte no encontrado", HttpStatus.NOT_FOUND));

        Hibernate.initialize(user.getEquiposSeguidos());
        Hibernate.initialize(user.getLigasSeguidas());

        user.removeDeporte(deporte);

        return userRepository.save(user);
    }

    @Transactional
    public User seguirLiga(String username, FollowLigaRequest nombreLiga) {
        User user = userRepository.findFirstByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado", HttpStatus.NOT_FOUND));

        Liga liga = ligaRepository.findByNombreNoEspacio(nombreLiga.nombreLiga())
                .orElseThrow(() -> new LigaNotFoundException("Liga no encontrada", HttpStatus.NOT_FOUND));

        Hibernate.initialize(user.getEquiposSeguidos());
        Hibernate.initialize(user.getDeportesSeguidos());
        user.addLiga(liga);

        return userRepository.save(user);
    }

    @Transactional
    public User dejarDeSeguirLiga(String username, FollowLigaRequest nombreLiga) {
        User user = userRepository.findFirstByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado", HttpStatus.NOT_FOUND));

        Liga liga = ligaRepository.findByNombreNoEspacio(nombreLiga.nombreLiga())
                .orElseThrow(() -> new LigaNotFoundException("Equipo no encontrado", HttpStatus.NOT_FOUND));

        Hibernate.initialize(user.getDeportesSeguidos());
        Hibernate.initialize(user.getEquiposSeguidos());

        user.removeLiga(liga);

        return userRepository.save(user);
    }

    public Page<Liga> findLigasFavoritasByUsername(String username, Pageable pageable) {
        return ligaRepository.findLigasFavoritasByUsername(username, pageable);
    }

    public Page<Deporte> findDeportesFavoritosByUsername(String username, Pageable pageable) {
        return deporteRepository.findByUsuariosSeguidosUsername(username, pageable);
    }

    public Page<Equipo> findEquiposFavoritosByUsername(String username, Pageable pageable) {
        return equipoRepository.findByUsuariosSeguidosUsername(username, pageable);
    }

    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findFirstByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return user;
    }

    @Transactional
    public List<User> findAllWithEquiposAndDeportesSeguidos() {
        return userRepository.findAllWithEquiposAndDeportesSeguidos();
    }

    @Transactional
    public List<User> findAllUsers() {
        List<User> users = userRepository.findAllWithEquiposSeguidos();
        return users;
    }

    @Transactional
    public List<User> findAllWithAllSeguidos() {
        return userRepository.findAllWithAllSeguidos();
    }

}
