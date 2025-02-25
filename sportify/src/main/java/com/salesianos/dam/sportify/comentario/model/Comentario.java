package com.salesianos.dam.sportify.comentario.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.salesianos.dam.sportify.noticia.model.Noticia;
import com.salesianos.dam.sportify.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Comentario {

    @EmbeddedId
    private ComentariosPk idComentario;

    private String titulo;
    private String comentario;

    private LocalDateTime fechaComentario;


    @ManyToOne
    @MapsId("idUsuario_comentario")
    @JoinColumn(name = "id_usuario_comentario")
    @JsonBackReference
    private User usuario;

    @ManyToOne
    @MapsId("idNoticia_comentario")
    @JoinColumn(name = "id_noticia_comentario")
    @JsonBackReference
    private Noticia noticia_comentario;


}
