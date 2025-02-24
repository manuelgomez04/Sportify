package com.salesianos.dam.sportify.liga.repo;

import com.salesianos.dam.sportify.liga.model.Liga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface LigaRepository extends JpaRepository<Liga, UUID> {

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Liga l WHERE TRIM(LOWER(l.nombre)) = TRIM(LOWER(:nombre))")
    boolean existsByNombreEqualsIgnoreCaseAndIgnoreWhitespace(@Param("nombre") String nombre);

    Optional<Liga> findByNombreNoEspacio(String nombre);
}
