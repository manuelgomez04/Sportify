package com.salesianos.dam.sportify.liga.dto;

import com.salesianos.dam.sportify.liga.model.Liga;

public record GetNombreLiga(
        String nombreLiga
) {

    public static GetNombreLiga of(Liga nombreLiga) {
        return new GetNombreLiga(nombreLiga.getNombre());
    }
}
