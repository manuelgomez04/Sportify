package com.salesianos.dam.sportify.deporte.repo;

import com.salesianos.dam.sportify.deporte.model.Deporte;
import com.salesianos.dam.sportify.liga.model.Liga;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface DeporteRepository extends JpaRepository<Deporte, UUID> {

    Optional<Deporte> findByNombreEqualsIgnoreCase(String nombre);

    boolean existsByNombreEqualsIgnoreCase(String nombre);

    @Query(value = "SELECT d.* FROM deporte d " +
            "JOIN usuario_deporte ud ON d.id = ud.deporte_id " +
            "JOIN users u ON ud.usuario_id = u.id " +
            "WHERE u.username = :username",
            countQuery = "SELECT COUNT(*) FROM deporte d " +
                    "JOIN usuario_deporte ud ON d.id = ud.deporte_id " +
                    "JOIN users u ON ud.usuario_id = u.id " +
                    "WHERE u.username = :username",
            nativeQuery = true)
    Page<Deporte> findByUsuariosSeguidosUsername(@Param("username") String username, Pageable pageable);
}
