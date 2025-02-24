package com.salesianos.dam.sportify.liga.dto;

import com.salesianos.dam.sportify.validation.UniqueLigaName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateLigaRequest(

        @UniqueLigaName(message = "{createLigaRequest.nombre.uniqueLigaName}")
        @NotBlank(message = "{createLigaRequest.nombre.notBlank}")
        @NotNull(message = "{createLigaRequest.nombre.notNull}")
        String nombre,


        @NotBlank(message = "{createLigaRequest.nombreDeporte.notBlank}")
        @NotNull(message = "{createLigaRequest.nombreDeporte.notNull}")
        String nombreDeporte,


        String descripcion
) {
}
