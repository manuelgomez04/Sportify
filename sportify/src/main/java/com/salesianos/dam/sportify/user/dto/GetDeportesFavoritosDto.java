package com.salesianos.dam.sportify.user.dto;

import com.salesianos.dam.sportify.deporte.dto.GetNombreDeporteDto;
import com.salesianos.dam.sportify.deporte.model.Deporte;
import com.salesianos.dam.sportify.liga.dto.GetNombreLiga;
import com.salesianos.dam.sportify.user.model.User;
import org.springframework.data.domain.Page;

public record GetDeportesFavoritosDto(
        String username,
        Page<GetNombreDeporteDto> deportesFavoritos
) {
    public static GetDeportesFavoritosDto of(User u, Page<Deporte> deportes) {
        return new GetDeportesFavoritosDto(u.getUsername(), deportes.map(GetNombreDeporteDto::of));
    }
}
