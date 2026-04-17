package com.crud.project.utils.validation;

import com.crud.project.utils.ValidadorDocumentos;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validador de CPF
 */
public class CPFValidator implements ConstraintValidator<ValidCPF, String> {
    
    public CPFValidator() {
        // Construtor vazio necessário para Hibernate Validator
    }
    
    @Override
    public void initialize(ValidCPF constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // Permitir null/vazio, use @NotNull/@NotBlank para validar isso
        }
        return ValidadorDocumentos.isValidCPF(value);
    }
}

