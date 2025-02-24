package com.salesianos.dam.sportify.validation;

import com.salesianos.dam.sportify.deporte.repo.DeporteRepository;
import com.salesianos.dam.sportify.liga.repo.LigaRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class UniqueLigaNameValidator implements ConstraintValidator<UniqueLigaName, String> {
    @Autowired
    private LigaRepository repo;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return StringUtils.hasText(s) && !repo.existsByNombreEqualsIgnoreCaseAndIgnoreWhitespace(s);
    }
}
