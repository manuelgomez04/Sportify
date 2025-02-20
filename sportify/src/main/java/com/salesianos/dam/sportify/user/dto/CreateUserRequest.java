package com.salesianos.dam.sportify.user.dto;

public record CreateUserRequest(
        String username, String email, String password, String verifyPassword, String phoneNumber

) {
}
