package com.salesianos.dam.sportify.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class EntidadNoEncontradaException extends RuntimeException {

    private final HttpStatus status;

    public EntidadNoEncontradaException(String mensaje, HttpStatus status) {
        super(mensaje);
        this.status = status;
    }

}
