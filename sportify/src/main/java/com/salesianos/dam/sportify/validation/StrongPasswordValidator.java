package com.salesianos.dam.sportify.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.*;

import java.util.Arrays;
import org.passay.MessageResolver;
import org.passay.PropertiesMessageResolver;
import java.util.Properties;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }

        // Mensajes personalizados en español
        Properties props = new Properties();
        props.setProperty("TOO_SHORT", "La contraseña debe tener al menos 8 caracteres.");
        props.setProperty("TOO_LONG", "La contraseña no puede tener más de 30 caracteres.");
        props.setProperty("INSUFFICIENT_UPPERCASE", "La contraseña debe tener al menos una letra mayúscula.");
        props.setProperty("INSUFFICIENT_LOWERCASE", "La contraseña debe tener al menos una letra minúscula.");
        props.setProperty("INSUFFICIENT_DIGIT", "La contraseña debe tener al menos un dígito.");
        props.setProperty("INSUFFICIENT_SPECIAL", "La contraseña debe tener al menos un carácter especial.");
        props.setProperty("ILLEGAL_WHITESPACE", "La contraseña no puede contener espacios en blanco.");

        MessageResolver resolver = new PropertiesMessageResolver(props);

        PasswordValidator validator = new PasswordValidator(
                resolver,
                Arrays.asList(
                        new LengthRule(8, 30),
                        new CharacterRule(EnglishCharacterData.UpperCase, 1),
                        new CharacterRule(EnglishCharacterData.LowerCase, 1),
                        new CharacterRule(EnglishCharacterData.Digit, 1),
                        new CharacterRule(EnglishCharacterData.Special, 1),
                        new WhitespaceRule()
                )
        );

        RuleResult result = validator.validate(new PasswordData(password));

        if (result.isValid()) {
            return true;
        } else {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(String.join(" ", validator.getMessages(result)))
                    .addConstraintViolation();
            return false;
        }
    }
}
