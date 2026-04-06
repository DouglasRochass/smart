package com.crud.project.controllers;

import com.crud.project.models.Cargos;
import com.crud.project.models.Mercado;
import com.crud.project.repositories.MercadoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller para gerenciar Mercados
 *
 * Endpoints:
 * - GET /api/mercados → Listar todos os mercados
 * - GET /api/mercados/{id} → Buscar mercado por ID
 * - GET /api/mercados/nome/{nome} → Buscar por nome
 * - POST /api/mercados → Criar novo mercado
 * - PUT /api/mercados/{id} → Atualizar mercado
 * - DELETE /api/mercados/{id} → Deletar mercado
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/mercados")
public class MercadoController {

    @Autowired
    private MercadoRepository mercadoRepository;

    /**
     * Listar todos os mercados
     * GET /api/mercados
     * 
     * Restrição: Funcionários apenas veem seu próprio mercado
     * Headers:
     * - gerente-cargo: Cargo do usuário (FUNCIONARIO, GERENTE, MASTER)
     * - usuario-mercado-id: Mercado do usuário (obrigatório para FUNCIONARIO)
     */
    @GetMapping
    public ResponseEntity<?> listAll(
            @RequestHeader(value = "gerente-cargo", required = false) String cargoHeader,
            @RequestHeader(value = "usuario-mercado-id", required = false) Long usuarioMercadoId) {
        try {
            if (cargoHeader != null) {
                Cargos cargo = Cargos.valueOf(cargoHeader);
                
                // Funcionários normais veem apenas seu mercado
                if (cargo == Cargos.FUNCIONARIO) {
                    if (usuarioMercadoId == null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Funcionários devem fornecer usuario-mercado-id");
                    }
                    Optional<Mercado> mercado = mercadoRepository.findById(usuarioMercadoId);
                    if (mercado.isPresent()) {
                        return ResponseEntity.ok(java.util.Collections.singletonList(mercado.get()));
                    }
                    return ResponseEntity.notFound().build();
                }
            }
            
            // Gerentes/Masters e sem autenticação: veem todos
            List<Mercado> mercados = mercadoRepository.findAll();
            return ResponseEntity.ok(mercados);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Cargo inválido fornecido");
        }
    }

    /**
     * Buscar mercado por ID
     * GET /api/mercados/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Mercado> getById(@PathVariable Long id) {
        Optional<Mercado> mercado = mercadoRepository.findById(id);
        return mercado
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Buscar mercado por nome
     * GET /api/mercados/nome/{nome}
     */
    @GetMapping("/nome/{nome}")
    public ResponseEntity<Mercado> getByNome(@PathVariable String nome) {
        Optional<Mercado> mercado = mercadoRepository.findByNome(nome);
        return mercado
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Criar novo mercado
     * POST /api/mercados
     */
    @PostMapping
    public ResponseEntity<Mercado> create(@RequestBody Mercado mercado) {
        Mercado saved = mercadoRepository.save(mercado);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Atualizar mercado
     * PUT /api/mercados/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Mercado> update(@PathVariable Long id, @RequestBody Mercado mercadoDetails) {
        Optional<Mercado> mercado = mercadoRepository.findById(id);
        if (mercado.isPresent()) {
            Mercado m = mercado.get();
            if (mercadoDetails.getNome() != null)
                m.setNome(mercadoDetails.getNome());
            if (mercadoDetails.getEndereco() != null)
                m.setEndereco(mercadoDetails.getEndereco());
            if (mercadoDetails.getTelefone() != null)
                m.setTelefone(mercadoDetails.getTelefone());
            if (mercadoDetails.getCidade() != null)
                m.setCidade(mercadoDetails.getCidade());
            if (mercadoDetails.getEstado() != null)
                m.setEstado(mercadoDetails.getEstado());

            Mercado updated = mercadoRepository.save(m);
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Deletar mercado
     * DELETE /api/mercados/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (mercadoRepository.existsById(id)) {
            mercadoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

