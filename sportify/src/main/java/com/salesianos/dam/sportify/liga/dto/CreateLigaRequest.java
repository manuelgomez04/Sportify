package com.salesianos.dam.sportify.liga.dto;

import com.salesianos.dam.sportify.validation.UniqueLigaName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateLigaRequest(

        @UniqueLigaName
        @NotBlank
        @NotNull
        String nombre,

        @NotBlank
        @NotNull
        String nombreDeporte,

        @NotBlank
        @NotNull
        String descripcion
) {
}
