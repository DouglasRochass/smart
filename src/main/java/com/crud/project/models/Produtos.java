package com.crud.project.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "produtos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produtos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descricao;
    private Double preco;
    private Integer quantidade;  // Mudado de int para Integer
    private Long fornecedorId;
    private String categoria;

    // Getters explícitos
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public Double getPreco() { return preco; }
    public Integer getQuantidade() { return quantidade; }
    public Long getFornecedorId() { return fornecedorId; }
    public String getCategoria() { return categoria; }

    // Setters explícitos
    public void setId(Long id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setPreco(Double preco) { this.preco = preco; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    public void setFornecedorId(Long fornecedorId) { this.fornecedorId = fornecedorId; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
}
