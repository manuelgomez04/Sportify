package com.salesianos.dam.sportify.deporte.service;

import com.salesianos.dam.sportify.deporte.dto.CreateDeporteRequest;
import com.salesianos.dam.sportify.deporte.model.Deporte;
import com.salesianos.dam.sportify.deporte.repo.DeporteRepository;
import com.salesianos.dam.sportify.error.DeporteNotFoundException;
import com.salesianos.dam.sportify.files.model.FileMetadata;
import com.salesianos.dam.sportify.files.service.StorageService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeporteService {

    private final DeporteRepository deporteRepository;
    private final StorageService storageService;

    public Deporte createDeporte(CreateDeporteRequest createDeporteRequest, MultipartFile imagen) {
        FileMetadata fileMetadata = storageService.store(imagen);

        String imageUrl = fileMetadata.getFilename();
        imageUrl = getImageUrl(imageUrl);

        Deporte d = deporteRepository.save(Deporte.builder()
                .nombre(createDeporteRequest.nombre())
                .descripcion(createDeporteRequest.descripcion())
                .imagen(imageUrl)
                .build());
        d.generarNombreNoEspacio();
        deporteRepository.save(d);
        return d;

    }

    public void deleteDeporte(String name) {

        Deporte d = deporteRepository.findByNombreNoEspacio(name)
                .orElseThrow(() -> new DeporteNotFoundException("Deporte no encontrado", HttpStatus.NOT_FOUND));

        deporteRepository.delete(d);

    }
    public String getImageUrl(String filename) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(filename)
                .toUriString();
    }

    public List<Deporte> getAllDeportes() {
        return deporteRepository.findAll();
    }
}
