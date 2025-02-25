package com.salesianos.dam.sportify.like.service;

import com.salesianos.dam.sportify.comentario.model.Comentario;
import com.salesianos.dam.sportify.comentario.model.ComentariosPk;
import com.salesianos.dam.sportify.error.ComentarioNotFoundException;
import com.salesianos.dam.sportify.error.NoticiaNotFoundException;
import com.salesianos.dam.sportify.error.UserNotFoundException;
import com.salesianos.dam.sportify.like.dto.CreateLikeRequest;
import com.salesianos.dam.sportify.like.model.Like;
import com.salesianos.dam.sportify.like.model.LikePk;
import com.salesianos.dam.sportify.like.repo.LikeRepository;
import com.salesianos.dam.sportify.noticia.model.Noticia;
import com.salesianos.dam.sportify.noticia.repo.NoticiaRepository;
import com.salesianos.dam.sportify.user.model.User;
import com.salesianos.dam.sportify.user.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final NoticiaRepository noticiaRepository;
    private final UserRepository userRepository;

    public Like createLike(User autenticado, String slug, CreateLikeRequest createLikeRequest) {
        User u = userRepository.findFirstByUsername(autenticado.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado", HttpStatus.NOT_FOUND));

        Noticia n = noticiaRepository.findBySlug(slug)
                .orElseThrow(() -> new NoticiaNotFoundException("Noticia no encontrada", HttpStatus.NOT_FOUND));

        LikePk id = new LikePk(u.getId(), n.getID());

        Like l = Like.builder()
                .idLike(id)
                .usuario(u)
                .noticia_like(n)
                .build();

        return likeRepository.save(l);
    }

    @Transactional
    public void deleteLike(String user, String slug) {
        User u = userRepository.findFirstByUsername(user)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado", HttpStatus.NOT_FOUND));

        Noticia n = noticiaRepository.findBySlug(slug)
                .orElseThrow(() -> new NoticiaNotFoundException("Noticia no encontrada", HttpStatus.NOT_FOUND));

        LikePk id = new LikePk(u.getId(), n.getID());

        Like l = likeRepository.findById(id)
                .orElseThrow(() -> new ComentarioNotFoundException("Comentario no encontrado", HttpStatus.NOT_FOUND));

        likeRepository.delete(l);

    }
}
