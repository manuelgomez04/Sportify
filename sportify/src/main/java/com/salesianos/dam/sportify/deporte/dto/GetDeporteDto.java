package com.salesianos.dam.sportify.deporte.dto;

import com.salesianos.dam.sportify.deporte.model.Deporte;

import java.util.UUID;

public record GetDeporteDto(

        UUID id,
        String nombre,
        String descripcion,
        String imagen,
        String nombreNoEspacio
) {
    public static GetDeporteDto of(Deporte d) {
        return new GetDeporteDto(d.getId(), d.getNombre(), d.getDescripcion(), d.getImagen(), d.getNombreNoEspacio());
    }
}
