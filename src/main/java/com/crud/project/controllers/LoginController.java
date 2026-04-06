package com.crud.project.controllers;

import com.crud.project.dto.LoginRequest;
import com.crud.project.dto.LoginResponse;
import com.crud.project.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * Endpoint para obter página de login (GET)
     * GET /api/login
     * Retorna informações sobre o endpoint de login
     */
    @GetMapping("/login")
    public ResponseEntity<?> loginPage() {
        return ResponseEntity.ok(new MessageResponse(
            "Use POST /api/login com { \"email\": \"seu@email.com\", \"senha\": \"sua_senha\" }"
        ));
    }

    /**
     * Endpoint de Login
     * POST /api/login
     * Body: { "email": "user@example.com", "senha": "password123" }
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = authenticationService.authenticate(loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            String message = e.getMessage();

            // Verificar se é erro de MongoDB indisponível
            if (message != null && message.contains("MongoDB indisponível")) {
                log.error("MongoDB indisponível: {}", message);
                return ResponseEntity
                        .status(HttpStatus.SERVICE_UNAVAILABLE) // 503
                        .body(new ErrorResponse(
                            "MongoDB Indisponível",
                            "O servidor de banco de dados não está disponível. Por favor, tente novamente em alguns momentos."
                        ));
            }

            // Verificar se é erro de conexão com banco
            if (message != null && message.contains("conexão com banco")) {
                log.error("Erro de conexão: {}", message);
                return ResponseEntity
                        .status(HttpStatus.SERVICE_UNAVAILABLE) // 503
                        .body(new ErrorResponse(
                            "Erro de Conexão",
                            "Não conseguimos conectar ao banco de dados. Verifique sua conexão de internet."
                        ));
            }

            // Erro de autenticação (credenciais inválidas)
            log.warn("Falha de autenticação: {}", message);
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED) // 401
                    .body(new ErrorResponse("Falha no login", message != null ? message : "Email ou senha inválidos"));
        } catch (Exception e) {
            log.error("Erro inesperado no login: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
                    .body(new ErrorResponse("Erro no servidor", "Ocorreu um erro inesperado. Tente novamente."));
        }
    }

    /**
     * Endpoint para recuperar senha
     * POST /api/recuperar-senha
     * Body: { "email": "user@example.com" }
     */
    @PostMapping("/recuperar-senha")
    public ResponseEntity<?> recuperarSenha(@RequestBody EmailRequest emailRequest) {
        try {
            boolean exists = authenticationService.emailExists(emailRequest.getEmail());
            if (exists) {
                // TODO: Implementar lógica de envio de email com token de recuperação
                return ResponseEntity.ok(new MessageResponse("Email de recuperação de senha foi enviado"));
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Usuário não encontrado", "Não há usuário registrado com este email"));
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro ao processar requisição", e.getMessage()));
        }
    }

    /**
     * Endpoint para recuperar email
     * POST /api/recuperar-email
     * Body: { "cpf": "12345678900" }
     */
    @PostMapping("/recuperar-email")
    public ResponseEntity<?> recuperarEmail(@RequestBody CpfRequest cpfRequest) {
        try {
            // TODO: Implementar lógica de busca por CPF
            return ResponseEntity.ok(new MessageResponse("Email do colaborador foi enviado para o celular registrado"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro ao processar requisição", e.getMessage()));
        }
    }

    // Classes auxiliares para respostas
    @Data
    @Getter
    @Setter
    @NoArgsConstructor
    public static class ErrorResponse {
        private String error;
        private String message;

        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
        }
    }

    @Data
    @Getter
    @Setter
    @NoArgsConstructor
    public static class MessageResponse {
        private String message;

        public MessageResponse(String message) {
            this.message = message;
        }
    }

    @Data
    @Getter
    @Setter
    @NoArgsConstructor
    public static class EmailRequest {
        private String email;
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    @Data
    @Getter
    @Setter
    @NoArgsConstructor
    public static class CpfRequest {
        private String cpf;
    }
}
