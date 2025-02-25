package com.salesianos.dam.sportify.noticia.dto;

import com.salesianos.dam.sportify.deporte.dto.GetNombreDeporteDto;
import com.salesianos.dam.sportify.noticia.model.Noticia;
import com.salesianos.dam.sportify.user.dto.GetUserNoAsociacionesDto;
import com.salesianos.dam.sportify.user.dto.GetUsuarioDto;

import java.time.LocalDate;
import java.util.List;

public record GetNoticiaNoAuthDto(
        String titular,
        String cuerpo,
        List<String> multimedia,
        LocalDate fechaCreacion,
        String slug,
        GetUserNoAsociacionesDto usuario,
        GetNombreDeporteDto deporte
) {

    public static GetNoticiaNoAuthDto of(Noticia n) {
        return new GetNoticiaNoAuthDto(
                n.getTitular(),
                n.getCuerpo(),
                n.getMultimedia(),
                n.getFechaPublicacion(),
                n.getSlug(),
                GetUserNoAsociacionesDto.of(n.getAutor()),

                n.getDeporteNoticia() != null ? GetNombreDeporteDto.of(n.getDeporteNoticia()) : null);
    }
}
