package com.salesianos.dam.sportify.noticia.repo;

import com.salesianos.dam.sportify.comentario.model.Comentario;
import com.salesianos.dam.sportify.noticia.model.Noticia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoticiaRepository extends JpaRepository<Noticia, UUID>, JpaSpecificationExecutor<Noticia> {


    Page<Noticia> findAll(Pageable pageable);

    boolean existsByTitular(String titular);

    Optional<Noticia> findBySlug(String slug);

    @Query("SELECT n FROM Noticia n LEFT JOIN FETCH n.comentarios c WHERE n.slug = :slug")
    Page<Noticia> findNoticiaWithComentariosPaginadosBySlug(@Param("slug") String slug, Pageable pageable);

    Page<Noticia> findByAutor_Username(String username, Pageable pageable);

    // Consulta para traer noticias por deporte (por objeto Deporte)
    List<Noticia> findByDeporteNoticia_NombreNoEspacio(String nombreNoEspacio);

    List<Noticia> findByEquipoNoticia_NombreNoEspacio(String nombreNoEspacio);

    List<Noticia> findByLigaNoticia_NombreNoEspacio(String nombreNoEspacio);
  
}

