package com.salesianos.dam.sportify.user.repo;

import com.salesianos.dam.sportify.user.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Transactional
    Optional<User> findFirstByUsername(String username);

    Optional<User> findByActivationToken(String activationToken);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("""
            SELECT u FROM User u JOIN FETCH u.equiposSeguidos WHERE u.username = :username
            """)
    Optional<User> findWithEquiposSeguidosByUsername(@Param("username")  String username);
}
