package com.salesianos.dam.sportify.noticia.dto;

import com.salesianos.dam.sportify.noticia.model.Noticia;

public record GetTitularDto(
        String titular,
        String slug
) {
    public static GetTitularDto of(Noticia noticia) {
        return new GetTitularDto(noticia.getTitular(), noticia.getSlug());
    }
}
