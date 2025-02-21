package com.salesianos.dam.sportify.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD }) // La anotación se aplica a campos
@Retention(RetentionPolicy.RUNTIME) // La anotación estará disponible en tiempo de ejecución
@Constraint(validatedBy = UniqueEmailValidator.class) // Validador asociado
public @interface UniqueMail {
    String message() default "El email ya está registrado"; // Mensaje de error por defecto

    Class<?>[] groups() default {}; // Grupos de validación

    Class<? extends Payload>[] payload() default {}; // Payload para metadata adicional
}