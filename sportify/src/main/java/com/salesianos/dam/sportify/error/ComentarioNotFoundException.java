package com.salesianos.dam.sportify.error;

import org.springframework.http.HttpStatus;

public class ComentarioNotFoundException extends EntidadNoEncontradaException{


    public ComentarioNotFoundException(String mensaje, HttpStatus status) {
        super(mensaje, status);
    }
}
