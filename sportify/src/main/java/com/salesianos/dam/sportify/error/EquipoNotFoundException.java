package com.salesianos.dam.sportify.error;

import org.springframework.http.HttpStatus;

public class EquipoNotFoundException extends EntidadNoEncontradaException {
    public EquipoNotFoundException(String mensaje, HttpStatus status) {
        super(mensaje, status);
    }
}
