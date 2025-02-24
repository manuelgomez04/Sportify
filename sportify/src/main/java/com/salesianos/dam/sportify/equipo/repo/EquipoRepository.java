package com.salesianos.dam.sportify.equipo.repo;

import com.salesianos.dam.sportify.equipo.model.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface EquipoRepository extends JpaRepository<Equipo, UUID> {

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Equipo e WHERE TRIM(LOWER(e.nombre)) = TRIM(LOWER(:nombre))")
    boolean existsByNombreEqualsIgnoreCaseAndIgnoreWhitespace(@Param("nombre") String nombre);

    Optional<Equipo> findByNombreNoEspacio(String nombreNoEspacios);
}
