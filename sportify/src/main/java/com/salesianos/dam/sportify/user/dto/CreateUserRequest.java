package com.salesianos.dam.sportify.user.dto;

import java.util.Date;

public record CreateUserRequest(
        String username,
        String email,
        String password,
        String verifyPassword,
        String phoneNumber,
        Date fechaNacimiento,
        String nombre
) {
}
