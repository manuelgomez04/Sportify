package com.salesianos.dam.sportify.validation;

import com.salesianos.dam.sportify.deporte.repo.DeporteRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class UniqueDeporteNameValidator implements ConstraintValidator<UniqueDeporteName, String> {
    @Autowired
    private DeporteRepository repo;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return StringUtils.hasText(s) && !repo.existsByNombre(s);
    }
}
