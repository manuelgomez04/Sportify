package com.salesianos.dam.sportify.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEquipoNameValidator.class)
@Documented
public @interface UniqueEquipoName {
    String message() default "Ya existe equipo con ese nombre";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
