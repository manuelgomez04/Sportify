package com.salesianos.dam.sportify.deporte.service;

import com.salesianos.dam.sportify.deporte.dto.CreateDeporteRequest;
import com.salesianos.dam.sportify.deporte.model.Deporte;
import com.salesianos.dam.sportify.deporte.repo.DeporteRepository;
import com.salesianos.dam.sportify.error.DeporteNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeporteService {

    private final DeporteRepository deporteRepository;


    public Deporte createDeporte(CreateDeporteRequest createDeporteRequest) {
        return deporteRepository.save(Deporte.builder()
                .nombre(createDeporteRequest.nombre())
                .descripcion(createDeporteRequest.descripcion())
                .build());

    }

    public void deleteDeporte(String name) {

        Deporte d = deporteRepository.findByNombreEqualsIgnoreCase(name).orElseThrow(() -> new DeporteNotFoundException("Deporte no encontrado", HttpStatus.NOT_FOUND));

        deporteRepository.delete(d);

    }


}
