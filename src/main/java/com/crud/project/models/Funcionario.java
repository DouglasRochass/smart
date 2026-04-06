package com.crud.project.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.crud.project.utils.validation.ValidCPF;

@Entity
@Table(name = "funcionarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String nomeColaborador;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    private String senha;

    @NotNull(message = "Cargo é obrigatório")
    private Cargos cargos;

    private Long mercadoId;

    private boolean ativo = true;

    @NotBlank(message = "CPF é obrigatório")
    @ValidCPF(message = "CPF deve ser válido")
    private String cpf;

    @Min(value = 0, message = "Idade não pode ser negativa")
    @Max(value = 150, message = "Idade deve ser realista")
    private int idade;

    // Getters explícitos
    public Long getId() { return id; }
    public String getNomeColaborador() { return nomeColaborador; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
    public Cargos getCargos() { return cargos; }
    public Long getMercadoId() { return mercadoId; }
    public boolean isAtivo() { return ativo; }
    public String getCpf() { return cpf; }
    public int getIdade() { return idade; }

    // Setters explícitos
    public void setId(Long id) { this.id = id; }
    public void setNomeColaborador(String nomeColaborador) { this.nomeColaborador = nomeColaborador; }
    public void setEmail(String email) { this.email = email; }
    public void setSenha(String senha) { this.senha = senha; }
    public void setCargos(Cargos cargos) { this.cargos = cargos; }
    public void setMercadoId(Long mercadoId) { this.mercadoId = mercadoId; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public void setIdade(int idade) { this.idade = idade; }
}
