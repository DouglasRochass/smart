package com.crud.project.services;

import com.crud.project.dto.LoginRequest;
import com.crud.project.dto.LoginResponse;
import com.crud.project.models.Funcionario;
import com.crud.project.models.Gerente;
import com.crud.project.repositories.FuncionarioRepository;
import com.crud.project.repositories.GerenteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private GerenteRepository gerenteRepository;

    /**
     * Valida credenciais de login e retorna informações do usuário
     */
    public LoginResponse authenticate(LoginRequest loginRequest) {
        try {
            String email = loginRequest.getEmail();
            String senha = loginRequest.getSenha();

            // Buscar como Funcionário
            Optional<Funcionario> funcionario = funcionarioRepository.findByEmail(email);
            if (funcionario.isPresent()) {
                Funcionario func = funcionario.get();

                // Validar senha
                if (validarSenha(senha, func.getSenha())) {
                    if (!func.isAtivo()) {
                        throw new RuntimeException("Usuário inativo");
                    }

                    return LoginResponse.builder()
                            .id(func.getId())
                            .email(func.getEmail())
                            .nomeColaborador(func.getNomeColaborador())
                            .cargo(func.getCargos().toString())
                            .tipo("FUNCIONARIO")
                            .mercadoId(func.getMercadoId())
                            .ativo(func.isAtivo())
                            .token(UUID.randomUUID().toString())
                            .message("Login realizado com sucesso como Funcionário")
                            .build();
                } else {
                    throw new RuntimeException("Senha incorreta");
                }
            }

            // Buscar como Gerente
            Optional<Gerente> gerente = gerenteRepository.findByEmail(email);
            if (gerente.isPresent()) {
                Gerente ger = gerente.get();

                // Validar senha
                if (validarSenha(senha, ger.getSenha())) {
                    if (!ger.isAtivo()) {
                        throw new RuntimeException("Usuário inativo");
                    }

                    return LoginResponse.builder()
                            .id(ger.getId())
                            .email(ger.getEmail())
                            .nomeColaborador(ger.getNomeColaborador())
                            .cargo(ger.getCargos().toString())
                            .tipo("GERENTE")
                            .mercadoId(ger.getMercadoId())
                            .ativo(ger.isAtivo())
                            .token(UUID.randomUUID().toString())
                            .message("Login realizado com sucesso como Gerente")
                            .build();
                } else {
                    throw new RuntimeException("Senha incorreta");
                }
            }

            // Usuário não encontrado
            throw new RuntimeException("Usuário não encontrado");
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro de autenticação: {}", e.getMessage(), e);
            throw new RuntimeException("Erro de conexão com banco de dados.", e);
        }
    }

    /**
     * Valida a senha (comparação simples - em produção usar BCrypt)
     */
    private boolean validarSenha(String senhaDigitada, String senhaArmazenada) {
        // TODO: Implementar BCrypt em produção
        // Para desenvolvimento: comparação direta
        return senhaDigitada.equals(senhaArmazenada);
    }

    /**
     * Verifica se um email existe no sistema
     */
    public boolean emailExists(String email) {
        return funcionarioRepository.findByEmail(email).isPresent() || 
               gerenteRepository.findByEmail(email).isPresent();
    }
}

