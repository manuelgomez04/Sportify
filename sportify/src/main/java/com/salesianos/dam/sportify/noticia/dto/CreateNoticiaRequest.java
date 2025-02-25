package com.salesianos.dam.sportify.noticia.dto;

import com.salesianos.dam.sportify.validation.UniqueTitular;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.List;

public record CreateNoticiaRequest(

        @UniqueTitular(message = "{createNoticiaRequest.titular.uniqueTitular}")
        @NotBlank(message = "{createNoticiaRequest.titular.notBlank}")
        String titular,

        @NotBlank(message = "{createNoticiaRequest.cuerpo.notBlank}")
        String cuerpo,

        String autorUsername,

        @NotNull(message = "{createNoticiaRequest.fechaPublicacion.notNull}")
        @PastOrPresent(message = "{createNoticiaRequest.fechaPublicacion.pastOrPresent}")
        LocalDate fechaPublicacion,


        String nombreDeporte,

        String nombreEquipo

) {
}
