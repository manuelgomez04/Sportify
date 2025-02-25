package com.salesianos.dam.sportify.equipo.repo;

import com.salesianos.dam.sportify.equipo.model.Equipo;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface EquipoRepository extends JpaRepository<Equipo, UUID> {

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Equipo e WHERE TRIM(LOWER(e.nombre)) = TRIM(LOWER(:nombre))")
    boolean existsByNombreEqualsIgnoreCaseAndIgnoreWhitespace(@Param("nombre") String nombre);

    Optional<Equipo> findByNombreNoEspacio(String nombreNoEspacios);

    @Query(value = "SELECT e.* FROM equipo e " +
            "JOIN usuario_equipo ue ON e.id = ue.equipo_id " +
            "JOIN users u ON ue.usuario_id = u.id " +
            "WHERE u.username = :username",
            countQuery = "SELECT COUNT(*) FROM equipo e " +
                    "JOIN usuario_equipo ue ON e.id = ue.equipo_id " +
                    "JOIN users u ON ue.usuario_id = u.id " +
                    "WHERE u.username = :username",
            nativeQuery = true)
    Page<Equipo> findByUsuariosSeguidosUsername(@Param("username") String username, Pageable pageable);
}
