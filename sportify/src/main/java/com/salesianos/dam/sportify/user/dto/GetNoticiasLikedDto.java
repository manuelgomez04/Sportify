package com.salesianos.dam.sportify.user.dto;

import com.salesianos.dam.sportify.noticia.dto.GetNoticiaDto;
import com.salesianos.dam.sportify.noticia.model.Noticia;
import com.salesianos.dam.sportify.user.model.User;
import org.springframework.data.domain.Page;

public record GetNoticiasLikedDto(
        String username,
        Page<GetNoticiaDto> noticiasLiked
) {
    public static GetNoticiasLikedDto of(User username, Page<Noticia> noticias) {
        return new GetNoticiasLikedDto(username.getUsername(), noticias.map(GetNoticiaDto::of));
    }
}
