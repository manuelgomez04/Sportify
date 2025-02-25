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
    @MapsId("idUsuario")
    @JoinColumn(name = "usuario_id")
    @JsonBackReference
    private User usuario;

    @ManyToOne
    @MapsId("idNoticia")
    @JoinColumn(name = "noticia_id")
    @JsonBackReference
    private Noticia noticia;



}
