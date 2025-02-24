package com.salesianos.dam.sportify.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueMail {
    String message() default "El email ya está registrado";

    Class<?>[] groups() default {}; // Grupos de validación

    Class<? extends Payload>[] payload() default {}; // Payload para metadata adicional
}