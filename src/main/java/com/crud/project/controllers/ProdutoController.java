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
            @RequestBody java.util.Map<String, Object> payload,
            @RequestHeader(value = "gerente-cargo", required = true) String cargoHeader) {
        try {
            log.info("=== CRIAR PRODUTO ===");
            log.info("Cargo recebido: {}", cargoHeader);
            log.info("Payload recebido: {}", payload);
            log.info("Payload class: {}", payload.getClass().getName());
            
            // Validar permissão
            Cargos cargo;
            try {
                cargo = Cargos.valueOf(cargoHeader);
                log.info("Cargo parseado: {}", cargo);
            } catch (Exception e) {
                log.error("Erro ao parsear cargo: {}", cargoHeader, e);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Cargo inválido: " + cargoHeader);
            }
            
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
            
            log.info("Permissão validada. Criando produto...");
            
            // Criar produto manualmente
            Produtos produto = new Produtos();
            
            // Validar e setar nome
            log.info("Processando nome...");
            String nome = (String) payload.get("nome");
            if (nome == null || nome.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Nome do produto é obrigatório");
            }
            produto.setNome(nome.trim());
            log.info("Nome setado: {}", produto.getNome());
            
            // Validar e setar descrição
            log.info("Processando descrição...");
            String descricao = (String) payload.get("descricao");
            if (descricao == null || descricao.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Descrição é obrigatória");
            }
            produto.setDescricao(descricao.trim());
            log.info("Descrição setada: {}", produto.getDescricao());
            
            // Validar e setar categoria
            log.info("Processando categoria...");
            String categoria = (String) payload.get("categoria");
            if (categoria == null || categoria.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Categoria é obrigatória");
            }
            produto.setCategoria(categoria.trim());
            log.info("Categoria setada: {}", produto.getCategoria());
            
            // Validar e setar preço
            log.info("Processando preço...");
            Object precoObj = payload.get("preco");
            log.info("Preço objeto: {} (tipo: {})", precoObj, precoObj != null ? precoObj.getClass().getName() : "null");
            if (precoObj == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Preço é obrigatório");
            }
            Double preco = precoObj instanceof Number ? ((Number) precoObj).doubleValue() : Double.parseDouble(precoObj.toString());
            if (preco <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Preço deve ser maior que zero");
            }
            produto.setPreco(preco);
            log.info("Preço setado: {}", produto.getPreco());
            
            // Validar e setar quantidade
            log.info("Processando quantidade...");
            Object quantidadeObj = payload.get("quantidade");
            log.info("Quantidade objeto: {} (tipo: {})", quantidadeObj, quantidadeObj != null ? quantidadeObj.getClass().getName() : "null");
            if (quantidadeObj == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Quantidade é obrigatória");
            }
            Integer quantidade = quantidadeObj instanceof Number ? ((Number) quantidadeObj).intValue() : Integer.parseInt(quantidadeObj.toString());
            if (quantidade < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Quantidade não pode ser negativa");
            }
            produto.setQuantidade(quantidade);
            log.info("Quantidade setada: {}", produto.getQuantidade());
            
            // Validar e setar fornecedor
            log.info("Processando fornecedorId...");
            Object fornecedorIdObj = payload.get("fornecedorId");
            log.info("FornecedorId objeto: {} (tipo: {})", fornecedorIdObj, fornecedorIdObj != null ? fornecedorIdObj.getClass().getName() : "null");
            if (fornecedorIdObj == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Fornecedor é obrigatório");
            }
            Long fornecedorId = fornecedorIdObj instanceof Number ? ((Number) fornecedorIdObj).longValue() : Long.parseLong(fornecedorIdObj.toString());
            if (fornecedorId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Fornecedor inválido");
            }
            produto.setFornecedorId(fornecedorId);
            log.info("FornecedorId setado: {}", produto.getFornecedorId());
            
            log.info("Produto completo antes de salvar: {}", produto);
            log.info("Salvando no banco de dados...");
            
            Produtos saved = produtosRepository.save(produto);
            
            log.info("Produto salvo com sucesso! ID: {}", saved.getId());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
            
        } catch (IllegalArgumentException e) {
            log.error("Erro de argumento inválido", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Erro de validação: " + e.getMessage());
        } catch (NumberFormatException e) {
            log.error("Erro de formato de número", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Formato de número inválido: " + e.getMessage());
        } catch (org.springframework.dao.DataAccessException e) {
            log.error("Erro de acesso ao banco de dados", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao acessar banco de dados: " + e.getMessage());
        } catch (Exception e) {
            log.error("Erro inesperado ao criar produto", e);
            log.error("Stack trace completo:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao criar produto: " + e.getClass().getSimpleName() + " - " + e.getMessage());
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


