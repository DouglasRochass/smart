package com.crud.project.controllers;

import com.crud.project.models.Usuario;
import com.crud.project.repositories.UsuarioRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller para gerenciar Usuários
 *
 * Endpoints:
 * - GET /api/usuarios → Listar todos os usuários
 * - GET /api/usuarios/{id} → Buscar usuário por ID
 * - GET /api/usuarios/email/{email} → Buscar usuário por email
 * - POST /api/usuarios → Criar novo usuário
 * - PUT /api/usuarios/{id} → Atualizar usuário
 * - DELETE /api/usuarios/{id} → Deletar usuário
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Listar todos os usuários
     * GET /api/usuarios
     */
    @GetMapping
    public ResponseEntity<List<Usuario>> listAll() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Buscar usuário por ID
     * GET /api/usuarios/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getById(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Buscar usuário por email
     * GET /api/usuarios/email/{email}
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> getByEmail(@PathVariable String email) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        return usuario
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Criar novo usuário
     * POST /api/usuarios
     * Body: { "nomeColaborador": "...", "email": "...", "senha": "...", ... }
     */
    @PostMapping
    public ResponseEntity<Usuario> create(@RequestBody Usuario usuario) {
        Usuario saved = usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Atualizar usuário
     * PUT /api/usuarios/{id}
     * Body: { "nomeColaborador": "...", ... }
     */
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> update(@PathVariable Long id, @RequestBody Usuario usuarioDetails) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            Usuario u = usuario.get();
            if (usuarioDetails.getNomeColaborador() != null)
                u.setNomeColaborador(usuarioDetails.getNomeColaborador());
            if (usuarioDetails.getEmail() != null)
                u.setEmail(usuarioDetails.getEmail());
            if (usuarioDetails.getCargos() != null)
                u.setCargos(usuarioDetails.getCargos());

            Usuario updated = usuarioRepository.save(u);
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Deletar usuário
     * DELETE /api/usuarios/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

