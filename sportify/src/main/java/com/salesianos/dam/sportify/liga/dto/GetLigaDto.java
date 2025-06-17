package com.salesianos.dam.sportify.liga.dto;

import com.salesianos.dam.sportify.liga.model.Liga;

import java.util.UUID;

public record GetLigaDto(
        UUID id,
        String nombre,
        String descripcion,
        String nombreDeporte,
        String nombreSinEspacio,
        String imagen,
        String deporteNoEspacio,
        String deporteNombre

) {

    public static GetLigaDto of(Liga l) {
        return new GetLigaDto(
                l.getId(),
                l.getNombre(),
                l.getDescripcion(),
                l.getDeporte().getNombreNoEspacio(),
                l.getNombreNoEspacio(),
                l.getImagen(),
                l.getDeporte().getNombreNoEspacio(),
                l.getDeporte().getNombre()
        );
    }
}
