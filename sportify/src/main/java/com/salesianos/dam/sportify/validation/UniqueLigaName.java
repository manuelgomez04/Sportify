package com.salesianos.dam.sportify.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueLigaNameValidator.class)
@Documented
public @interface UniqueLigaName {
    String message() default "Ya existe liga con ese nombre";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
