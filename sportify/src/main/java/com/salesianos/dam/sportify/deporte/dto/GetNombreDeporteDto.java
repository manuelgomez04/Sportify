package com.salesianos.dam.sportify.deporte.dto;

import com.salesianos.dam.sportify.deporte.model.Deporte;

public record GetNombreDeporteDto(

        String nombreDeporte,
        String imagen
) {
    public static GetNombreDeporteDto of(Deporte nombreDeporte) {
        return new GetNombreDeporteDto(nombreDeporte.getNombre(), nombreDeporte.getImagen());
    }
}
