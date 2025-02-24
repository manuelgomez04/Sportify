package com.salesianos.dam.sportify.error;

import org.springframework.http.HttpStatus;

public class DeporteNotFoundException extends EntidadNoEncontradaException {
    public DeporteNotFoundException(String mensaje, HttpStatus status) {
        super(mensaje, status);
    }
}
