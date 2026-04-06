package com.crud.project.utils;

/**
 * Classe utilitária para validação de documentos (CPF e CNPJ)
 */
public class ValidadorDocumentos {

    /**
     * Valida um CPF
     * @param cpf CPF a ser validado (sem máscara)
     * @return true se o CPF é válido, false caso contrário
     */
    public static boolean isValidCPF(String cpf) {
        if (cpf == null || cpf.isEmpty()) {
            return false;
        }

        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("\\D", "");

        // Verifica se tem 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }

        // Verifica se todos os dígitos são iguais (inválido)
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        // Calcula o primeiro dígito verificador
        int sum = 0;
        int remainder;
        for (int i = 1; i <= 9; i++) {
            sum += Integer.parseInt(cpf.substring(i - 1, i)) * (11 - i);
        }
        remainder = (sum * 10) % 11;
        if (remainder == 10 || remainder == 11) {
            remainder = 0;
        }
        if (remainder != Integer.parseInt(cpf.substring(9, 10))) {
            return false;
        }

        // Calcula o segundo dígito verificador
        sum = 0;
        for (int i = 1; i <= 10; i++) {
            sum += Integer.parseInt(cpf.substring(i - 1, i)) * (12 - i);
        }
        remainder = (sum * 10) % 11;
        if (remainder == 10 || remainder == 11) {
            remainder = 0;
        }
        if (remainder != Integer.parseInt(cpf.substring(10, 11))) {
            return false;
        }

        return true;
    }

    /**
     * Valida um CNPJ
     * @param cnpj CNPJ a ser validado (sem máscara)
     * @return true se o CNPJ é válido, false caso contrário
     */
    public static boolean isValidCNPJ(String cnpj) {
        if (cnpj == null || cnpj.isEmpty()) {
            return false;
        }

        // Remove caracteres não numéricos
        cnpj = cnpj.replaceAll("\\D", "");

        // Verifica se tem 14 dígitos
        if (cnpj.length() != 14) {
            return false;
        }

        // Verifica se todos os dígitos são iguais (inválido)
        if (cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        // Calcula o primeiro dígito verificador
        int size = cnpj.length() - 2;
        int sum = 0;
        int pos = size - 7;
        for (int i = size; i >= 0; i--) {
            sum += Integer.parseInt(cnpj.substring(i, i + 1)) * pos--;
            if (pos < 2) {
                pos = 9;
            }
        }
        int result = sum % 11 < 2 ? 0 : 11 - sum % 11;
        if (result != Integer.parseInt(cnpj.substring(size, size + 1))) {
            return false;
        }

        // Calcula o segundo dígito verificador
        size = cnpj.length() - 1;
        sum = 0;
        pos = size - 7;
        for (int i = size; i >= 0; i--) {
            sum += Integer.parseInt(cnpj.substring(i, i + 1)) * pos--;
            if (pos < 2) {
                pos = 9;
            }
        }
        result = sum % 11 < 2 ? 0 : 11 - sum % 11;
        if (result != Integer.parseInt(cnpj.substring(size, size + 1))) {
            return false;
        }

        return true;
    }
}

