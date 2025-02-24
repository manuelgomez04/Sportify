package com.salesianos.dam.sportify.liga.service;

import com.salesianos.dam.sportify.deporte.model.Deporte;
import com.salesianos.dam.sportify.deporte.repo.DeporteRepository;
import com.salesianos.dam.sportify.error.DeporteNotFoundException;
import com.salesianos.dam.sportify.liga.dto.CreateLigaRequest;
import com.salesianos.dam.sportify.liga.model.Liga;
import com.salesianos.dam.sportify.liga.repo.LigaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LigaService {

    private final LigaRepository ligaRepository;
    private final DeporteRepository deporteRepository;


    public Liga createLiga(CreateLigaRequest createLigaRequest) {

        Liga l = Liga.builder()
                .nombre(createLigaRequest.nombre())
                .descripcion(createLigaRequest.descripcion())
                .build();

        ligaRepository.save(l);

        Optional<Deporte> d = ;

        if (deporteRepository.findByNombreEqualsIgnoreCase(createLigaRequest.nombreDeporte()).isPresent()) {
            deporteRepository.findByNombreEqualsIgnoreCase(createLigaRequest.nombreDeporte()).get().addLiga(l);
            ligaRepository.save(l);
        } else {
            throw new DeporteNotFoundException("Deporte no encontrado", HttpStatus.NOT_FOUND);
        }

        return l;

    }


}
