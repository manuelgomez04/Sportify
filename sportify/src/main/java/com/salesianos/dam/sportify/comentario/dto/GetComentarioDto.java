package com.salesianos.dam.sportify.comentario.dto;

import com.salesianos.dam.sportify.comentario.model.Comentario;
import com.salesianos.dam.sportify.noticia.dto.GetNoticiaDto;
import com.salesianos.dam.sportify.noticia.dto.GetTitularDto;
import com.salesianos.dam.sportify.user.dto.GetUserNoAsociacionesDto;

import java.time.LocalDateTime;

public record GetComentarioDto(
        GetUserNoAsociacionesDto usuario,
        GetTitularDto noticia,
        String comentario,
        String titular,
        LocalDateTime fecha
) {
    public static GetComentarioDto of(Comentario c) {
        return new GetComentarioDto(
                GetUserNoAsociacionesDto.of(c.getUsuario()),
                GetTitularDto.of(c.getNoticia_comentario()),
                c.getComentario(),
                c.getTitulo(),
                c.getFechaComentario()
        );
    }
}
