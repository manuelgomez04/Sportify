package com.salesianos.dam.sportify.user.error;

public class ActivationExpiredException extends RuntimeException{
    public ActivationExpiredException(String s) {
        super(s);
    }
}
