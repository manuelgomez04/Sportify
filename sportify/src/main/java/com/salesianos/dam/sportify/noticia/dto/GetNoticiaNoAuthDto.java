package com.salesianos.dam.sportify.noticia.dto;

import com.salesianos.dam.sportify.deporte.dto.GetNombreDeporteDto;
import com.salesianos.dam.sportify.equipo.dto.GetNombreEquipoDto;
import com.salesianos.dam.sportify.liga.dto.GetNombreLiga;
import com.salesianos.dam.sportify.noticia.model.Noticia;
import com.salesianos.dam.sportify.user.dto.GetUserNoAsociacionesDto;
import com.salesianos.dam.sportify.user.dto.GetUsuarioDto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record GetNoticiaNoAuthDto(
        String titular,
        String cuerpo,
        List<String> multimedia,
        LocalDate fechaCreacion,
        String slug,
        GetUserNoAsociacionesDto usuario,
        GetNombreEquipoDto equipo,
        GetNombreLiga liga,
        GetNombreDeporteDto deporte
) {

    public static GetNoticiaNoAuthDto of(Noticia n) {
        return new GetNoticiaNoAuthDto(
                n.getTitular(),
                n.getCuerpo(),
                n.getMultimedia() != null
                    ? n.getMultimedia().stream()
                        .map(filename -> "/download/" + filename)
                        .collect(Collectors.toList())
                    : null,
                n.getFechaPublicacion(),
                n.getSlug(),
                GetUserNoAsociacionesDto.of(n.getAutor()),
                n.getEquipoNoticia() != null ? GetNombreEquipoDto.of(n.getEquipoNoticia()) : null,
                n.getLigaNoticia() != null ? GetNombreLiga.of(n.getLigaNoticia()) : null,
                n.getDeporteNoticia() != null ? GetNombreDeporteDto.of(n.getDeporteNoticia()) : null
        );

    }
}
