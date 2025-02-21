package com.salesianos.dam.sportify.validation;

import com.salesianos.dam.sportify.user.repo.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueEmailValidator implements ConstraintValidator<UniqueMail, String> {

    @Autowired
    private UserRepository userRepository;



    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            return true;
        }


        return !userRepository.existsByEmail(email);
    }
}
