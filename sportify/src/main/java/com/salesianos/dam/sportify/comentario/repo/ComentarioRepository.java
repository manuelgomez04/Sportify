package com.salesianos.dam.sportify.comentario.repo;

import com.salesianos.dam.sportify.comentario.model.Comentario;
import com.salesianos.dam.sportify.comentario.model.ComentariosPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface ComentarioRepository extends JpaRepository<Comentario, ComentariosPk> {

    @Query("""
            SELECT c FROM Comentario c 
            JOIN FETCH c.usuario 
            JOIN FETCH c.noticia
            WHERE c.idComentario = :idComentario
            """)
    Optional<Comentario> findComentarioById(@Param("idComentario") ComentariosPk idComentario);
}
