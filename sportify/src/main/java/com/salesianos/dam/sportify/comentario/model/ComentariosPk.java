package com.salesianos.dam.sportify.comentario.model;

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
public class ComentariosPk implements Serializable {

    @Column(name = "id_usuario_comentario")
    private UUID idUsuario_comentario;

    @Column(name = "id_noticia_comentario")
    private UUID idNoticia_comentario;
}
