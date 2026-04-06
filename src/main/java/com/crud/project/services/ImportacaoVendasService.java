package com.crud.project.services;

import com.crud.project.dto.ImportacaoResultadoDTO;
import com.crud.project.models.Venda;
import com.crud.project.repositories.VendaRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Serviço para importação de histórico de vendas
 *
 * Suporta:
 * - CSV
 * - EXCEL (.xlsx)
 */
@AllArgsConstructor
@Service
public class ImportacaoVendasService {

    private static final Logger log = LoggerFactory.getLogger(ImportacaoVendasService.class);
    private VendaRepository vendaRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER_2 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final int MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    /**
     * Importar vendas de arquivo CSV
     */
    public ImportacaoResultadoDTO importarCSV(MultipartFile file) {
        List<String> erros = new ArrayList<>();
        long startTime = System.currentTimeMillis();

        try {
            // Validar arquivo
            validarArquivo(file, "csv");

            // Ler CSV
            List<String[]> linhas = lerCSV(file);

            if (linhas.isEmpty()) {
                throw new RuntimeException("Arquivo CSV vazio");
            }

            // Processar linhas (pular cabeçalho)
            List<Venda> vendas = new ArrayList<>();
            for (int i = 1; i < linhas.size(); i++) {
                try {
                    String[] linha = linhas.get(i);
                    Venda venda = processarLinhaCSV(linha);
                    vendas.add(venda);
                } catch (Exception e) {
                    erros.add("Linha " + (i + 1) + ": " + e.getMessage());
                    log.error("Erro ao processar linha CSV: {}", e.getMessage());
                }
            }

            // Salvar vendas
            int sucessos = 0;
            if (!vendas.isEmpty()) {
                vendaRepository.saveAll(vendas);
                sucessos = vendas.size();
                log.info("✓ {} vendas importadas com sucesso", sucessos);
            }

            long endTime = System.currentTimeMillis();
            double tempoProcessamento = (double) (endTime - startTime) / 1000;

            return construirResultado(file.getOriginalFilename(), "CSV",
                    linhas.size() - 1, sucessos, erros.size(), erros, tempoProcessamento);

        } catch (Exception e) {
            log.error("Erro ao importar CSV: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao importar arquivo: " + e.getMessage());
        }
    }

    /**
     * Importar vendas de arquivo EXCEL (.xlsx)
     */
    public ImportacaoResultadoDTO importarExcel(MultipartFile file) {
        List<String> erros = new ArrayList<>();
        long startTime = System.currentTimeMillis();

        try {
            // Validar arquivo
            validarArquivo(file, "xlsx");

            // Ler Excel
            List<Venda> vendas = new ArrayList<>();
            int totalLinhas = 0;
            try (InputStream is = file.getInputStream();
                 Workbook workbook = new XSSFWorkbook(is)) {

                Sheet sheet = workbook.getSheetAt(0);
                totalLinhas = sheet.getLastRowNum();

                for (int i = 1; i <= totalLinhas; i++) {
                    try {
                        Row row = sheet.getRow(i);
                        if (row == null) continue;

                        Venda venda = processarLinhaExcel(row);
                        vendas.add(venda);
                    } catch (Exception e) {
                        erros.add("Linha " + (i + 1) + ": " + e.getMessage());
                        log.error("Erro ao processar linha Excel: {}", e.getMessage());
                    }
                }
            }

            // Salvar vendas
            int sucessos = 0;
            if (!vendas.isEmpty()) {
                vendaRepository.saveAll(vendas);
                sucessos = vendas.size();
                log.info("✓ {} vendas importadas com sucesso", sucessos);
            }

            long endTime = System.currentTimeMillis();
            double tempoProcessamento = (double) (endTime - startTime) / 1000;

            return construirResultado(file.getOriginalFilename(), "EXCEL",
                    totalLinhas, sucessos, erros.size(), erros, tempoProcessamento);

        } catch (Exception e) {
            log.error("Erro ao importar EXCEL: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao importar arquivo: " + e.getMessage());
        }
    }

    /**
     * Processar linha CSV
     * Formato esperado: produtoId, nomeProduto, quantidadeVendida, precoUnitario, mercadoId, nomeMercado, funcionarioId, nomeFuncionario, dataVenda, categoria, observacoes, numeroNota
     */

    private Venda processarLinhaCSV(String[] linha) {
        if (linha.length < 9) {
            throw new RuntimeException("Linha com número insuficiente de colunas. Esperado: 12, Recebido: " + linha.length);
        }

        Venda venda = new Venda();

        try {
            venda.setProdutoId(Long.parseLong(linha[0].trim()));
            venda.setNomeProduto(linha[1].trim());
            venda.setQuantidadeVendida(Integer.parseInt(linha[2].trim()));
            venda.setPrecoUnitario(Double.parseDouble(linha[3].trim()));
            venda.setMercadoId(Long.parseLong(linha[4].trim()));
            venda.setNomeMercado(linha[5].trim());
            venda.setFuncionarioId(Long.parseLong(linha[6].trim()));
            venda.setNomeFuncionario(linha[7].trim());
            venda.setDataVenda(parseData(linha[8].trim()));
            venda.setCategoria(linha.length > 9 ? linha[9].trim() : "");
            venda.setObservacoes(linha.length > 10 ? linha[10].trim() : "");
            venda.setNumeroNota(linha.length > 11 ? linha[11].trim() : "");

            // Calcular preço total
            venda.setPrecoTotal(venda.getQuantidadeVendida() * venda.getPrecoUnitario());

        } catch (NumberFormatException e) {
            throw new RuntimeException("Erro ao converter valores numéricos: " + e.getMessage());
        }

        return venda;
    }

    /**
     * Processar linha Excel
     */
    private Venda processarLinhaExcel(Row row) {
        Venda venda = new Venda();

        try {
            venda.setProdutoId(Long.parseLong(obterValorCelula(row, 0)));
            venda.setNomeProduto(obterValorCelula(row, 1));
            venda.setQuantidadeVendida((int) Double.parseDouble(obterValorCelula(row, 2)));
            venda.setPrecoUnitario(Double.parseDouble(obterValorCelula(row, 3)));
            venda.setMercadoId(Long.parseLong(obterValorCelula(row, 4)));
            venda.setNomeMercado(obterValorCelula(row, 5));
            venda.setFuncionarioId(Long.parseLong(obterValorCelula(row, 6)));
            venda.setNomeFuncionario(obterValorCelula(row, 7));
            venda.setDataVenda(parseData(obterValorCelula(row, 8)));
            venda.setCategoria(obterValorCelula(row, 9));
            venda.setObservacoes(obterValorCelula(row, 10));
            venda.setNumeroNota(obterValorCelula(row, 11));

            // Calcular preço total
            venda.setPrecoTotal(venda.getQuantidadeVendida() * venda.getPrecoUnitario());

        } catch (NumberFormatException e) {
            throw new RuntimeException("Erro ao converter valores numéricos: " + e.getMessage());
        }

        return venda;
    }

    /**
     * Ler arquivo CSV
     */
    private List<String[]> lerCSV(MultipartFile file) throws IOException, CsvException {
        try (InputStreamReader reader = new InputStreamReader(file.getInputStream());
             CSVReader csvReader = new CSVReader(reader)) {
            return csvReader.readAll();
        }
    }

    /**
     * Obter valor do cell do Excel
     */
    private String obterValorCelula(Row row, int coluna) {
        Cell cell = row.getCell(coluna);
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    /**
     * Parse data de múltiplos formatos
     */
    private LocalDateTime parseData(String data) {
        if (data == null || data.isEmpty()) {
            return LocalDateTime.now();
        }

        try {
            return LocalDateTime.parse(data, DATE_FORMATTER);
        } catch (Exception e1) {
            try {
                return LocalDateTime.parse(data, DATE_FORMATTER_2);
            } catch (Exception e2) {
                log.warn("Não foi possível fazer parse da data: {}. Usando data atual.", data);
                return LocalDateTime.now();
            }
        }
    }

    /**
     * Validar arquivo
     */
    private void validarArquivo(MultipartFile file, String tipoEsperado) {
        if (file.isEmpty()) {
            throw new RuntimeException("Arquivo vazio");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException("Arquivo muito grande. Máximo: 10MB");
        }

        String nomeArquivo = file.getOriginalFilename();
        if (!nomeArquivo.toLowerCase().endsWith("." + tipoEsperado)) {
            throw new RuntimeException("Tipo de arquivo inválido. Esperado: " + tipoEsperado);
        }
    }

    /**
     * Construir resposta de resultado
     */
    private ImportacaoResultadoDTO construirResultado(String nomeArquivo, String tipoArquivo,
            int totalRegistros, int sucessos, int erros, List<String> listaErros, double tempo) {
        ImportacaoResultadoDTO resultado = new ImportacaoResultadoDTO();
        resultado.setNomeArquivo(nomeArquivo);
        resultado.setTipoArquivo(tipoArquivo);
        resultado.setTotalRegistros(totalRegistros);
        resultado.setRegistrosComSucesso(sucessos);
        resultado.setRegistrosComErro(erros);
        resultado.setStatus("CONCLUIDO");
        resultado.setErros(listaErros);
        resultado.setTempoProcessamento(tempo);
        resultado.setMensagem("Importação concluída com sucesso");
        return resultado;
    }
}

