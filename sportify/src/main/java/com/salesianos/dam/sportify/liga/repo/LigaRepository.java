package com.salesianos.dam.sportify.liga.repo;

import com.salesianos.dam.sportify.liga.model.Liga;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LigaRepository extends JpaRepository<Liga, UUID> {

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Liga l WHERE TRIM(LOWER(l.nombre)) = TRIM(LOWER(:nombre))")
    boolean existsByNombreEqualsIgnoreCaseAndIgnoreWhitespace(@Param("nombre") String nombre);

    Optional<Liga> findByNombreNoEspacio(String nombre);

    List<Liga> findByDeporte_NombreNoEspacio(String nombreNoEspacio);

    @Query(value = "SELECT l.* FROM liga l " +
            "JOIN usuario_liga ul ON l.id = ul.liga_id " +
            "JOIN users u ON ul.usuario_id = u.id " +
            "WHERE u.username = :username",
            countQuery = "SELECT COUNT(*) FROM liga l " +
                    "JOIN usuario_liga ul ON l.id = ul.liga_id " +
                    "JOIN users u ON ul.usuario_id = u.id " +
                    "WHERE u.username = :username",
            nativeQuery = true)
    Page<Liga> findLigasFavoritasByUsername(@Param("username") String username, Pageable pageable);
}
