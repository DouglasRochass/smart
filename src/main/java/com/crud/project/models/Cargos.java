package com.crud.project.models;


public enum Cargos {

    FUNCIONARIO("Funcionário"),
    GERENTE("Gerente"),
    MASTER("Master");

    private final String descricao;

    Cargos(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
