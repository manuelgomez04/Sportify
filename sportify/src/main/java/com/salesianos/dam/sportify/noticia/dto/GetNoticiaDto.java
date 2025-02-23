package com.salesianos.dam.sportify.noticia.dto;

import com.salesianos.dam.sportify.noticia.model.Noticia;
import com.salesianos.dam.sportify.user.dto.GetUsuarioDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record GetNoticiaDto(
        UUID id,
        String titular,
        String cuerpo,
        List<String> multimedia,
        GetUsuarioDto usuario,
        LocalDate fechaCreacion
) {

    public static GetNoticiaDto of(Noticia n) {
        return new GetNoticiaDto(
                n.getID(),
                n.getTitular(),
                n.getCuerpo(),
                n.getMultimedia(),
                GetUsuarioDto.of(n.getAutor()),
                n.getFechaPublicacion()
        );
    }
}
