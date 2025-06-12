package com.salesianos.dam.sportify.liga.service;

import com.salesianos.dam.sportify.deporte.repo.DeporteRepository;
import com.salesianos.dam.sportify.error.DeporteNotFoundException;
import com.salesianos.dam.sportify.error.LigaNotFoundException;
import com.salesianos.dam.sportify.files.model.FileMetadata;
import com.salesianos.dam.sportify.files.service.StorageService;
import com.salesianos.dam.sportify.liga.dto.CreateLigaRequest;
import com.salesianos.dam.sportify.liga.model.Liga;
import com.salesianos.dam.sportify.liga.repo.LigaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LigaService {

    private final LigaRepository ligaRepository;
    private final DeporteRepository deporteRepository;
    private final StorageService storageService;

    @Transactional
    public Liga createLiga(CreateLigaRequest createLigaRequest, MultipartFile imagen) {

      

        FileMetadata fileMetadata = storageService.store(imagen);

  

        String imageUrl = fileMetadata.getFilename();
        imageUrl = getImageUrl(imageUrl);

        



        Liga l = Liga.builder()
                .nombre(createLigaRequest.nombre())
                .descripcion(createLigaRequest.descripcion())
                .imagen(imageUrl)
                .build();

        l.generarNombreNoEspacio();
        ligaRepository.save(l);

        if (deporteRepository.findByNombreNoEspacio(createLigaRequest.nombreDeporte()).isPresent()) {
            deporteRepository.findByNombreNoEspacio(createLigaRequest.nombreDeporte()).get().addLiga(l);
            ligaRepository.save(l);
        } else {
            throw new DeporteNotFoundException("Deporte no encontrado", HttpStatus.NOT_FOUND);
        }

        return l;

    }

    @Transactional
    public void deleteLiga(String nombre) {
        Optional<Liga> liga = ligaRepository.findByNombreNoEspacio(nombre);

        if (liga.isPresent()) {
            liga.get().getDeporte().deleteLiga(liga.get());
            ligaRepository.delete(liga.get());
        } else {
            throw new LigaNotFoundException("Liga no encontrada", HttpStatus.NOT_FOUND);
        }
    }

      public String getImageUrl(String filename) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(filename)
                .toUriString();
    }
}
