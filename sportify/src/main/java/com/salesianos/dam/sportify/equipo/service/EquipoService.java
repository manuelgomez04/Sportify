package com.salesianos.dam.sportify.equipo.service;

import com.salesianos.dam.sportify.equipo.dto.CreateEquipoRequest;
import com.salesianos.dam.sportify.equipo.model.Equipo;
import com.salesianos.dam.sportify.equipo.repo.EquipoRepository;
import com.salesianos.dam.sportify.error.EquipoNotFoundException;
import com.salesianos.dam.sportify.error.LigaNotFoundException;
import com.salesianos.dam.sportify.files.model.FileMetadata;
import com.salesianos.dam.sportify.files.service.StorageService;
import com.salesianos.dam.sportify.liga.repo.LigaRepository;
import com.salesianos.dam.sportify.noticia.model.Noticia;
import com.salesianos.dam.sportify.noticia.repo.NoticiaRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.Metadata;
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
public class EquipoService {
    private final EquipoRepository equipoRepository;
    private final LigaRepository ligaRepository;
    private final StorageService storageService;
    private final NoticiaRepository noticiaRepository; // Añade esta línea


    @Transactional
    public Equipo createEquipo(CreateEquipoRequest createEquipoRequest, MultipartFile escudo) {

        FileMetadata fileMetadata = storageService.store(escudo);
        String imageUrl = getImageUrl(fileMetadata.getFilename());

        Equipo e = Equipo.builder()
                .nombre(createEquipoRequest.nombre())
                .pais(createEquipoRequest.pais())
                .ciudad(createEquipoRequest.ciudad())
                .fechaCreacion(createEquipoRequest.fechaCreacion())
                .escudo(imageUrl)
                .build();

        e.generarNombreNoEspacio();

        equipoRepository.save(e);

        if (ligaRepository.findByNombreNoEspacio(createEquipoRequest.nombreLiga()).isPresent()) {
            ligaRepository.findByNombreNoEspacio(createEquipoRequest.nombreLiga()).get().addEquipo(e);
            equipoRepository.save(e);
        } else {
            throw new LigaNotFoundException("Liga no encontrada", HttpStatus.NOT_FOUND);
        }

        return e;
    }

    public String getImageUrl(String filename) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(filename)
                .toUriString();
    }

    @Transactional
    public void deleteEquipo(String nombre) {
        Optional<Equipo> e = equipoRepository.findByNombreNoEspacio(nombre);

        if (e.isPresent()) {
            // Poner a null el campo equipo en todas las noticias que lo referencian
            List<Noticia> noticias = noticiaRepository.findByEquipoNoticia_NombreNoEspacio(nombre);
            for (Noticia n : noticias) {
                n.setEquipoNoticia(null);
                noticiaRepository.save(n);
            }
            e.get().getLiga().deleteEquipo(e.get());
            equipoRepository.delete(e.get());
        } else {
            throw new EquipoNotFoundException("Equipo no encontrado", HttpStatus.NOT_FOUND);
        }

    }

    public List<Equipo> getAllEquipos() {
        return equipoRepository.findAll();
    }

    public Page<Equipo> getEquiposPorLiga(String nombreLiga, Pageable pageable) {
        return equipoRepository.findByLiga_NombreNoEspacio(nombreLiga, pageable);
    }

    public Page<Equipo> findAllOrderByLigaNombre(Pageable pageable) {
        return equipoRepository.findAllOrderByLigaNombre(pageable);
    }
}