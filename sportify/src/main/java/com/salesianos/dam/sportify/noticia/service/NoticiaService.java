package com.salesianos.dam.sportify.noticia.service;

import com.salesianos.dam.sportify.comentario.model.Comentario;
import com.salesianos.dam.sportify.comentario.repo.ComentarioRepository;
import com.salesianos.dam.sportify.deporte.dto.FollowDeporteRequest;
import com.salesianos.dam.sportify.deporte.model.Deporte;
import com.salesianos.dam.sportify.deporte.repo.DeporteRepository;
import com.salesianos.dam.sportify.equipo.dto.FollowEquipoRequest;
import com.salesianos.dam.sportify.equipo.model.Equipo;
import com.salesianos.dam.sportify.equipo.repo.EquipoRepository;
import com.salesianos.dam.sportify.error.*;
import com.salesianos.dam.sportify.files.model.FileMetadata;
import com.salesianos.dam.sportify.files.service.StorageService;
import com.salesianos.dam.sportify.liga.dto.FollowLigaRequest;
import com.salesianos.dam.sportify.liga.model.Liga;
import com.salesianos.dam.sportify.liga.repo.LigaRepository;
import com.salesianos.dam.sportify.noticia.dto.CreateNoticiaRequest;
import com.salesianos.dam.sportify.noticia.dto.EditNoticiaDto;
import com.salesianos.dam.sportify.noticia.model.Noticia;
import com.salesianos.dam.sportify.noticia.repo.NoticiaRepository;
import com.salesianos.dam.sportify.query.NoticiaSpecificationBuilder;
import com.salesianos.dam.sportify.user.model.User;
import com.salesianos.dam.sportify.user.repo.UserRepository;
import com.salesianos.dam.sportify.util.SearchCriteria;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticiaService {

    private final NoticiaRepository noticiaRepository;
    private final UserRepository userRepository;
    private final StorageService storageService;
    private final DeporteRepository deporteRepository;
    private final EquipoRepository equipoRepository;
    private final LigaRepository ligaRepository;
    private final ComentarioRepository comentarioRepository;


    @Data
    @AllArgsConstructor
    public static class NoticiaWithComentarios {
        private Noticia noticia;
        private Page<Comentario> comentarios;
    }

    public Noticia findBySlug(String n) {
        return noticiaRepository.findBySlug(n).orElseThrow(() -> new NoticiaNotFoundException("No se ha encontrado la noticia", HttpStatus.NOT_FOUND));
    }

    public User findUserByUsername(String username) {
        return userRepository.findFirstByUsername(username).orElseThrow(() -> new UserNotFoundException("No se ha encontrado el usuario", HttpStatus.NOT_FOUND));
    }


    public Liga findLigaByNombre(String nombre) {
        return ligaRepository.findByNombreNoEspacio(nombre).orElseThrow(() -> new LigaNotFoundException("No se ha encontrado la liga", HttpStatus.NOT_FOUND));
    }

    @Transactional
    public Noticia saveNoticia(CreateNoticiaRequest createNoticiaRequest, User username, List<MultipartFile> files) {

        List<FileMetadata> fileMetadata = files.stream()
                .map(storageService::store)
                .toList();

        List<String> imageUrls = fileMetadata.stream()
                .map(FileMetadata::getFilename)
                .map(this::getImageUrl)
                .toList();

        Deporte d = deporteRepository.findByNombreNoEspacio(createNoticiaRequest.nombreDeporte())
                .orElseThrow(() -> new DeporteNotFoundException("No se ha encontrado el deporte", HttpStatus.NOT_FOUND));

        Equipo e = equipoRepository.findByNombreNoEspacio(createNoticiaRequest.nombreEquipo())
                .orElseThrow(() -> new EquipoNotFoundException("No se ha encontrado el equipo", HttpStatus.NOT_FOUND));

        Liga l = findLigaByNombre(createNoticiaRequest.nombreLiga());

        Noticia n = noticiaRepository.save(Noticia.builder()
                .titular(createNoticiaRequest.titular())
                .cuerpo(createNoticiaRequest.cuerpo())
                .fechaPublicacion(LocalDate.now()) // <-- fecha automática
                .multimedia(imageUrls)
                .deporteNoticia(d)
                .equipoNoticia(e)
                .ligaNoticia(l)
                .build());

        n.generarSlug();

        noticiaRepository.save(n);

        findUserByUsername(username.getUsername()).addNoticia(n);
        noticiaRepository.save(n);


        return n;

    }

    public String getImageUrl(String filename) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(filename)
                .toUriString();
    }

    public Page<Noticia> findAllNoticias(Pageable pageable) {
        return noticiaRepository.findAll(pageable);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @noticiaService.esAutorDeNoticiaSlug(authentication.principal.username,#slug )")
    public Noticia editNoticia(String slug, EditNoticiaDto editNoticiaDto, User usuarioAutenticado, List<MultipartFile> files) {

        Noticia noticia = findBySlug(slug);

        if (editNoticiaDto.titular() != null) {
            noticia.setTitular(editNoticiaDto.titular());
        }
        if (editNoticiaDto.cuerpo() != null) {
            noticia.setCuerpo(editNoticiaDto.cuerpo());
        }

        if (files != null && !files.isEmpty()) {
            List<FileMetadata> fileMetadata = files.stream()
                    .map(storageService::store)
                    .toList();

            List<String> imageUrls = fileMetadata.stream()
                    .map(FileMetadata::getFilename)
                    .map(this::getImageUrl)
                    .toList();

            noticia.setMultimedia(imageUrls);
        }

        noticia.generarSlug();

        return noticiaRepository.save(noticia);
    }


    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @noticiaService.esAutorDeNoticiaSlug(authentication.principal.username,#slug )")
    public void deleteNoticia(String slug, User usuarioAutenticado) {
        Noticia noticia = findBySlug(slug);


        noticia.getAutor().removeNoticia(noticia);
        noticiaRepository.delete(noticia);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @noticiaService.esAutorDeNoticiaSlug(authentication.principal.username,#slug )")
    public Noticia addDeporteEnNoticia(User autenticado, String slug, FollowDeporteRequest followDeporteRequest) {

        User u = findUserByUsername(autenticado.getUsername());

        Noticia n = findBySlug(slug);


        Deporte d = deporteRepository.findByNombreNoEspacio(followDeporteRequest.nombreDeporte())
                .orElseThrow(() -> new DeporteNotFoundException("No se ha encontrado el deporte", HttpStatus.NOT_FOUND));


        n.setDeporteNoticia(d);

        return noticiaRepository.save(n);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @noticiaService.esAutorDeNoticiaSlug(authentication.principal.username,#slug )")
    public Noticia addEquipoEnNoticia(User autenticado, String slug, FollowEquipoRequest followEquipoRequest) {

        User u = findUserByUsername(autenticado.getUsername());

        Noticia n = findBySlug(slug);


        Equipo e = equipoRepository.findByNombreNoEspacio(followEquipoRequest.nombreEquipo())
                .orElseThrow(() -> new EquipoNotFoundException("No se ha encontrado el equipo", HttpStatus.NOT_FOUND));


        n.setEquipoNoticia(e);

        return noticiaRepository.save(n);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @noticiaService.esAutorDeNoticiaSlug(authentication.principal.username,#slug )")
    public Noticia addLigaNoticia(User autenticado, String slug, FollowLigaRequest followLigaRequest) {

        User u = findUserByUsername(autenticado.getUsername());

        Noticia n = findBySlug(slug);


        Liga l = findLigaByNombre(followLigaRequest.nombreLiga());

        n.setLigaNoticia(l);

        return noticiaRepository.save(n);

    }

    public NoticiaWithComentarios findNoticiaWithComentariosBySlug(String slug, Pageable pageable) {
        Noticia noticia = noticiaRepository.findBySlug(slug)
                .orElseThrow(() -> new NoticiaNotFoundException("Noticia no encontrada", HttpStatus.NOT_FOUND));

        Page<Comentario> comentarios = comentarioRepository.findByNoticiaSlug(slug, pageable);
        return new NoticiaWithComentarios(noticia, comentarios);
    }

    public Page<Noticia> getNoticiasFiltradas(String username, String slug, String deporte, String liga, LocalDate fechaInicio, LocalDate fechaFin, Pageable pageable) {
        List<SearchCriteria> params = new ArrayList<>();

        if (username != null && !username.isBlank()) {
            params.add(new SearchCriteria("autor.username", ":", username));
        }
        if (slug != null && !slug.isBlank()) {
            params.add(new SearchCriteria("slug", ":", slug));
        }
        if (deporte != null && !deporte.isBlank()) {
            params.add(new SearchCriteria("deporteNoticia.nombre", ":", deporte.toLowerCase()));
        }
        if (liga != null && !liga.isBlank()) {
            params.add(new SearchCriteria("ligaNoticia.nombreNoEspacio", ":", liga.toLowerCase()));
        }
        if (fechaInicio != null) {
            params.add(new SearchCriteria("fechaPublicacion", ">", fechaInicio));
        }
        if (fechaFin != null) {
            params.add(new SearchCriteria("fechaPublicacion", "<", fechaFin));
        }

        NoticiaSpecificationBuilder builder = new NoticiaSpecificationBuilder(params);
        Specification<Noticia> spec = builder.build();

        return noticiaRepository.findAll(spec, pageable);
    }

}
