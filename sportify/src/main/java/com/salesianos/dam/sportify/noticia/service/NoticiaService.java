package com.salesianos.dam.sportify.noticia.service;

import com.salesianos.dam.sportify.error.EntidadNoEncontradaException;
import com.salesianos.dam.sportify.error.NoticiaNotFoundException;
import com.salesianos.dam.sportify.error.UserNotFoundException;
import com.salesianos.dam.sportify.noticia.dto.CreateNoticiaRequest;
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


}
