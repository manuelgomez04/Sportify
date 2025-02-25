package com.salesianos.dam.sportify.user.dto;

import com.salesianos.dam.sportify.liga.dto.FollowLigaRequest;
import com.salesianos.dam.sportify.liga.dto.GetNombreLiga;
import com.salesianos.dam.sportify.liga.model.Liga;
import com.salesianos.dam.sportify.user.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

public record GetLigasFavoritasDto(
        String username,
        Page<GetNombreLiga> ligasFavoritas
) {

    public static GetLigasFavoritasDto of(User u, Page<Liga> ligas) {
        return new GetLigasFavoritasDto(u.getUsername(), ligas.map(GetNombreLiga::of));
    }
}
