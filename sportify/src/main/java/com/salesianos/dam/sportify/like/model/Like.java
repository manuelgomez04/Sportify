package com.salesianos.dam.sportify.like.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.salesianos.dam.sportify.noticia.model.Noticia;
import com.salesianos.dam.sportify.user.model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "like_user")
public class Like {

    @EmbeddedId
    private LikePk idLike;

    @ManyToOne
    @MapsId("idUsuario_like")
    @JoinColumn(name = "id_usuario_like")
    @JsonBackReference
    private User usuario;


    @ManyToOne
    @MapsId("idNoticia_like")
    @JoinColumn(name = "id_noticia_like")
    @JsonBackReference
    private Noticia noticia_like;

}
