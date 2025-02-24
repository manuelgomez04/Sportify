package com.salesianos.dam.sportify.liga.dto;

public record CreateLigaRequest(
        String nombre,
        String nombreDeporte,
        String descripcion
) {
}
