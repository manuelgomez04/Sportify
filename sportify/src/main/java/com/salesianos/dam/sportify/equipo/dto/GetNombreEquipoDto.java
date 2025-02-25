package com.salesianos.dam.sportify.equipo.dto;

import com.salesianos.dam.sportify.equipo.model.Equipo;

public record GetNombreEquipoDto(
        String nombreEquipo
) {

    public static GetNombreEquipoDto of(Equipo nombreEquipo) {
        return new GetNombreEquipoDto(nombreEquipo.getNombre());
    }
}
