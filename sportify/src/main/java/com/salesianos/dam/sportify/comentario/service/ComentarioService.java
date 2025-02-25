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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
                .noticia_comentario(n)
                .titulo(createComentarioRequest.titulo())
                .comentario(createComentarioRequest.comentario())
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


        if (editComentarioRequest.comentario() != null) {
            c.setComentario(editComentarioRequest.comentario());
        }
        if (editComentarioRequest.titulo() != null) {
            c.setTitulo(editComentarioRequest.titulo());

        }


        return comentarioRepository.save(c);


    }


    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteComentario( String slug, String user) {
        User u = userRepository.findFirstByUsername(user)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado", HttpStatus.NOT_FOUND));

        Noticia n = noticiaRepository.findBySlug(slug)
                .orElseThrow(() -> new NoticiaNotFoundException("Noticia no encontrada", HttpStatus.NOT_FOUND));

        ComentariosPk id = new ComentariosPk(u.getId(), n.getID());

        Comentario c = comentarioRepository.findById(id)
                .orElseThrow(() -> new ComentarioNotFoundException("Comentario no encontrado", HttpStatus.NOT_FOUND));

        comentarioRepository.delete(c);

    }

    @Transactional
    public void deleteComentarioMe(String user, String slug) {
        User u = userRepository.findFirstByUsername(user)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado", HttpStatus.NOT_FOUND));

        Noticia n = noticiaRepository.findBySlug(slug)
                .orElseThrow(() -> new NoticiaNotFoundException("Noticia no encontrada", HttpStatus.NOT_FOUND));

        ComentariosPk id = new ComentariosPk(u.getId(), n.getID());

        Comentario c = comentarioRepository.findById(id)
                .orElseThrow(() -> new ComentarioNotFoundException("Comentario no encontrado", HttpStatus.NOT_FOUND));

        comentarioRepository.delete(c);

    }

    public boolean isOwner(String user, String slug) {
        User u = userRepository.findFirstByUsername(user)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado", HttpStatus.NOT_FOUND));

        Noticia n = noticiaRepository.findBySlug(slug)
                .orElseThrow(() -> new NoticiaNotFoundException("Noticia no encontrada", HttpStatus.NOT_FOUND));

        ComentariosPk id = new ComentariosPk(u.getId(), n.getID());

        Comentario c = comentarioRepository.findById(id)
                .orElseThrow(() -> new ComentarioNotFoundException("Comentario no encontrado", HttpStatus.NOT_FOUND));

        return c.getUsuario().equals(u);
    }

    public Page<Comentario> findComentariosByNoticiaSlug(String slug, Pageable pageable) {
        return comentarioRepository.findByNoticiaSlug(slug, pageable);
    }
}
