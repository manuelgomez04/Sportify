package com.salesianos.dam.sportify.equipo.dto;

import com.salesianos.dam.sportify.equipo.model.Equipo;

public record GetNombreEquipoDto(
        String nombreEquipo,
        String escudo
) {

    public static GetNombreEquipoDto of(Equipo nombreEquipo) {
        return new GetNombreEquipoDto(nombreEquipo.getNombre(), nombreEquipo.getEscudo());
    }
}
