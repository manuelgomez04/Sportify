package com.salesianos.dam.sportify.deporte.repo;

import com.salesianos.dam.sportify.deporte.model.Deporte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeporteRepository extends JpaRepository<Deporte, UUID> {

    Optional<Deporte> findByNombreEqualsIgnoreCase(String nombre);

    boolean existsByNombreEqualsIgnoreCase(String nombre);
}
