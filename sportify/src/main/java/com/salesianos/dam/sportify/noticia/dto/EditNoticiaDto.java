package com.salesianos.dam.sportify.noticia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record EditNoticiaDto(
        @NotBlank(message = "{editNoticiaDto.titular.notBlank}")
        String titular,

        @NotBlank(message = "{editNoticiaDto.cuerpo.notBlank}")
        String cuerpo,


        List<String> multimedia


) {
}
