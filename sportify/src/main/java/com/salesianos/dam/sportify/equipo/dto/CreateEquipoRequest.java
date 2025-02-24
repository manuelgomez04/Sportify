package com.salesianos.dam.sportify.equipo.dto;

import com.salesianos.dam.sportify.validation.UniqueEquipoName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record CreateEquipoRequest(

        @UniqueEquipoName(message = "{createEquipoRequest.nombre.uniqueEquipoName}")
        @NotBlank(message = "{createEquipoRequest.nombre.notBlank}")
        @NotNull(message = "{createEquipoRequest.nombre.notNull}")
        String nombre,

        @NotBlank(message = "{createEquipoRequest.nombreLiga.notBlank}")
        @NotNull(message = "{createEquipoRequest.nombreLiga.notNull}")
        String nombreLiga,

        @NotBlank(message = "{createEquipoRequest.ciudad.notBlank}")
        @NotNull(message = "{createEquipoRequest.ciudad.notNull}")
        String ciudad,

        @NotNull(message = "{createEquipoRequest.pais.notNull}")
        String pais,



        @NotNull(message = "{createEquipoRequest.fechaCreacion.notNull}")
        @PastOrPresent(message = "{createEquipoRequest.fechaCreacion.pastOrPresent}")
        LocalDate fechaCreacion
) {
}
