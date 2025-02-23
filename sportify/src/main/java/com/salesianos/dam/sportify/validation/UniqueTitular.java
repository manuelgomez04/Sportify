package com.salesianos.dam.sportify.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueTitularValidator.class)
@Documented

public @interface UniqueTitular {

    String message() default "Ya existe una noticia con este titular";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
