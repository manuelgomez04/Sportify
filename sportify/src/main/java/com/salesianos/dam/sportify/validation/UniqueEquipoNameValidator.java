package com.salesianos.dam.sportify.validation;

import com.salesianos.dam.sportify.equipo.repo.EquipoRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class UniqueEquipoNameValidator implements ConstraintValidator<UniqueEquipoName, String> {
    @Autowired
    private EquipoRepository repo;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return StringUtils.hasText(s) && !repo.existsByNombreEqualsIgnoreCaseAndIgnoreWhitespace(s);
    }
}
