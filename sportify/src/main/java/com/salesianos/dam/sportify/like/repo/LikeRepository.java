package com.salesianos.dam.sportify.like.repo;

import com.salesianos.dam.sportify.like.model.Like;
import com.salesianos.dam.sportify.like.model.LikePk;
import com.salesianos.dam.sportify.noticia.model.Noticia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, LikePk> {

    @Query("""
            SELECT l FROM Like l
            JOIN FETCH l.usuario
            JOIN FETCH l.noticia_like
            WHERE l.idLike = :idLike
            """)
    Optional<Like> findLikeById(@Param("idLike") LikePk idLike);

    @Query("SELECT l FROM Like l JOIN l.noticia_like n WHERE n.slug = :slug")
    Page<Like> findByNoticiaSlug(@Param("slug") String slug, Pageable pageable);

    @Query("SELECT l.noticia_like FROM Like l WHERE l.usuario.username = :username")
    Page<Noticia> findNoticiasLikedByUsuario(@Param("username") String username, Pageable pageable);

}
