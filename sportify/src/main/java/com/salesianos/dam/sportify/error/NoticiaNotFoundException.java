package com.salesianos.dam.sportify.error;

import org.springframework.http.HttpStatus;

public class NoticiaNotFoundException extends EntidadNoEncontradaException {
    public NoticiaNotFoundException(String mensaje, HttpStatus status) {
        super(mensaje, status);
    }
}
