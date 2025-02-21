package com.salesianos.dam.sportify.user.dto;

import com.salesianos.dam.sportify.validation.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.util.Date;

@FieldsValueMatch.List({
        @FieldsValueMatch(field = "password", fieldMatch = "verifyPassword", message = "Las contraseñas no coinciden"),
        @FieldsValueMatch(field = "email", fieldMatch = "verifyEmail", message = "Los emails no coinciden")
})
public record CreateUserRequest(


        @NotBlank(message = "{createUserRequest.username.notBlank}")
        @UniqueUsername(message = "{createUserRequest.username.uniqueUsername}")
        String username,

        @StrongPassword(message = "{createUserRequest.password.strongPassword}")
        @NotBlank(message = "{createUserRequest.password.notBlank}")
        String password,

        @NotBlank(message = "{createUserRequest.verifyPassword.notBlank}")
        String verifyPassword,

        @UniqueMail(message = "{createUserRequest.email.uniqueEmail}")
        @NotBlank(message = "{createUserRequest.email.notBlank}")
        @Email(message = "{createUserRequest.email.invalid}")
        String email,


        @NotBlank(message = "{createUserRequest.verifyEmail.notBlank}")
        @Email(message = "{createUserRequest.verifyEmail.invalid}")
        String verifyEmail,


        @Past(message = "{createUserRequest.fechaNacimiento.past}")
        @MinAge(min = 16, message = "{createUserRequest.fechaNacimiento.minAge}")
        LocalDate fechaNacimiento,

        @NotBlank(message = "{createUserRequest.nombre.notBlank}")
        String nombre
) {
}
