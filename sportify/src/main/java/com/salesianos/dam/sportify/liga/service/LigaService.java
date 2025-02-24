package com.salesianos.dam.sportify.liga.service;

import com.salesianos.dam.sportify.deporte.repo.DeporteRepository;
import com.salesianos.dam.sportify.error.DeporteNotFoundException;
import com.salesianos.dam.sportify.error.LigaNotFoundException;
import com.salesianos.dam.sportify.liga.dto.CreateLigaRequest;
import com.salesianos.dam.sportify.liga.model.Liga;
import com.salesianos.dam.sportify.liga.repo.LigaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LigaService {

    private final LigaRepository ligaRepository;
    private final DeporteRepository deporteRepository;


    @Transactional
    public Liga createLiga(CreateLigaRequest createLigaRequest) {

        Liga l = Liga.builder()
                .nombre(createLigaRequest.nombre())
                .descripcion(createLigaRequest.descripcion())
                .build();

        l.generarNombreNoEspacio();
        ligaRepository.save(l);


        if (deporteRepository.findByNombreEqualsIgnoreCase(createLigaRequest.nombreDeporte()).isPresent()) {
            deporteRepository.findByNombreEqualsIgnoreCase(createLigaRequest.nombreDeporte()).get().addLiga(l);
            ligaRepository.save(l);
        } else {
            throw new DeporteNotFoundException("Deporte no encontrado", HttpStatus.NOT_FOUND);
        }

        return l;

    }

    @Transactional
    public void deleteLiga(String nombre) {
        Optional<Liga> liga = ligaRepository.findBy(nombre);

        if (liga.isPresent()) {
            liga.get().getDeporte().deleteLiga(liga.get());
            ligaRepository.delete(liga.get());
        } else {
            throw new LigaNotFoundException("Liga no encontrada", HttpStatus.NOT_FOUND);
        }
    }


}
