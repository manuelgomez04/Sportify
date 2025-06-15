package com.salesianos.dam.sportify.deporte.dto;

import com.salesianos.dam.sportify.validation.UniqueDeporteName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateDeporteRequest(

        @UniqueDeporteName(message = "{createDeporteRequest.nombre.uniqueNombre}")
        @NotBlank(message = "{createDeporteRequest.nombre.notBlank}")

        String nombre,

        String descripcion,

        String imagen
) {
}