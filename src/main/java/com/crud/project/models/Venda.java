package com.crud.project.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Modelo para Histórico de Vendas
 *
 * Armazena informações de cada venda realizada
 */
@Entity
@Table(name = "vendas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "ID do produto é obrigatório")
    private Long produtoId;

    @NotBlank(message = "Nome do produto é obrigatório")
    private String nomeProduto;

    @NotNull(message = "Quantidade vendida é obrigatória")
    @Min(value = 1, message = "Quantidade deve ser maior que 0")
    private Integer quantidadeVendida;

    @NotNull(message = "Preço unitário é obrigatório")
    @DecimalMin(value = "0.0", message = "Preço deve ser maior que 0")
    private Double precoUnitario;

    @NotNull(message = "Preço total é obrigatório")
    private Double precoTotal;

    @NotBlank(message = "Mercado ID é obrigatório")
    private Long mercadoId;

    @NotBlank(message = "Nome do mercado é obrigatório")
    private String nomeMercado;

    @NotBlank(message = "Funcionário ID é obrigatório")
    private Long funcionarioId;

    @NotBlank(message = "Nome do funcionário é obrigatório")
    private String nomeFuncionario;

    @NotNull(message = "Data da venda é obrigatória")
    private LocalDateTime dataVenda;

    private String categoria;

    private String observacoes;

    private String numeroNota; // Número da nota fiscal

    private boolean ativo = true;

    private LocalDateTime dataCriacao = LocalDateTime.now();

    private LocalDateTime dataAtualizacao = LocalDateTime.now();

    // Manual getters and setters to avoid Lombok processing issues
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }

    public String getNomeProduto() { return nomeProduto; }
    public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; }

    public Integer getQuantidadeVendida() { return quantidadeVendida; }
    public void setQuantidadeVendida(Integer quantidadeVendida) { this.quantidadeVendida = quantidadeVendida; }

    public Double getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(Double precoUnitario) { this.precoUnitario = precoUnitario; }

    public Double getPrecoTotal() { return precoTotal; }
    public void setPrecoTotal(Double precoTotal) { this.precoTotal = precoTotal; }

    public Long getMercadoId() { return mercadoId; }
    public void setMercadoId(Long mercadoId) { this.mercadoId = mercadoId; }

    public String getNomeMercado() { return nomeMercado; }
    public void setNomeMercado(String nomeMercado) { this.nomeMercado = nomeMercado; }

    public Long getFuncionarioId() { return funcionarioId; }
    public void setFuncionarioId(Long funcionarioId) { this.funcionarioId = funcionarioId; }

    public String getNomeFuncionario() { return nomeFuncionario; }
    public void setNomeFuncionario(String nomeFuncionario) { this.nomeFuncionario = nomeFuncionario; }

    public LocalDateTime getDataVenda() { return dataVenda; }
    public void setDataVenda(LocalDateTime dataVenda) { this.dataVenda = dataVenda; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public String getNumeroNota() { return numeroNota; }
    public void setNumeroNota(String numeroNota) { this.numeroNota = numeroNota; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
}
