# Restrições e Validações Implementadas

## 📋 Resumo das Restrições Implementadas

### 1. Um funcionário por mercado
- Funcionário deve estar associado a apenas um mercado
- Não pode ser transferido para outro mercado
- CPF não pode ser duplicado no sistema

### 2. Somente Gerentes podem gerenciar produtos
- Criar produtos: apenas GERENTE/MASTER
- Deletar produtos: apenas GERENTE/MASTER
- Funcionários não podem criar/deletar produtos

### 3. Somente Gerentes podem ver informações de funcionários
- Listar todos: apenas GERENTE/MASTER
- Buscar por ID: GERENTE/MASTER veem qualquer um, FUNCIONARIO vê apenas do seu mercado
- Buscar por mercado: FUNCIONARIO só vê seu próprio mercado
- CPF nunca é retornado aos funcionários (sempre usa DTO sem CPF)

### 4. Funcionário não pode apagar outro funcionário
- DELETE /api/funcionarios/{id}: apenas GERENTE/MASTER
- Funcionários recebem 403 Forbidden ao tentar deletar

### 5. Funcionário não pode ver outros mercados
- Funcionários listar mercados: recebem apenas seu mercado
- Funcionários ver detalhes: recebem apenas seu mercado
- Gerentes/Masters: veem todos os mercados

### 6. Validação de CPF e CNPJ

### Validador de CPF
- Arquivo: `com.crud.project.utils.validation.ValidCPF`
- Aplica para: `Usuario`, `Funcionario`
- Valida se o CPF tem 11 dígitos e passa nos testes de check digit
- Rejeita CPF com todos os dígitos iguais

### Validador de CNPJ
- Arquivo: `com.crud.project.utils.validation.ValidCNPJ`
- Aplica para: `Fornecedor`
- Valida se o CNPJ tem 14 dígitos e passa nos testes de check digit
- Rejeita CNPJ com todos os dígitos iguais

### Como usar:
```bash
POST /api/usuarios
{
    "nomeColaborador": "João Silva",
    "email": "joao@example.com",
    "senha": "senha123",
    "cpf": "12345678909",  # CPF válido
    "cargos": "FUNCIONARIO"
}

POST /api/fornecedores
{
    "nome": "Empresa XYZ",
    "email": "contato@xyz.com",
    "cnpj": "12345678901234",  # CNPJ válido
    "telefone": "1122334455",
    "endereco": "Rua A, 123"
}
```

## 2. Preço de Produtos Apenas Positivo

- **Campo**: `Produtos.preco`
- **Validação**: `@DecimalMin(value = "0.01")`
- **Rejeita**: Preços <= 0

```bash
POST /api/produtos
{
    "nome": "Produto A",
    "descricao": "Descrição",
    "preco": 19.99,  # Deve ser > 0
    "quantidade": 10,
    "fornecedorId": 1,
    "categoria": "Alimentos"
}
```

## 3. Restrições de Funcionário

### 3.1 - Um Funcionário por Mercado
- **Restrição**: Um funcionário trabalha em apenas um mercado
- **Validação**: 
  - No POST: Valida que mercadoId é fornecido e válido
  - No PUT: Impede transferência de funcionário para outro mercado
  
### 3.2 - Funcionário Não Pode Apagar Outro Funcionário
- **Endpoint**: `DELETE /api/funcionarios/{id}`
- **Restrição**: 
  - Funcionários: 403 Forbidden
  - Gerentes: Podem deletar apenas funcionários do SEU mercado
  - Masters: Podem deletar de qualquer mercado
- **Headers obrigatórios para GERENTE**: `usuario-mercado-id`

### 3.3 - Funcionário Não Pode Ver CPF de Outros
- **Implementação**: Usa DTO `FuncionarioDTO` sem campo CPF
- **Todos os retornos**: Funcionários retornam sem CPF
- **Gerentes/Masters**: Veem dados completos (sem CPF no DTO também)

### 3.4 - Funcionário Não Pode Ver Outros Mercados
- **GET /api/mercados**: FUNCIONARIO vê apenas seu mercado
- **GET /api/funcionarios**: FUNCIONARIO vê apenas colegas do seu mercado
- **GET /api/funcionarios/{id}**: FUNCIONARIO vê apenas se for do seu mercado

### Endpoints de Funcionário:
- `GET /api/funcionarios` - Listar (com restrições por cargo)
- `GET /api/funcionarios/{id}` - Buscar (com restrições por cargo)
- `GET /api/funcionarios/mercado/{mercadoId}` - Listar por mercado (com restrições)
- `POST /api/funcionarios` - Criar
- `PUT /api/funcionarios/{id}` - Atualizar
- `DELETE /api/funcionarios/{id}` - Deletar (apenas GERENTE/MASTER)

```bash
# Listar funcionários - GERENTE vê todos
GET /api/funcionarios
Header: gerente-cargo: GERENTE
Response: [{ id, nome, email, cargo, mercado, ativo, idade }, ...]  # SEM CPF

# Listar funcionários - FUNCIONARIO vê apenas seu mercado
GET /api/funcionarios
Header: gerente-cargo: FUNCIONARIO
Header: usuario-mercado-id: 1
Response: [{ id, nome, email, cargo, mercado, ativo, idade }, ...]  # SEM CPF

# Listar por mercado - FUNCIONARIO vê apenas seu mercado
GET /api/funcionarios/mercado/1
Header: gerente-cargo: FUNCIONARIO
Header: usuario-mercado-id: 1
Response: [{ id, nome, email, cargo, mercado, ativo, idade }, ...]  # SEM CPF

# Buscar específico - FUNCIONARIO vê apenas se for do seu mercado
GET /api/funcionarios/1
Header: gerente-cargo: FUNCIONARIO
Header: usuario-mercado-id: 1
Response: { id, nome, email, cargo, mercado, ativo, idade }  # SEM CPF

# Tentar buscar de outro mercado - FUNCIONARIO recebe 403
GET /api/funcionarios/1
Header: gerente-cargo: FUNCIONARIO
Header: usuario-mercado-id: 2
Response: 403 Forbidden - "Você só pode visualizar funcionários do seu mercado"

# Deletar - FUNCIONARIO recebe 403
DELETE /api/funcionarios/1
Header: gerente-cargo: FUNCIONARIO
Response: 403 Forbidden - "Funcionários não podem deletar outros funcionários"

# Deletar - GERENTE do mesmo mercado (Sucesso)
DELETE /api/funcionarios/1
Header: gerente-cargo: GERENTE
Header: usuario-mercado-id: 1
Response: 204 No Content (se funcionário é do mercado 1)

# Deletar - GERENTE de mercado diferente (Falha)
DELETE /api/funcionarios/2
Header: gerente-cargo: GERENTE
Header: usuario-mercado-id: 1
Response: 403 Forbidden - "Gerente só pode deletar funcionários do seu próprio mercado (Mercado 1). Este funcionário é do Mercado 2"

# Deletar - MASTER (Sucesso de qualquer mercado)
DELETE /api/funcionarios/1
Header: gerente-cargo: MASTER
Response: 204 No Content

# Criar funcionário
POST /api/funcionarios
{
    "nomeColaborador": "Maria Silva",
    "email": "maria@example.com",
    "senha": "senha123",
    "cpf": "12345678909",
    "mercadoId": 1,  # OBRIGATÓRIO - um funcionário por mercado
    "cargos": "FUNCIONARIO",
    "idade": 30
}

# Tentativa de mudança de mercado retorna erro 400
PUT /api/funcionarios/1
{
    "mercadoId": 2  # ERRO - Funcionário não pode mudar de mercado
}
Response: 400 Bad Request - "Funcionário não pode ser transferido para outro mercado"
```

## 4. Somente Gerentes Podem Adicionar/Remover Produtos

- **Restrição**: Apenas usuários com cargo GERENTE ou MASTER
- **Método**: Passa cargo via header `gerente-cargo`
- **Funcionários**: Recebem 403 Forbidden

### Endpoints protegidos:
- `POST /api/produtos` - Criar produto (requer GERENTE/MASTER)
- `DELETE /api/produtos/{id}` - Deletar produto (requer GERENTE/MASTER)

```bash
# Criar produto - GERENTE sucesso
POST /api/produtos
Header: gerente-cargo: GERENTE
{
    "nome": "Produto XYZ",
    "descricao": "Descrição",
    "preco": 29.99,
    "quantidade": 50,
    "fornecedorId": 1,
    "categoria": "Alimentos"
}
Response: 201 Created

# Criar produto - FUNCIONARIO recebe 403
POST /api/produtos
Header: gerente-cargo: FUNCIONARIO
{...}
Response: 403 Forbidden - "Funcionários não podem cadastrar produtos. Apenas Gerentes e Masters."

# Deletar produto - GERENTE sucesso
DELETE /api/produtos/1
Header: gerente-cargo: GERENTE
Response: 204 No Content

# Deletar produto - FUNCIONARIO recebe 403
DELETE /api/produtos/1
Header: gerente-cargo: FUNCIONARIO
Response: 403 Forbidden - "Funcionários não podem deletar produtos. Apenas Gerentes e Masters."
```

## 5. Somente Gerentes Podem Ver Informações de Funcionários

- **Restrição**: Apenas usuários com cargo GERENTE ou MASTER podem listar TODOS
- **Funcionários**: Veem apenas colegas do seu mercado
- **Método**: Passa cargo via header `gerente-cargo`
- **Header obrigatório para FUNCIONARIO**: `usuario-mercado-id`

### Endpoints protegidos:
- `GET /api/funcionarios` - Listar todos (requer GERENTE/MASTER)
- `GET /api/funcionarios/{id}` - Buscar específico (com restrições)

```bash
# Listar todos - GERENTE/MASTER
GET /api/funcionarios
Header: gerente-cargo: GERENTE
Response: [todos os funcionários - SEM CPF]

# Listar por mercado - FUNCIONARIO vê apenas seu
GET /api/funcionarios/mercado/1
Header: gerente-cargo: FUNCIONARIO
Header: usuario-mercado-id: 1
Response: [funcionários do mercado 1 - SEM CPF]
```

## 5. Restrições de Mercado

### 5.1 - Funcionário Não Pode Ver Outros Mercados
- **GET /api/mercados**: FUNCIONARIO vê apenas seu mercado
- **Gerentes/Masters**: Veem todos os mercados

```bash
# Listar mercados - GERENTE vê todos
GET /api/mercados
Header: gerente-cargo: GERENTE
Response: [todos os mercados]

# Listar mercados - FUNCIONARIO vê apenas seu
GET /api/mercados
Header: gerente-cargo: FUNCIONARIO
Header: usuario-mercado-id: 1
Response: [{ id: 1, nome, endereco, ... }]  # Apenas seu mercado
```

## Padrão de Erro

Quando uma validação falha, o sistema retorna:

```json
{
    "timestamp": "2026-04-06T19:30:00.000-03:00",
    "status": 400,
    "error": "Bad Request",
    "message": "CPF inválido",
    "path": "/api/usuarios"
}
```

Quando autorização falha:

```json
{
    "timestamp": "2026-04-06T19:30:00.000-03:00",
    "status": 403,
    "error": "Forbidden",
    "message": "Apenas Gerentes ou Masters podem executar esta operação",
    "path": "/api/produtos"
}
```

## Classes Utilitárias

### ValidadorDocumentos
- `isValidCPF(String cpf)` - Valida CPF
- `isValidCNPJ(String cnpj)` - Valida CNPJ

### ValidadorNegocio
- `verificarSeGerenteOuMaster(Cargos cargo)` - Valida se é Gerente/Master
- `verificarSeMaster(Cargos cargo)` - Valida se é Master

## Testes Recomendados

1. CPF/CNPJ válidos e inválidos
2. Preço negativo/zero
3. Funcionário sem mercado
4. Transferência de funcionário entre mercados
5. Operações de produto sem autorização
6. Listagem de funcionários sem autorização





