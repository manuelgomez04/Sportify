package com.salesianos.dam.sportify.equipo.dto;

import com.salesianos.dam.sportify.equipo.model.Equipo;

import java.time.LocalDate;
import java.util.UUID;

public record GetEquipoDto(
        UUID id,
        String nombre,
        String nombreNoEspacio,
        String nombreLiga,
        String ciudad,
        String pais,
        String escudo,
        LocalDate fechaCreacion,
        String nombreLigaNoEspacio

) {
    public static GetEquipoDto of(Equipo e) {
        return new GetEquipoDto(e.getId(), e.getNombre(), e.getNombreNoEspacio(), e.getLiga().getNombre(), e.getCiudad(), e.getPais(), e.getEscudo(), e.getFechaCreacion(), e.getLiga().getNombreNoEspacio());
    }
}
