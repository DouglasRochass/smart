package com.crud.project.dto;

import com.crud.project.models.Cargos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para Funcionário sem exposição de dados sensíveis (CPF)
 * Usado quando funcionários consultam dados de colegas
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioDTO {
    private Long id;
    private String nomeColaborador;
    private String email;
    private Cargos cargos;
    private Long mercadoId;
    private boolean ativo;
    private int idade;
    
    // CPF não é incluído neste DTO
    
    // Getters
    public Long getId() { return id; }
    public String getNomeColaborador() { return nomeColaborador; }
    public String getEmail() { return email; }
    public Cargos getCargos() { return cargos; }
    public Long getMercadoId() { return mercadoId; }
    public boolean isAtivo() { return ativo; }
    public int getIdade() { return idade; }
    
    // Setters
    public void setId(Long id) { this.id = id; }
    public void setNomeColaborador(String nomeColaborador) { this.nomeColaborador = nomeColaborador; }
    public void setEmail(String email) { this.email = email; }
    public void setCargos(Cargos cargos) { this.cargos = cargos; }
    public void setMercadoId(Long mercadoId) { this.mercadoId = mercadoId; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public void setIdade(int idade) { this.idade = idade; }
}


