package com.crud.project.utils.validation;

import com.crud.project.utils.ValidadorDocumentos;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação para validação de CNPJ
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CNPJValidator.class)
public @interface ValidCNPJ {
    String message() default "CNPJ inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

/**
 * Validador de CNPJ
 */
class CNPJValidator implements ConstraintValidator<ValidCNPJ, String> {
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

