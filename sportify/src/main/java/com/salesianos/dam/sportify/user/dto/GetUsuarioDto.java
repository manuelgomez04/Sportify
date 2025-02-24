package com.salesianos.dam.sportify.user.dto;

import com.salesianos.dam.sportify.equipo.dto.GetEquipoDto;
import com.salesianos.dam.sportify.user.model.User;
import com.salesianos.dam.sportify.validation.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import org.hibernate.Hibernate;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


public record GetUsuarioDto(
        String username,
        String password,
        String email,
        LocalDate fechaNacimiento,
        List<GetEquipoDto> equiposSeguidos,
        String nombre
) {
    public static GetUsuarioDto of(User user) {
        Hibernate.initialize(user.getEquiposSeguidos());
        return new GetUsuarioDto(
                user.getUsername(),
                user.getPassword(),
                user.getNombre(),
                user.getFechaNacimiento(),
                user.getEquiposSeguidos().stream().map(GetEquipoDto::of).toList(),
                user.getEmail()

        );
    }
}
