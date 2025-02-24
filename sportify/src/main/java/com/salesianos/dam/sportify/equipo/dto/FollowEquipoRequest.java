package com.salesianos.dam.sportify.equipo.dto;

import com.salesianos.dam.sportify.validation.UniqueEquipoName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FollowEquipoRequest(
        @NotNull
        @NotBlank
        String nombreEquipo
) {
}
