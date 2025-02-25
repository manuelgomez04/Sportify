package com.salesianos.dam.sportify.comentario.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateComentarioRequest(

        String username,

        String titular,

        @NotBlank
        @NotNull
        @Size(min = 1, max = 255)
        String titulo,
        @Size(min = 20, max = 500)
        String comentario
) {
}
