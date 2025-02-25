package com.salesianos.dam.sportify.user.dto;

import com.salesianos.dam.sportify.deporte.dto.GetNombreDeporteDto;
import com.salesianos.dam.sportify.equipo.dto.GetNombreEquipoDto;
import com.salesianos.dam.sportify.equipo.model.Equipo;
import com.salesianos.dam.sportify.user.model.User;
import org.springframework.data.domain.Page;

public record GetEquiposFavoritos(
        String username,
        Page<GetNombreEquipoDto> equiposFavoritos
) {
    public static GetEquiposFavoritos of(User username, Page<Equipo> equipos) {
        return new GetEquiposFavoritos(username.getUsername(), equipos.map(GetNombreEquipoDto::of));
    }
}
