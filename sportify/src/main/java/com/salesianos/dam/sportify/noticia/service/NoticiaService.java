package com.salesianos.dam.sportify.noticia.service;

import com.salesianos.dam.sportify.error.*;
import com.salesianos.dam.sportify.noticia.dto.CreateNoticiaRequest;
import com.salesianos.dam.sportify.noticia.dto.EditNoticiaDto;
import com.salesianos.dam.sportify.noticia.dto.GetNoticiaDto;
import com.salesianos.dam.sportify.noticia.model.Noticia;
import com.salesianos.dam.sportify.noticia.repo.NoticiaRepository;
import com.salesianos.dam.sportify.user.model.User;
import com.salesianos.dam.sportify.user.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticiaService {

    private final NoticiaRepository noticiaRepository;
    private final UserRepository userRepository;


    public Noticia findById(Noticia noticia) {
        Optional<Noticia> n = noticiaRepository.findById(noticia.getID());

        if (n.isPresent()) {
            return n.get();
        } else {
            throw new NoticiaNotFoundException("No se ha encontrado la noticia", HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    public Noticia saveNoticia(CreateNoticiaRequest createNoticiaRequest, User username) {

        Noticia n = noticiaRepository.save(Noticia.builder()
                .titular(createNoticiaRequest.titular())
                .cuerpo(createNoticiaRequest.cuerpo())
                .fechaPublicacion(createNoticiaRequest.fechaPublicacion())
                .multimedia(createNoticiaRequest.multimedia())
                .build());

        n.generarSlug();

        noticiaRepository.save(n);

        if (userRepository.findFirstByUsername(username.getUsername()).isPresent()) {
            userRepository.findFirstByUsername(username.getUsername()).get().addNoticia(n);
            noticiaRepository.save(n);
        } else {
            throw new UserNotFoundException("No se ha encontrado el usuario", HttpStatus.NOT_FOUND);
        }

        return n;

    }

    public Page<Noticia> findAllNoticias(Pageable pageable) {
        return noticiaRepository.findAll(pageable);
    }

    @Transactional
    public Noticia editNoticia(String slug, EditNoticiaDto createNoticiaRequest, User usuarioAutenticado) {
        Noticia noticia = noticiaRepository.findBySlug(slug)
                .orElseThrow(() -> new NoticiaNotFoundException("No se ha encontrado la noticia", HttpStatus.NOT_FOUND));

        if (!esAutorDeNoticia(usuarioAutenticado, noticia) && !esAdmin(usuarioAutenticado)) {
            throw new UnauthorizedEditException("No tienes permiso para editar esta noticia", HttpStatus.FORBIDDEN);
        }

        if (createNoticiaRequest.titular() != null) {
            noticia.setTitular(createNoticiaRequest.titular());
        }
        if (createNoticiaRequest.cuerpo() != null) {
            noticia.setCuerpo(createNoticiaRequest.cuerpo());
        }
        if (createNoticiaRequest.multimedia() != null) {
            noticia.setMultimedia(createNoticiaRequest.multimedia());
        }
        noticia.generarSlug();

        return noticiaRepository.save(noticia);
    }
    private boolean esAutorDeNoticia(User usuario, Noticia noticia) {
        return noticia.getAutor().getId().equals(usuario.getId());
    }

    private boolean esAdmin(User usuario) {
        return usuario.getRoles().stream()
                .anyMatch(role -> role.name().equals("ADMIN"));
    }

    @Transactional
    public void deleteNoticia(String slug, User usuarioAutenticado) {
        Noticia noticia = noticiaRepository.findBySlug(slug)
                .orElseThrow(() -> new NoticiaNotFoundException("No se ha encontrado la noticia", HttpStatus.NOT_FOUND));

        if (!esAutorDeNoticia(usuarioAutenticado, noticia) && !esAdmin(usuarioAutenticado)) {
            throw new UnauthorizedDeleteException("No tienes permiso para eliminar esta noticia", HttpStatus.FORBIDDEN);
        }

        noticia.getAutor().removeNoticia(noticia);
        noticiaRepository.delete(noticia);
    }

}
