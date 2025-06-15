package com.salesianos.dam.sportify.noticia.dto;

import com.salesianos.dam.sportify.validation.UniqueTitular;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record CreateNoticiaRequest(

        @UniqueTitular(message = "{createNoticiaRequest.titular.uniqueTitular}")
        @NotBlank(message = "{createNoticiaRequest.titular.notBlank}")
        String titular,

        @NotBlank(message = "{createNoticiaRequest.cuerpo.notBlank}")
        String cuerpo,

        String autorUsername,

        @PastOrPresent(message = "{createNoticiaRequest.fechaPublicacion.pastOrPresent}")
        LocalDate fechaPublicacion,

        String nombreDeporte,

        String nombreEquipo,

        String nombreLiga

) {
}
