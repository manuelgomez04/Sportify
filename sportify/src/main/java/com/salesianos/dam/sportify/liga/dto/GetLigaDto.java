package com.salesianos.dam.sportify.liga.dto;

import com.salesianos.dam.sportify.liga.model.Liga;

import java.util.UUID;

public record GetLigaDto(
        UUID id,
        String nombre,
        String descripcion,
        String nombreDeporte,
        String nombreSinEspacio
) {

    public static GetLigaDto of(Liga l) {
        return new GetLigaDto(l.getId(), l.getNombre(), l.getDescripcion(), l.getDeporte().getNombre(), l.getNombreNoEspacio());
    }
}
