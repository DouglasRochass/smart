package com.crud.project.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller raiz para boas-vindas e informações gerais
 */
@Slf4j
@RestController
@RequestMapping("/")
public class RootController {

    /**
     * Endpoint raiz - GET /
     * Retorna mensagem de boas-vindas
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> welcome() {
        Map<String, Object> response = new HashMap<>();
        response.put("app", "SmartStock - Sistema de Estoque");
        response.put("version", "1.0.0");
        response.put("status", "Online");
        response.put("message", "Bem-vindo à API!");
        response.put("endpoints", new HashMap<String, String>() {{
            put("health", "GET /api/health");
            put("info", "GET /api/info");
            put("usuarios", "GET /api/usuarios");
            put("fornecedores", "GET /api/fornecedores");
            put("produtos", "GET /api/produtos");
            put("mercados", "GET /api/mercados");
            put("login", "POST /api/login");
        }});
        return ResponseEntity.ok(response);
    }
}

