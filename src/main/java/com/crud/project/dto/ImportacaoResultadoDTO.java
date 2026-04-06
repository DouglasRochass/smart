package com.crud.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO para resposta de importação de vendas
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImportacaoResultadoDTO {

    private String nomeArquivo;

    private String tipoArquivo; // CSV ou EXCEL

    private Integer totalRegistros;

    private Integer registrosComSucesso;

    private Integer registrosComErro;

    private String status; // CONCLUIDO ou ERRO

    private List<String> erros;

    private Double tempoProcessamento; // em segundos

    private String mensagem;

    // Getters explícitos
    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public String getTipoArquivo() {
        return tipoArquivo;
    }

    public Integer getTotalRegistros() {
        return totalRegistros;
    }

    public Integer getRegistrosComSucesso() {
        return registrosComSucesso;
    }

    public Integer getRegistrosComErro() {
        return registrosComErro;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getErros() {
        return erros;
    }

    public Double getTempoProcessamento() {
        return tempoProcessamento;
    }

    public String getMensagem() {
        return mensagem;
    }

    // Setters explícitos
    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public void setTipoArquivo(String tipoArquivo) {
        this.tipoArquivo = tipoArquivo;
    }

    public void setTotalRegistros(Integer totalRegistros) {
        this.totalRegistros = totalRegistros;
    }

    public void setRegistrosComSucesso(Integer registrosComSucesso) {
        this.registrosComSucesso = registrosComSucesso;
    }

    public void setRegistrosComErro(Integer registrosComErro) {
        this.registrosComErro = registrosComErro;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setErros(List<String> erros) {
        this.erros = erros;
    }

    public void setTempoProcessamento(Double tempoProcessamento) {
        this.tempoProcessamento = tempoProcessamento;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
