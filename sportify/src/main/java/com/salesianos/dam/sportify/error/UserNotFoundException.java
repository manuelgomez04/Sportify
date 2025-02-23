package com.salesianos.dam.sportify.error;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends EntidadNoEncontradaException {
    public UserNotFoundException(String mensaje, HttpStatus status) {
        super(mensaje, status);
    }
}
