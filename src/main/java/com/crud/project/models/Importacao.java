package com.crud.project.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "importacoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Importacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeArquivo;

    private String tipoArquivo; // CSV, EXCEL, JSON

    private Long usuarioId;

    private String nomeUsuario;

    private LocalDateTime dataImportacao = LocalDateTime.now();

    private Integer totalRegistros = 0;

    private Integer registrosComSucesso = 0;

    private Integer registrosComErro = 0;

    private String status; // PENDENTE, PROCESSANDO, CONCLUIDO, ERRO

    private String mensagemErro;

    private Double tempoProcessamento; // em segundos

    private String descricao;

    private boolean ativo = true;
}
