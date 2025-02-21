package com.salesianos.dam.sportify.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinAgeValidator.class)
public @interface MinAge {
    String message() default "Debes tener al menos {min} años";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    int min() default 16;
}
