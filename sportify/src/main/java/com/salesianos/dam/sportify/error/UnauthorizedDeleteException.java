package com.salesianos.dam.sportify.error;

import org.springframework.http.HttpStatus;

public class UnauthorizedDeleteException extends RuntimeException {
    private final HttpStatus status;

    public UnauthorizedDeleteException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
