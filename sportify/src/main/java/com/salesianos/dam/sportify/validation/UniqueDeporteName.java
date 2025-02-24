package com.salesianos.dam.sportify.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueDeporteNameValidator.class)
@Documented
public @interface UniqueDeporteName {
    String message() default "El nombre del deporte no puede estar repetido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
