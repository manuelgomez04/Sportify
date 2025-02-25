package com.salesianos.dam.sportify.deporte.dto;

import com.salesianos.dam.sportify.deporte.model.Deporte;

public record GetNombreDeporteDto(

        String nombreDeporte
) {
    public static GetNombreDeporteDto of(Deporte nombreDeporte) {
        return new GetNombreDeporteDto(nombreDeporte.getNombre());
    }
}
