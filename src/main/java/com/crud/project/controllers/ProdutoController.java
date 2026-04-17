package com.crud.project.controllers;

import com.crud.project.models.Cargos;
import com.crud.project.models.Produtos;
import com.crud.project.repositories.ProdutosRepository;
import com.crud.project.utils.ValidadorNegocio;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller para gerenciar Produtos
 *
 * Endpoints:
 * - GET /api/produtos → Listar todos os produtos
 * - GET /api/produtos/{id} → Buscar produto por ID
 * - GET /api/produtos/categoria/{categoria} → Buscar por categoria
 * - GET /api/produtos/fornecedor/{fornecedorId} → Buscar por fornecedor
 * - POST /api/produtos → Criar novo produto
 * - PUT /api/produtos/{id} → Atualizar produto
 * - DELETE /api/produtos/{id} → Deletar produto
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutosRepository produtosRepository;

    /**
     * Listar todos os produtos
     * GET /api/produtos
     */
    @GetMapping
    public ResponseEntity<List<Produtos>> listAll() {
        List<Produtos> produtos = produtosRepository.findAll();
        return ResponseEntity.ok(produtos);
    }

    /**
     * Buscar produto por ID
     * GET /api/produtos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Produtos> getById(@PathVariable Long id) {
        Optional<Produtos> produto = produtosRepository.findById(id);
        return produto
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Buscar produtos por categoria
     * GET /api/produtos/categoria/{categoria}
     */
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Produtos>> getByCategoria(@PathVariable String categoria) {
        List<Produtos> produtos = produtosRepository.findByCategoria(categoria);
        return ResponseEntity.ok(produtos);
    }

    /**
     * Buscar produtos por fornecedor
     * GET /api/produtos/fornecedor/{fornecedorId}
     */
    @GetMapping("/fornecedor/{fornecedorId}")
    public ResponseEntity<List<Produtos>> getByFornecedor(@PathVariable Long fornecedorId) {
        List<Produtos> produtos = produtosRepository.findByFornecedorId(fornecedorId);
        return ResponseEntity.ok(produtos);
    }

    /**
     * Criar novo produto
     * POST /api/produtos
     * 
     * Restrição: Somente Gerentes e Masters podem criar produtos
     * Funcionários não podem criar produtos
     * 
     * Headers obrigatórios:
     * - gerente-cargo: Cargo do usuário (GERENTE ou MASTER)
     */
    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody Produtos produto,
            @RequestHeader(value = "gerente-cargo", required = true) String cargoHeader) {
        try {
            // Validar permissão
            Cargos cargo = Cargos.valueOf(cargoHeader);
            
            // Funcionários não podem criar produtos
            if (cargo == Cargos.FUNCIONARIO) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Funcionários não podem cadastrar produtos. Apenas Gerentes e Masters.");
            }
            
            // Verifica se é Gerente ou Master
            if (cargo != Cargos.GERENTE && cargo != Cargos.MASTER) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Apenas Gerentes e Masters podem cadastrar produtos");
            }
            
            Produtos saved = produtosRepository.save(produto);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Cargo inválido fornecido no header gerente-cargo");
        }
    }

    /**
     * Atualizar produto
     * PUT /api/produtos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Produtos> update(@PathVariable Long id, @RequestBody Produtos produtoDetails) {
        Optional<Produtos> produto = produtosRepository.findById(id);
        if (produto.isPresent()) {
            Produtos p = produto.get();
            if (produtoDetails.getNome() != null)
                p.setNome(produtoDetails.getNome());
            if (produtoDetails.getDescricao() != null)
                p.setDescricao(produtoDetails.getDescricao());
            if (produtoDetails.getPreco() != null)
                p.setPreco(produtoDetails.getPreco());
            if (produtoDetails.getQuantidade() >= 0)
                p.setQuantidade(produtoDetails.getQuantidade());

            Produtos updated = produtosRepository.save(p);
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Deletar produto
     * DELETE /api/produtos/{id}
     * 
     * Restrição: Somente Gerentes e Masters podem deletar produtos
     * Funcionários não podem deletar produtos
     * 
     * Headers obrigatórios:
     * - gerente-cargo: Cargo do usuário (GERENTE ou MASTER)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id,
            @RequestHeader(value = "gerente-cargo", required = true) String cargoHeader) {
        try {
            // Validar permissão
            Cargos cargo = Cargos.valueOf(cargoHeader);
            
            // Funcionários não podem deletar produtos
            if (cargo == Cargos.FUNCIONARIO) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Funcionários não podem deletar produtos. Apenas Gerentes e Masters.");
            }
            
            // Verifica se é Gerente ou Master
            if (cargo != Cargos.GERENTE && cargo != Cargos.MASTER) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Apenas Gerentes e Masters podem deletar produtos");
            }
            
            if (produtosRepository.existsById(id)) {
                produtosRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Cargo inválido fornecido no header gerente-cargo");
        }
    }
}


