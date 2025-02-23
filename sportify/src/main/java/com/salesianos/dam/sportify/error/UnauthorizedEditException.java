package com.salesianos.dam.sportify.error;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


public class UnauthorizedEditException extends RuntimeException {
    private final HttpStatus status;

    public UnauthorizedEditException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
