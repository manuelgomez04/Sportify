package com.salesianos.dam.sportify.user.dto;

import com.salesianos.dam.sportify.validation.FieldsValueMatch;
import com.salesianos.dam.sportify.validation.StrongPassword;
import jakarta.validation.constraints.NotBlank;

@FieldsValueMatch (field = "password", fieldMatch = "verifyPassword", message = "Las contraseñas no coinciden")


public record EditPasswordDto(
        @StrongPassword(message = "{createUserRequest.password.strongPassword}")
        @NotBlank(message = "{createUserRequest.password.notBlank}")
        String password,

        @NotBlank(message = "{createUserRequest.verifyPassword.notBlank}")
        String verifyPassword,
) {
}
