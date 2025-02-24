package com.salesianos.dam.sportify.error;

import org.springframework.http.HttpStatus;

public class LigaNotFoundException extends EntidadNoEncontradaException {

    public LigaNotFoundException(String mensaje, HttpStatus status) {
        super(mensaje, status);
    }
}
