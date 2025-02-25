package com.salesianos.dam.sportify.like.dto;

import com.salesianos.dam.sportify.like.model.Like;
import com.salesianos.dam.sportify.noticia.dto.GetTitularDto;
import com.salesianos.dam.sportify.user.dto.GetUserNoAsociacionesDto;

public record GetLikeDto(
        GetUserNoAsociacionesDto usuario,
        GetTitularDto noticia
) {

    public static GetLikeDto of(Like l) {
        return new GetLikeDto(
                GetUserNoAsociacionesDto.of(l.getUsuario()),
                GetTitularDto.of(l.getNoticia_like())
        );
    }
}
