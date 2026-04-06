package com.crud.project.controllers;

import com.crud.project.services.ImportacaoVendasService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller para tratamento de erros HTTP
 * Substitui a página padrão "Whitelabel Error Page"
 */
@RestController
@RequestMapping("/error")
public class CustomErrorController {

    private static final Logger log = LoggerFactory.getLogger(CustomErrorController.class);

    /**
     * Trata erros HTTP e retorna resposta estruturada
     */
    @GetMapping
    @PostMapping
    public ResponseEntity<?> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        int statusCode = status != null ? Integer.parseInt(status.toString()) : 500;

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", System.currentTimeMillis());
        response.put("status", statusCode);

        switch (statusCode) {
            case 404:
                response.put("error", "Not Found");
                response.put("message", "Endpoint não encontrado");
                log.warn("404 Not Found: {}", request.getRequestURI());
                break;
            case 405:
                response.put("error", "Method Not Allowed");
                response.put("message", "Método HTTP não permitido para este endpoint");
                response.put("hint", "Use POST /api/login com JSON body para fazer login");
                log.warn("405 Method Not Allowed: {} {}", request.getMethod(), request.getRequestURI());
                break;
            case 500:
                response.put("error", "Internal Server Error");
                response.put("message", message != null ? message.toString() : "Erro interno do servidor");
                log.error("500 Internal Server Error: {}", request.getRequestURI(), exception);
                break;
            case 503:
                response.put("error", "Service Unavailable");
                response.put("message", "MongoDB não está disponível");
                log.warn("503 Service Unavailable: MongoDB timeout");
                break;
            default:
                response.put("error", "Error");
                response.put("message", message != null ? message.toString() : "Erro desconhecido");
                break;
        }

        response.put("path", request.getRequestURI());

        HttpStatus httpStatus = HttpStatus.resolve(statusCode);
        return ResponseEntity.status(httpStatus != null ? httpStatus : HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

