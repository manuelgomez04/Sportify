package com.salesianos.dam.sportify.liga.repo;

import com.salesianos.dam.sportify.liga.model.Liga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LigaRepository extends JpaRepository<Liga, UUID> {
}
