package com.salesianos.dam.sportify.user.dto;

import com.salesianos.dam.sportify.user.model.User;

public record GetUserNoAsociacionesDto(
        String username
) {
    public static GetUserNoAsociacionesDto of(User user) {
        return new GetUserNoAsociacionesDto(
                user.getUsername()
        );
    }
}
