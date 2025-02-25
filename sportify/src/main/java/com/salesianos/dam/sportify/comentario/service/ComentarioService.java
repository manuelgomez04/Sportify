package com.salesianos.dam.sportify.comentario.service;

import com.salesianos.dam.sportify.comentario.dto.CreateComentarioRequest;
import com.salesianos.dam.sportify.comentario.dto.EditComentarioRequest;
import com.salesianos.dam.sportify.comentario.model.Comentario;
import com.salesianos.dam.sportify.comentario.model.ComentariosPk;
import com.salesianos.dam.sportify.comentario.repo.ComentarioRepository;
import com.salesianos.dam.sportify.error.ComentarioNotFoundException;
import com.salesianos.dam.sportify.error.NoticiaNotFoundException;
import com.salesianos.dam.sportify.error.UserNotFoundException;
import com.salesianos.dam.sportify.noticia.model.Noticia;
import com.salesianos.dam.sportify.noticia.repo.NoticiaRepository;
import com.salesianos.dam.sportify.user.model.User;
import com.salesianos.dam.sportify.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final UserRepository userRepository;
    private final NoticiaRepository noticiaRepository;


    public Comentario createComentario(User autenticado, String slug, CreateComentarioRequest createComentarioRequest) {

        User u = userRepository.findFirstByUsername(autenticado.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado", HttpStatus.NOT_FOUND));

        Noticia n = noticiaRepository.findBySlug(slug)
                .orElseThrow(() -> new NoticiaNotFoundException("Noticia no encontrada", HttpStatus.NOT_FOUND));

        ComentariosPk id = new ComentariosPk(u.getId(), n.getID());

        Comentario c = Comentario.builder()
                .idComentario(id)
                .usuario(u)
                .noticia(n)
                .titulo(createComentarioRequest.titulo()) // Asignar el título correctamente
                .comentario(createComentarioRequest.comentario()) // Asignar el comentario correctamente
                .fechaComentario(LocalDateTime.now())
                .build();

        return comentarioRepository.save(c);
    }

    public Comentario editComentario(User user, EditComentarioRequest editComentarioRequest, String slug) {

        User u = userRepository.findFirstByUsername(user.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado", HttpStatus.NOT_FOUND));

        Noticia n = noticiaRepository.findBySlug(slug)
                .orElseThrow(() -> new NoticiaNotFoundException("Noticia no encontrada", HttpStatus.NOT_FOUND));

        ComentariosPk id = new ComentariosPk(u.getId(), n.getID());

        Comentario c = comentarioRepository.findById(id)
                .orElseThrow(() -> new ComentarioNotFoundException("Comentario no encontrado", HttpStatus.NOT_FOUND));


        c.setTitulo(editComentarioRequest.titulo());
        c.setComentario(editComentarioRequest.comentario());

        return comentarioRepository.save(c);


    }
}
