package com.salesianos.dam.sportify.noticia.dto;

import com.salesianos.dam.sportify.deporte.dto.GetDeporteDto;
import com.salesianos.dam.sportify.deporte.dto.GetNombreDeporteDto;
import com.salesianos.dam.sportify.equipo.dto.GetNombreEquipoDto;
import com.salesianos.dam.sportify.liga.dto.GetNombreLiga;
import com.salesianos.dam.sportify.noticia.model.Noticia;
import com.salesianos.dam.sportify.user.dto.GetUserNoAsociacionesDto;
import com.salesianos.dam.sportify.user.dto.GetUsuarioDto;
import org.hibernate.Hibernate;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record GetNoticiaDto(
        UUID id,
        String titular,
        String cuerpo,
        List<String> multimedia,
        GetUserNoAsociacionesDto usuario,
        LocalDate fechaCreacion,
        GetNombreEquipoDto equipo,
        GetNombreDeporteDto deporte,
        GetNombreLiga liga,
        String slug
) {

    public static GetNoticiaDto of(Noticia n) {

        return new GetNoticiaDto(
                n.getID(),
                n.getTitular(),
                n.getCuerpo(),
                n.getMultimedia(),
                GetUserNoAsociacionesDto.of(n.getAutor()),
                n.getFechaPublicacion(),
                n.getEquipoNoticia() != null ? GetNombreEquipoDto.of(n.getEquipoNoticia()) : null,
                n.getDeporteNoticia() != null ? GetNombreDeporteDto.of(n.getDeporteNoticia()) : null,
                n.getLigaNoticia() != null ? GetNombreLiga.of(n.getLigaNoticia()) : null,

                n.getSlug()
        );
    }
}
