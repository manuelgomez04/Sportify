package com.salesianos.dam.sportify.like.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class LikePk  implements Serializable {
    @Column(name = "id_usuario_like")
    private UUID idUsuario_like;

    @Column(name = "id_noticia_like")
    private UUID idNoticia_like;
}
