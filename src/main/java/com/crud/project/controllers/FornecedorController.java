package com.crud.project.controllers;

import com.crud.project.models.Fornecedor;
import com.crud.project.repositories.FornecedorRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller para gerenciar Fornecedores
 *
 * Endpoints:
 * - GET /api/fornecedores → Listar todos os fornecedores
 * - GET /api/fornecedores/{id} → Buscar fornecedor por ID
 * - GET /api/fornecedores/nome/{nome} → Buscar por nome
 * - GET /api/fornecedores/email/{email} → Buscar por email
 * - POST /api/fornecedores → Criar novo fornecedor
 * - PUT /api/fornecedores/{id} → Atualizar fornecedor
 * - DELETE /api/fornecedores/{id} → Deletar fornecedor
 */

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/fornecedores")
public class FornecedorController {

    @Autowired
    private FornecedorRepository fornecedorRepository;

    /**
     * Listar todos os fornecedores
     * GET /api/fornecedores
     */
    @GetMapping
    public ResponseEntity<List<Fornecedor>> listAll() {
        List<Fornecedor> fornecedores = fornecedorRepository.findAll();
        return ResponseEntity.ok(fornecedores);
    }

    /**
     * Endpoint de teste para validar CNPJ
     * POST /api/fornecedores/validar-cnpj
     */
    @PostMapping("/validar-cnpj")
    public ResponseEntity<?> validarCNPJ(@RequestBody java.util.Map<String, String> payload) {
        try {
            String cnpj = payload.get("cnpj");
            if (cnpj == null || cnpj.isEmpty()) {
                return ResponseEntity.badRequest().body("CNPJ não fornecido");
            }
            
            String cnpjLimpo = cnpj.replaceAll("\\D", "");
            boolean valido = com.crud.project.utils.ValidadorDocumentos.isValidCNPJ(cnpjLimpo);
            
            return ResponseEntity.ok(java.util.Map.of(
                "cnpj", cnpj,
                "cnpjLimpo", cnpjLimpo,
                "valido", valido,
                "tamanho", cnpjLimpo.length()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro: " + e.getMessage());
        }
    }

    /**
     * Buscar fornecedor por ID
     * GET /api/fornecedores/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Fornecedor> getById(@PathVariable Long id) {
        Optional<Fornecedor> fornecedor = fornecedorRepository.findById(id);
        return fornecedor
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Buscar fornecedor por nome
     * GET /api/fornecedores/nome/{nome}
     */
    @GetMapping("/nome/{nome}")
    public ResponseEntity<Fornecedor> getByNome(@PathVariable String nome) {
        Optional<Fornecedor> fornecedor = fornecedorRepository.findByNome(nome);
        return fornecedor
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Buscar fornecedor por email
     * GET /api/fornecedores/email/{email}
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Fornecedor> getByEmail(@PathVariable String email) {
        Optional<Fornecedor> fornecedor = fornecedorRepository.findByEmail(email);
        return fornecedor
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Criar novo fornecedor
     * POST /api/fornecedores
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Fornecedor fornecedor) {
        try {
            log.info("Recebendo requisição para criar fornecedor: {}", fornecedor);
            
            // Validar campos obrigatórios
            if (fornecedor.getNome() == null || fornecedor.getNome().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Nome é obrigatório");
            }
            
            if (fornecedor.getEmail() == null || fornecedor.getEmail().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Email é obrigatório");
            }
            
            if (fornecedor.getTelefone() == null || fornecedor.getTelefone().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Telefone é obrigatório");
            }
            
            if (fornecedor.getEndereco() == null || fornecedor.getEndereco().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Endereço é obrigatório");
            }
            
            // Validar e limpar CNPJ
            if (fornecedor.getCnpj() == null || fornecedor.getCnpj().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("CNPJ é obrigatório");
            }
            
            String cnpjLimpo = fornecedor.getCnpj().replaceAll("\\D", "");
            log.info("CNPJ limpo: {}", cnpjLimpo);
            
            if (cnpjLimpo.length() != 14) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("CNPJ deve ter 14 dígitos. Recebido: " + cnpjLimpo.length() + " dígitos");
            }
            
            // Validar CNPJ
            if (!com.crud.project.utils.ValidadorDocumentos.isValidCNPJ(cnpjLimpo)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("CNPJ inválido: " + cnpjLimpo);
            }
            
            fornecedor.setCnpj(cnpjLimpo);
            
            log.info("Salvando fornecedor: {}", fornecedor);
            Fornecedor saved = fornecedorRepository.save(fornecedor);
            log.info("Fornecedor salvo com sucesso: {}", saved);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            log.error("Erro de integridade de dados ao criar fornecedor", e);
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Erro: Fornecedor com este email ou CNPJ já existe");
        } catch (jakarta.validation.ConstraintViolationException e) {
            log.error("Erro de validação ao criar fornecedor", e);
            String erros = e.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Erro de validação");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Erro de validação: " + erros);
        } catch (Exception e) {
            log.error("Erro inesperado ao criar fornecedor", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao criar fornecedor: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }

    /**
     * Atualizar fornecedor
     * PUT /api/fornecedores/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Fornecedor> update(@PathVariable Long id, @RequestBody Fornecedor fornecedorDetails) {
        Optional<Fornecedor> fornecedor = fornecedorRepository.findById(id);
        if (fornecedor.isPresent()) {
            Fornecedor f = fornecedor.get();
            if (fornecedorDetails.getNome() != null)
                f.setNome(fornecedorDetails.getNome());
            if (fornecedorDetails.getTelefone() != null)
                f.setTelefone(fornecedorDetails.getTelefone());
            if (fornecedorDetails.getEmail() != null)
                f.setEmail(fornecedorDetails.getEmail());
            if (fornecedorDetails.getEndereco() != null)
                f.setEndereco(fornecedorDetails.getEndereco());

            Fornecedor updated = fornecedorRepository.save(f);
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Deletar fornecedor
     * DELETE /api/fornecedores/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (fornecedorRepository.existsById(id)) {
            fornecedorRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

