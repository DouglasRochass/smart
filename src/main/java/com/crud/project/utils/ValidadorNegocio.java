package com.crud.project.utils;

import com.crud.project.models.Cargos;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Classe utilitária para validações de negócio
 */
public class ValidadorNegocio {

    /**
     * Valida se o usuário tem cargo de Gerente ou Master
     * @param cargo Cargo do usuário
     * @throws ResponseStatusException se não for Gerente ou Master
     */
    public static void verificarSeGerenteOuMaster(Cargos cargo) {
        if (cargo != Cargos.GERENTE && cargo != Cargos.MASTER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, 
                "Apenas Gerentes ou Masters podem executar esta operação");
        }
    }

    /**
     * Valida se o usuário tem cargo de Master
     * @param cargo Cargo do usuário
     * @throws ResponseStatusException se não for Master
     */
    public static void verificarSeMaster(Cargos cargo) {
        if (cargo != Cargos.MASTER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, 
                "Apenas Masters podem executar esta operação");
        }
    }
}

