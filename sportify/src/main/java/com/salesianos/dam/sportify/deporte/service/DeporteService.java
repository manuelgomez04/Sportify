package com.salesianos.dam.sportify.deporte.service;

import com.salesianos.dam.sportify.deporte.dto.CreateDeporteRequest;
import com.salesianos.dam.sportify.deporte.model.Deporte;
import com.salesianos.dam.sportify.deporte.repo.DeporteRepository;
import com.salesianos.dam.sportify.equipo.model.Equipo;
import com.salesianos.dam.sportify.equipo.repo.EquipoRepository;
import com.salesianos.dam.sportify.error.DeporteNotFoundException;
import com.salesianos.dam.sportify.files.model.FileMetadata;
import com.salesianos.dam.sportify.files.service.StorageService;
import com.salesianos.dam.sportify.liga.model.Liga;
import com.salesianos.dam.sportify.liga.repo.LigaRepository;
import com.salesianos.dam.sportify.noticia.model.Noticia;
import com.salesianos.dam.sportify.noticia.repo.NoticiaRepository;
import com.salesianos.dam.sportify.user.model.User;
import com.salesianos.dam.sportify.user.repo.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeporteService {

    private final DeporteRepository deporteRepository;
    private final StorageService storageService;
    private final NoticiaRepository noticiaRepository;
    private final UserRepository userRepository;
    private final LigaRepository ligaRepository;
    private final EquipoRepository equipoRepository;

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

    @Transactional
    public void deleteDeporte(String name) {
        Deporte d = deporteRepository.findByNombreNoEspacio(name)
                .orElseThrow(() -> new DeporteNotFoundException("Deporte no encontrado", HttpStatus.NOT_FOUND));

        // 1. Desvincular usuarios de equipos de las ligas del deporte
        List<Liga> ligas = ligaRepository.findByDeporte_NombreNoEspacio(name);
        for (Liga liga : ligas) {
            List<Equipo> equipos = equipoRepository.findByLiga_NombreNoEspacio(liga.getNombreNoEspacio());
            for (Equipo equipo : equipos) {
                List<User> usuariosEquipo = userRepository.findByEquiposSeguidos_NombreNoEspacio(equipo.getNombreNoEspacio());
                for (User user : usuariosEquipo) {
                    user.getEquiposSeguidos().remove(equipo);
                    userRepository.save(user);
                }
            }
        }

        // 2. Desvincular usuarios de las ligas del deporte
        for (Liga liga : ligas) {
            List<User> usuariosLiga = userRepository.findByLigasSeguidas_NombreNoEspacio(liga.getNombreNoEspacio());
            for (User user : usuariosLiga) {
                user.getLigasSeguidas().remove(liga);
                userRepository.save(user);
            }
        }

        // 3. Desvincular usuarios del propio deporte
        List<User> usuariosDeporte = userRepository.findByDeportesSeguidos_NombreNoEspacio(name);
        for (User user : usuariosDeporte) {
            user.getDeportesSeguidos().remove(d);
            userRepository.save(user);
        }

        // 4. Poner a null referencias en noticias (ya lo tienes implementado)
        List<Noticia> n = noticiaRepository.findByDeporteNoticia_NombreNoEspacio(name);
        if (!n.isEmpty()) {
            for (Noticia noticia : n) {
                noticia.setDeporteNoticia(null);
                noticia.setLigaNoticia(null);
                noticia.setEquipoNoticia(null);
                noticiaRepository.save(noticia);
            }
        }

        // 5. Borrar el deporte (borra ligas y equipos en cascada)
        deporteRepository.delete(d);

    }

    public String getImageUrl(String filename) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(filename)
                .toUriString();
    }

    public Page<Deporte> getAllDeportes(Pageable pageable) {
        return deporteRepository.findAll(pageable);
    }
}
