package com.salesianos.dam.sportify.liga.service;

import com.salesianos.dam.sportify.deporte.repo.DeporteRepository;
import com.salesianos.dam.sportify.error.DeporteNotFoundException;
import com.salesianos.dam.sportify.error.LigaNotFoundException;
import com.salesianos.dam.sportify.files.model.FileMetadata;
import com.salesianos.dam.sportify.files.service.StorageService;
import com.salesianos.dam.sportify.liga.dto.CreateLigaRequest;
import com.salesianos.dam.sportify.liga.model.Liga;
import com.salesianos.dam.sportify.liga.repo.LigaRepository;
import com.salesianos.dam.sportify.user.repo.UserRepository;
import com.salesianos.dam.sportify.user.model.User;
import com.salesianos.dam.sportify.equipo.model.Equipo;
import com.salesianos.dam.sportify.equipo.repo.EquipoRepository;
import com.salesianos.dam.sportify.noticia.model.Noticia;
import com.salesianos.dam.sportify.noticia.repo.NoticiaRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final UserRepository userRepository;
    private final EquipoRepository equipoRepository;
    private final NoticiaRepository noticiaRepository;

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
    public void deleteLiga(String nombreNoEspacio) {
        Liga liga = ligaRepository.findByNombreNoEspacio(nombreNoEspacio)
                .orElseThrow(() -> new LigaNotFoundException("Liga no encontrada", HttpStatus.NOT_FOUND));

        // 1. Desvincular usuarios de los equipos de la liga
        List<Equipo> equipos = equipoRepository.findByLiga_NombreNoEspacio(nombreNoEspacio);
        for (Equipo equipo : equipos) {
            List<User> usuariosEquipo = userRepository.findByEquiposSeguidos_NombreNoEspacio(equipo.getNombreNoEspacio());
            for (User user : usuariosEquipo) {
                user.getEquiposSeguidos().remove(equipo);
                userRepository.save(user);
            }
            // Desvincular equipo de noticias
            List<Noticia> noticiasEquipo = noticiaRepository.findByEquipoNoticia_NombreNoEspacio(equipo.getNombreNoEspacio());
            for (Noticia n : noticiasEquipo) {
                n.setEquipoNoticia(null);
                noticiaRepository.save(n);
            }
        }

        // 2. Desvincular usuarios de la liga
        List<User> usuariosLiga = userRepository.findByLigasSeguidas_NombreNoEspacio(nombreNoEspacio);
        for (User user : usuariosLiga) {
            user.getLigasSeguidas().remove(liga);
            userRepository.save(user);
        }

        // 3. Desvincular liga de noticias
        List<Noticia> noticiasLiga = noticiaRepository.findByLigaNoticia_NombreNoEspacio(nombreNoEspacio);
        for (Noticia n : noticiasLiga) {
            n.setLigaNoticia(null);
            noticiaRepository.save(n);
        }

        // 4. Desvincular la liga de su deporte
        if (liga.getDeporte() != null) {
            liga.getDeporte().deleteLiga(liga);
            liga.setDeporte(null);
        }

        // 5. Eliminar la liga
        ligaRepository.delete(liga);
    }

    public String getImageUrl(String filename) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(filename)
                .toUriString();
    }

    public List<Liga> getAllLigas() {
        return ligaRepository.findAll();
    }

    public Page<Liga> getLigasPorDeporte(String nombreDeporte, Pageable pageable) {
        return ligaRepository.findByDeporte_NombreNoEspacio(nombreDeporte, pageable);
    }

    public Page<Liga> getAllPage(Pageable pageable) {
        return ligaRepository.findAll(pageable);
    }

    public Page<Liga> findByDeporteNombreNoEspacio(String deporteNombreNoEspacio, Pageable pageable) {
        return ligaRepository.findByDeporte_NombreNoEspacio(deporteNombreNoEspacio, pageable);
    }
}
