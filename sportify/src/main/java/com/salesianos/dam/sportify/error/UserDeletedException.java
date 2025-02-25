package com.salesianos.dam.sportify.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserDeletedException extends RuntimeException{
    private final HttpStatus status;

    public UserDeletedException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
