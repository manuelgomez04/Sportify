package com.salesianos.dam.sportify.user.dto;

import com.salesianos.dam.sportify.user.model.User;
import com.salesianos.dam.sportify.validation.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;


public record GetUsuarioDto(
        String username,
        String password,
        String email,
        LocalDate fechaNacimiento,
        String nombre
) {
    public static GetUsuarioDto of(User user) {
        return new GetUsuarioDto(
                user.getUsername(),
                user.getPassword(),
                user.getNombre(),
                user.getFechaNacimiento(),
                user.getEmail()

        );
    }
}
