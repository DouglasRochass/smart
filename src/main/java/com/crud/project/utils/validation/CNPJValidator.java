package com.crud.project.utils.validation;

import com.crud.project.utils.ValidadorDocumentos;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validador de CNPJ
 */
public class CNPJValidator implements ConstraintValidator<ValidCNPJ, String> {
    
    public CNPJValidator() {
        // Construtor vazio necessário para Hibernate Validator
    }
    
    @Override
    public void initialize(ValidCNPJ constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // Permitir null/vazio, use @NotNull/@NotBlank para validar isso
        }
        return ValidadorDocumentos.isValidCNPJ(value);
    }
}

