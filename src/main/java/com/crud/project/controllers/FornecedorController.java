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
    public ResponseEntity<Fornecedor> create(@RequestBody Fornecedor fornecedor) {
        Fornecedor saved = fornecedorRepository.save(fornecedor);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
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

