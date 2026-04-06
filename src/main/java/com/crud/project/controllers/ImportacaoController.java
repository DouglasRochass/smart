package com.crud.project.controllers;

import com.crud.project.dto.ImportacaoResultadoDTO;
import com.crud.project.services.ImportacaoVendasService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller para Importação de Vendas
 *
 * Endpoints:
 * - POST /api/importacao/csv → Importar CSV
 * - POST /api/importacao/excel → Importar EXCEL
 */
@RestController
@RequestMapping("/api/importacao")
public class ImportacaoController {

    private static final Logger log = LoggerFactory.getLogger(ImportacaoController.class);

    @Autowired
    private ImportacaoVendasService importacaoVendasService;

    /**
     * Importar vendas de arquivo CSV
     * POST /api/importacao/csv
     *
     * Headers:
     * - Content-Type: multipart/form-data
     *
     * Form Data:
     * - file: arquivo CSV
     *
     * Formato esperado do CSV:
     * produtoId,nomeProduto,quantidadeVendida,precoUnitario,mercadoId,nomeMercado,funcionarioId,nomeFuncionario,dataVenda,categoria,observacoes,numeroNota
     */
    @PostMapping(value = "/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImportacaoResultadoDTO> importarCSV(
            @RequestParam("file") MultipartFile file) {
        try {
            log.info("Iniciando importação de CSV: {}", file.getOriginalFilename());
            ImportacaoResultadoDTO resultado = importacaoVendasService.importarCSV(file);
            log.info("Importação concluída com sucesso: {} registros", resultado.getRegistrosComSucesso());
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            log.error("Erro ao importar CSV: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Importar vendas de arquivo EXCEL
     * POST /api/importacao/excel
     *
     * Headers:
     * - Content-Type: multipart/form-data
     *
     * Form Data:
     * - file: arquivo EXCEL (.xlsx)
     *
     * Formato esperado do EXCEL:
     * produtoId, nomeProduto, quantidadeVendida, precoUnitario, mercadoId, nomeMercado, funcionarioId, nomeFuncionario, dataVenda, categoria, observacoes, numeroNota
     */
    @PostMapping(value = "/excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImportacaoResultadoDTO> importarExcel(
            @RequestParam("file") MultipartFile file) {
        try {
            log.info("Iniciando importação de EXCEL: {}", file.getOriginalFilename());
            ImportacaoResultadoDTO resultado = importacaoVendasService.importarExcel(file);
            log.info("Importação concluída com sucesso: {} registros", resultado.getRegistrosComSucesso());
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            log.error("Erro ao importar EXCEL: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}

