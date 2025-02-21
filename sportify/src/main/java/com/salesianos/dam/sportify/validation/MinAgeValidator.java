package com.salesianos.dam.sportify.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;


public class MinAgeValidator implements ConstraintValidator<MinAge, LocalDate> {

    private int minAge;

    @Override
    public void initialize(MinAge constraintAnnotation) {
        this.minAge = constraintAnnotation.min(); // Obtiene el valor mínimo de edad desde la anotación
    }

    @Override
    public boolean isValid(LocalDate fechaNacimiento, ConstraintValidatorContext context) {
        if (fechaNacimiento == null) {
            return true; // O false, dependiendo de si el campo es obligatorio
        }

        // Calcula la edad usando LocalDate y Period
        LocalDate hoy = LocalDate.now();
        Period periodo = Period.between(fechaNacimiento, hoy);
        int edad = periodo.getYears();

        // Verifica si la edad es mayor o igual a la edad mínima requerida
        return edad >= minAge;
    }
}
