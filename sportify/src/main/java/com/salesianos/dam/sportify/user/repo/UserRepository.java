package com.salesianos.dam.sportify.user.repo;

import com.salesianos.dam.sportify.equipo.model.Equipo;
import com.salesianos.dam.sportify.user.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Transactional
    Optional<User> findFirstByUsername(String username);

    Optional<User> findByActivationToken(String activationToken);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);


    List<User> findByDeportesSeguidos_NombreNoEspacio(String nombreNoEspacio);
    List<User> findByLigasSeguidas_NombreNoEspacio(String nombreNoEspacio);
    List<User> findByEquiposSeguidos_NombreNoEspacio(String nombreNoEspacio);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.equiposSeguidos")
    List<User> findAllWithEquiposSeguidos();

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.equiposSeguidos LEFT JOIN FETCH u.deportesSeguidos")
    List<User> findAllWithEquiposAndDeportesSeguidos();

    @Query("""
        SELECT DISTINCT u FROM User u
        LEFT JOIN FETCH u.equiposSeguidos
        LEFT JOIN FETCH u.deportesSeguidos
        LEFT JOIN FETCH u.ligasSeguidas
    """)
    List<User> findAllWithAllSeguidos();
}
