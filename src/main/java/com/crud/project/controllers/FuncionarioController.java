package com.crud.project.controllers;

import com.crud.project.dto.FuncionarioDTO;
import com.crud.project.models.Cargos;
import com.crud.project.models.Funcionario;
import com.crud.project.repositories.FuncionarioRepository;
import com.crud.project.utils.ValidadorNegocio;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller para gerenciar Funcionários
 *
 * Restrições:
 * - Um funcionário pode estar trabalhando somente em um mercado
 * - Somente Gerentes e Masters podem visualizar informações de todos os funcionários
 * - Funcionários normais veem apenas colegas do seu mercado (sem CPF)
 * - Funcionário não pode apagar outro funcionário
 * - Funcionário não pode cadastrar produtos
 * - Funcionário não pode ver outros mercados
 *
 * Endpoints:
 * - GET /api/funcionarios → Listar funcionários (com restrições)
 * - GET /api/funcionarios/{id} → Buscar funcionário por ID
 * - GET /api/funcionarios/mercado/{mercadoId} → Buscar funcionários por mercado
 * - POST /api/funcionarios → Criar novo funcionário
 * - PUT /api/funcionarios/{id} → Atualizar funcionário
 * - DELETE /api/funcionarios/{id} → Deletar funcionário
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    /**
     * Converter Funcionario para FuncionarioDTO (sem CPF)
     */
    private FuncionarioDTO toDTO(Funcionario funcionario) {
        FuncionarioDTO dto = new FuncionarioDTO();
        dto.setId(funcionario.getId());
        dto.setNomeColaborador(funcionario.getNomeColaborador());
        dto.setEmail(funcionario.getEmail());
        dto.setCargos(funcionario.getCargos());
        dto.setMercadoId(funcionario.getMercadoId());
        dto.setAtivo(funcionario.isAtivo());
        dto.setIdade(funcionario.getIdade());
        return dto;
    }

    /**
     * Listar funcionários
     * GET /api/funcionarios
     * 
     * Restrições:
     * - Gerentes/Masters: veem todos (sem CPF)
     * - Funcionários: veem apenas do seu mercado (sem CPF)
     * 
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
                
                // Gerentes e Masters veem todos os funcionários
                if (cargo == Cargos.GERENTE || cargo == Cargos.MASTER) {
                    List<Funcionario> funcionarios = funcionarioRepository.findAll();
                    List<FuncionarioDTO> dtos = funcionarios.stream()
                        .map(this::toDTO)
                        .collect(Collectors.toList());
                    return ResponseEntity.ok(dtos);
                } 
                // Funcionários normais veem apenas seu mercado
                else if (cargo == Cargos.FUNCIONARIO) {
                    if (usuarioMercadoId == null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Funcionários devem fornecer usuario-mercado-id");
                    }
                    List<Funcionario> funcionarios = funcionarioRepository.findByMercadoId(usuarioMercadoId);
                    List<FuncionarioDTO> dtos = funcionarios.stream()
                        .map(this::toDTO)
                        .collect(Collectors.toList());
                    return ResponseEntity.ok(dtos);
                }
            }
            
            List<Funcionario> funcionarios = funcionarioRepository.findAll();
            List<FuncionarioDTO> dtos = funcionarios.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Cargo inválido fornecido");
        }
    }

    /**
     * Buscar funcionário por ID
     * GET /api/funcionarios/{id}
     * 
     * Restrições:
     * - Gerentes/Masters: veem qualquer funcionário (sem CPF)
     * - Funcionários: veem apenas se estiver no mesmo mercado (sem CPF)
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable Long id,
            @RequestHeader(value = "gerente-cargo", required = false) String cargoHeader,
            @RequestHeader(value = "usuario-mercado-id", required = false) Long usuarioMercadoId) {
        try {
            Optional<Funcionario> funcionario = funcionarioRepository.findById(id);
            
            if (!funcionario.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            Funcionario func = funcionario.get();
            
            if (cargoHeader != null) {
                Cargos cargo = Cargos.valueOf(cargoHeader);
                
                // Gerentes e Masters veem qualquer funcionário
                if (cargo == Cargos.GERENTE || cargo == Cargos.MASTER) {
                    return ResponseEntity.ok(toDTO(func));
                }
                // Funcionários normais veem apenas se estiver no mesmo mercado
                else if (cargo == Cargos.FUNCIONARIO) {
                    if (usuarioMercadoId == null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Funcionários devem fornecer usuario-mercado-id");
                    }
                    if (!func.getMercadoId().equals(usuarioMercadoId)) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body("Você só pode visualizar funcionários do seu mercado");
                    }
                    return ResponseEntity.ok(toDTO(func));
                }
            }
            
            return ResponseEntity.ok(toDTO(func));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Cargo inválido fornecido");
        }
    }

    /**
     * Buscar funcionários por mercado
     * GET /api/funcionarios/mercado/{mercadoId}
     * 
     * Restrição:
     * - Funcionários só podem consultar seu próprio mercado
     */
    @GetMapping("/mercado/{mercadoId}")
    public ResponseEntity<?> getByMercado(
            @PathVariable Long mercadoId,
            @RequestHeader(value = "gerente-cargo", required = false) String cargoHeader,
            @RequestHeader(value = "usuario-mercado-id", required = false) Long usuarioMercadoId) {
        try {
            if (cargoHeader != null) {
                Cargos cargo = Cargos.valueOf(cargoHeader);
                
                // Funcionários só veem seu próprio mercado
                if (cargo == Cargos.FUNCIONARIO) {
                    if (usuarioMercadoId == null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Funcionários devem fornecer usuario-mercado-id");
                    }
                    if (!mercadoId.equals(usuarioMercadoId)) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body("Você só pode visualizar funcionários do seu mercado");
                    }
                }
            }
            
            List<Funcionario> funcionarios = funcionarioRepository.findByMercadoId(mercadoId);
            List<FuncionarioDTO> dtos = funcionarios.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Cargo inválido fornecido");
        }
    }

    /**
     * Criar novo funcionário
     * POST /api/funcionarios
     * 
     * Restrição: Um funcionário deve estar em apenas um mercado
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Funcionario funcionario) {
        // Validar que o funcionário tem apenas um mercado
        if (funcionario.getMercadoId() == null || funcionario.getMercadoId() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Funcionário deve estar associado a um mercado");
        }

        // Verificar se já existe outro funcionário com o mesmo CPF
        List<Funcionario> existentes = funcionarioRepository.findAll();
        boolean cpfDuplicado = existentes.stream()
            .anyMatch(f -> f.getCpf().equals(funcionario.getCpf()));
        
        if (cpfDuplicado) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("CPF já cadastrado no sistema");
        }

        Funcionario saved = funcionarioRepository.save(funcionario);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(saved));
    }

    /**
     * Atualizar funcionário
     * PUT /api/funcionarios/{id}
     * 
     * Restrição: Um funcionário não pode ser transferido para múltiplos mercados
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id, 
            @RequestBody Funcionario funcionarioDetails,
            @RequestHeader(value = "gerente-cargo", required = false) String cargoHeader,
            @RequestHeader(value = "usuario-id", required = false) Long usuarioId) {
        Optional<Funcionario> funcionario = funcionarioRepository.findById(id);
        if (funcionario.isPresent()) {
            Funcionario f = funcionario.get();
            
            // Se gerente-cargo for fornecido
            if (cargoHeader != null) {
                Cargos cargo = Cargos.valueOf(cargoHeader);
                // Apenas o próprio funcionário ou gerentes/masters podem atualizar
                if (cargo == Cargos.FUNCIONARIO && !f.getId().equals(usuarioId)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Funcionário só pode atualizar seus próprios dados");
                }
            }
            
            // Se está tentando mudar o mercado, validar
            if (funcionarioDetails.getMercadoId() != null && 
                !funcionarioDetails.getMercadoId().equals(f.getMercadoId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Funcionário não pode ser transferido para outro mercado. " +
                          "Ele já está associado ao mercado ID: " + f.getMercadoId());
            }
            
            if (funcionarioDetails.getNomeColaborador() != null)
                f.setNomeColaborador(funcionarioDetails.getNomeColaborador());
            if (funcionarioDetails.getEmail() != null)
                f.setEmail(funcionarioDetails.getEmail());
            if (funcionarioDetails.getIdade() > 0)
                f.setIdade(funcionarioDetails.getIdade());

            Funcionario updated = funcionarioRepository.save(f);
            return ResponseEntity.ok(toDTO(updated));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Deletar funcionário
     * DELETE /api/funcionarios/{id}
     * 
     * Restrição: 
     * - Funcionários NÃO podem apagar ninguém
     * - Apenas Gerentes podem deletar funcionários DO SEU MERCADO
     * - Masters podem deletar de qualquer mercado
     * 
     * Headers obrigatórios:
     * - gerente-cargo: Cargo do usuário (FUNCIONARIO, GERENTE, MASTER)
     * - usuario-mercado-id: Mercado do gerente (obrigatório se for GERENTE)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id,
            @RequestHeader(value = "gerente-cargo", required = false) String cargoHeader,
            @RequestHeader(value = "usuario-mercado-id", required = false) Long usuarioMercadoId) {
        try {
            Optional<Funcionario> funcionarioOpt = funcionarioRepository.findById(id);
            if (!funcionarioOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            Funcionario funcionario = funcionarioOpt.get();
            
            if (cargoHeader != null) {
                Cargos cargo = Cargos.valueOf(cargoHeader);
                
                // Funcionários não podem deletar ninguém
                if (cargo == Cargos.FUNCIONARIO) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Funcionários não podem deletar outros funcionários");
                }
                
                // Gerentes só podem deletar do seu mercado
                if (cargo == Cargos.GERENTE) {
                    if (usuarioMercadoId == null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Gerentes devem fornecer usuario-mercado-id");
                    }
                    
                    // Verifica se o funcionário é do mercado do gerente
                    if (!funcionario.getMercadoId().equals(usuarioMercadoId)) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body("Gerente só pode deletar funcionários do seu próprio mercado (Mercado " + 
                                  usuarioMercadoId + "). Este funcionário é do Mercado " + funcionario.getMercadoId());
                    }
                }
                
                // Masters podem deletar de qualquer lugar
                // (cargo == Cargos.MASTER - sem restrição)
            }
            
            funcionarioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Cargo inválido fornecido");
        }
    }
}


