package com.salesianos.dam.sportify.noticia.dto;

import com.salesianos.dam.sportify.comentario.dto.GetComentarioDto;
import com.salesianos.dam.sportify.comentario.model.Comentario;
import com.salesianos.dam.sportify.deporte.dto.GetNombreDeporteDto;
import com.salesianos.dam.sportify.equipo.dto.GetNombreEquipoDto;
import com.salesianos.dam.sportify.liga.dto.GetNombreLiga;
import com.salesianos.dam.sportify.noticia.model.Noticia;
import com.salesianos.dam.sportify.user.dto.GetUserNoAsociacionesDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record GetNoticiaComentarioDto(
        UUID id,
        String titular,
        String cuerpo,
        List<String> multimedia,
        GetUserNoAsociacionesDto usuario,
        LocalDate fechaCreacion,
        GetNombreEquipoDto equipo,
        GetNombreDeporteDto deporte,
        GetNombreLiga liga,
        String slug,
        Page<GetComentarioDto> comentarios
) {

    public static GetNoticiaComentarioDto of(Noticia n, Page<Comentario> comentarios) {
        return new GetNoticiaComentarioDto(
                n.getID(),
                n.getTitular(),
                n.getCuerpo(),
                n.getMultimedia(),
                GetUserNoAsociacionesDto.of(n.getAutor()),
                n.getFechaPublicacion(),
                n.getEquipoNoticia() != null ? GetNombreEquipoDto.of(n.getEquipoNoticia()) : null,
                n.getDeporteNoticia() != null ? GetNombreDeporteDto.of(n.getDeporteNoticia()) : null,
                n.getLigaNoticia() != null ? GetNombreLiga.of(n.getLigaNoticia()) : null,
                n.getSlug(),
                comentarios.map(GetComentarioDto::of)
        );
    }
}
