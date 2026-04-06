# Testes de Restrições e Validações

## Testes de CPF e CNPJ

### 1. CPF Válido
```bash
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nomeColaborador": "João Silva",
    "email": "joao@example.com",
    "senha": "senha123",
    "cpf": "12345678909",
    "cargos": "FUNCIONARIO"
  }'
# Esperado: 201 Created
```

### 2. CPF Inválido
```bash
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nomeColaborador": "João Silva",
    "email": "joao@example.com",
    "senha": "senha123",
    "cpf": "12345678900",
    "cargos": "FUNCIONARIO"
  }'
# Esperado: 400 Bad Request - "CPF inválido"
```

### 3. CNPJ Válido
```bash
curl -X POST http://localhost:8080/api/fornecedores \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Empresa XYZ",
    "email": "contato@xyz.com",
    "cnpj": "12345678901234",
    "telefone": "1122334455",
    "endereco": "Rua A, 123"
  }'
# Esperado: 201 Created
```

### 4. CNPJ Inválido
```bash
curl -X POST http://localhost:8080/api/fornecedores \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Empresa XYZ",
    "email": "contato@xyz.com",
    "cnpj": "12345678901200",
    "telefone": "1122334455",
    "endereco": "Rua A, 123"
  }'
# Esperado: 400 Bad Request - "CNPJ inválido"
```

## Testes de Preço Positivo

### 1. Preço Válido (> 0)
```bash
curl -X POST http://localhost:8080/api/produtos \
  -H "Content-Type: application/json" \
  -H "gerente-cargo: GERENTE" \
  -d '{
    "nome": "Produto A",
    "descricao": "Descrição",
    "preco": 19.99,
    "quantidade": 10,
    "fornecedorId": 1,
    "categoria": "Alimentos"
  }'
# Esperado: 201 Created
```

### 2. Preço Inválido (= 0)
```bash
curl -X POST http://localhost:8080/api/produtos \
  -H "Content-Type: application/json" \
  -H "gerente-cargo: GERENTE" \
  -d '{
    "nome": "Produto A",
    "descricao": "Descrição",
    "preco": 0.0,
    "quantidade": 10,
    "fornecedorId": 1,
    "categoria": "Alimentos"
  }'
# Esperado: 400 Bad Request - "Preço deve ser maior que 0"
```

## Testes de Um Funcionário por Mercado

### 1. Criar Funcionário com Mercado
```bash
curl -X POST http://localhost:8080/api/funcionarios \
  -H "Content-Type: application/json" \
  -d '{
    "nomeColaborador": "Maria Silva",
    "email": "maria@example.com",
    "senha": "senha123",
    "cpf": "12345678901",
    "mercadoId": 1,
    "cargos": "FUNCIONARIO",
    "idade": 30
  }'
# Esperado: 201 Created
```

### 2. Criar Funcionário sem Mercado
```bash
curl -X POST http://localhost:8080/api/funcionarios \
  -H "Content-Type: application/json" \
  -d '{
    "nomeColaborador": "Maria Silva",
    "email": "maria@example.com",
    "senha": "senha123",
    "cpf": "12345678901",
    "cargos": "FUNCIONARIO",
    "idade": 30
  }'
# Esperado: 400 Bad Request - "Funcionário deve estar associado a um mercado"
```

### 3. Transferir Funcionário (Proibido)
```bash
curl -X PUT http://localhost:8080/api/funcionarios/1 \
  -H "Content-Type: application/json" \
  -d '{
    "mercadoId": 2
  }'
# Esperado: 400 Bad Request - "Funcionário não pode ser transferido para outro mercado"
```

## Testes de Produtos (Somente Gerentes)

### 1. Criar Produto - GERENTE (Sucesso)
```bash
curl -X POST http://localhost:8080/api/produtos \
  -H "Content-Type: application/json" \
  -H "gerente-cargo: GERENTE" \
  -d '{
    "nome": "Produto XYZ",
    "descricao": "Descrição",
    "preco": 29.99,
    "quantidade": 50,
    "fornecedorId": 1,
    "categoria": "Alimentos"
  }'
# Esperado: 201 Created
```

### 2. Criar Produto - FUNCIONARIO (Falha)
```bash
curl -X POST http://localhost:8080/api/produtos \
  -H "Content-Type: application/json" \
  -H "gerente-cargo: FUNCIONARIO" \
  -d '{
    "nome": "Produto XYZ",
    "descricao": "Descrição",
    "preco": 29.99,
    "quantidade": 50,
    "fornecedorId": 1,
    "categoria": "Alimentos"
  }'
# Esperado: 403 Forbidden - "Funcionários não podem cadastrar produtos. Apenas Gerentes e Masters."
```

### 3. Deletar Produto - GERENTE (Sucesso)
```bash
curl -X DELETE http://localhost:8080/api/produtos/1 \
  -H "gerente-cargo: GERENTE"
# Esperado: 204 No Content
```

### 4. Deletar Produto - FUNCIONARIO (Falha)
```bash
curl -X DELETE http://localhost:8080/api/produtos/1 \
  -H "gerente-cargo: FUNCIONARIO"
# Esperado: 403 Forbidden - "Funcionários não podem deletar produtos"
```

## Testes de Funcionários (Visualização e Deleção)

### 1. Listar Todos - GERENTE (Sucesso)
```bash
curl -X GET http://localhost:8080/api/funcionarios \
  -H "gerente-cargo: GERENTE"
# Esperado: 200 OK - Lista de funcionários SEM CPF
```

### 2. Listar Todos - FUNCIONARIO (Falha)
```bash
curl -X GET http://localhost:8080/api/funcionarios \
  -H "gerente-cargo: FUNCIONARIO" \
  -H "usuario-mercado-id: 1"
# Esperado: 200 OK - Apenas funcionários do mercado 1, SEM CPF
```

### 4. Deletar Funcionário - GERENTE do mesmo mercado (Sucesso)
```bash
curl -X DELETE http://localhost:8080/api/funcionarios/1 \
  -H "gerente-cargo: GERENTE" \
  -H "usuario-mercado-id: 1"
# Esperado: 204 No Content (se funcionário é do mercado 1)
```

### 5. Deletar Funcionário - GERENTE de mercado diferente (Falha)
```bash
curl -X DELETE http://localhost:8080/api/funcionarios/2 \
  -H "gerente-cargo: GERENTE" \
  -H "usuario-mercado-id: 1"
# Esperado: 403 Forbidden - "Gerente só pode deletar funcionários do seu próprio mercado"
```

### 6. Deletar Funcionário - MASTER (Sucesso de qualquer mercado)
```bash
curl -X DELETE http://localhost:8080/api/funcionarios/1 \
  -H "gerente-cargo: MASTER"
# Esperado: 204 No Content
```

### 5. Buscar Colega (mesmo mercado) - FUNCIONARIO (Sucesso)
```bash
curl -X GET http://localhost:8080/api/funcionarios/2 \
  -H "gerente-cargo: FUNCIONARIO" \
  -H "usuario-mercado-id: 1"
# Esperado: 200 OK - Dados do funcionário SEM CPF (se for do mercado 1)
```

### 6. Buscar Colega (outro mercado) - FUNCIONARIO (Falha)
```bash
curl -X GET http://localhost:8080/api/funcionarios/2 \
  -H "gerente-cargo: FUNCIONARIO" \
  -H "usuario-mercado-id: 1"
# Se funcionário #2 for do mercado 2:
# Esperado: 403 Forbidden - "Você só pode visualizar funcionários do seu mercado"
```

## Testes de Mercados

### 1. Listar Mercados - GERENTE (Todos)
```bash
curl -X GET http://localhost:8080/api/mercados \
  -H "gerente-cargo: GERENTE"
# Esperado: 200 OK - Todos os mercados
```

### 2. Listar Mercados - FUNCIONARIO (Seu Mercado)
```bash
curl -X GET http://localhost:8080/api/mercados \
  -H "gerente-cargo: FUNCIONARIO" \
  -H "usuario-mercado-id: 1"
# Esperado: 200 OK - Apenas mercado #1
```

### 3. Listar Mercados - FUNCIONARIO (Sem Header Mercado)
```bash
curl -X GET http://localhost:8080/api/mercados \
  -H "gerente-cargo: FUNCIONARIO"
# Esperado: 400 Bad Request - "Funcionários devem fornecer usuario-mercado-id"
```

## Resumo de Headers Necessários

### Para Funcionário:
```
gerente-cargo: FUNCIONARIO
usuario-mercado-id: {id-do-seu-mercado}
usuario-id: {seu-id} # Opcional, para PUT
```

### Para Gerente:
```
gerente-cargo: GERENTE
usuario-mercado-id: {id-do-seu-mercado}  # OBRIGATÓRIO para DELETE
usuario-id: {seu-id} # Opcional, para PUT
```

### Para Master:
```
gerente-cargo: MASTER
usuario-mercado-id: {opcional} # Pode filtrar por mercado se fornecido
```

## Dados de Teste CPF Válidos

- 12345678909
- 11144477735 (CPF real de teste)
- 98765432109

## Dados de Teste CNPJ Válidos

- 12345678901234
- 11222333000181 (CNPJ real de teste)
- 34028316000152 (Google)



