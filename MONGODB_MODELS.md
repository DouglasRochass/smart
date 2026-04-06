# 📊 Documentação dos Modelos MongoDB - SmartStock

5. **Enums**: O Cargos é um Enum Java e é serializado como String no MongoDB
4. **Auto Index Creation**: Habilitado via `spring.data.mongodb.auto-index-creation=true`
3. **Referências**: Os IDs de referência (como `mercadoId`, `fornecedorId`) são strings contendo ObjectIds
2. **IDs**: MongoDB gera ObjectIds automaticamente para campos com `@Id`
1. **Segurança de Senha**: As senhas devem ser hasheadas com BCrypt antes de salvar no banco

## 📌 Notas Importantes

---

- [ ] Validações habilitadas
- [ ] Índices criados (executar scripts abaixo)
- [ ] Collections criadas (Spring Data cria automaticamente com `@Document`)
- [ ] Database `smartstock` criada
- [ ] URI de conexão configurada em `MongoConfig.java`
- [ ] MongoDB Atlas ou Local MongoDB rodando

## ✅ Checklist de Setup

---

6. mercados
5. fornecedores
4. produtos
3. gerentes
2. funcionarios
1. usuarios
### Collections Necessárias

```
smartstock
```
### Banco de Dados

```
mongodb+srv://smartstock:smartD%40stock@smartstock.erkrji7.mongodb.net/?appName=smartstock
```
### URI de Conexão

## 🚀 Configuração do MongoDB

---

```
}
  "estado": "SP"
  "cidade": "São Paulo",
  "telefone": "(11) 98765-4321",
  "endereco": "Rua Principal, 123",
  "nome": "Mercado Centro",
  "_id": ObjectId("60d5ec49c1c5e8a1b8c2e3f1"),
{
```json
### Mercado

```
}
  "categoria": "Grãos e Cereais"
  "fornecedorId": "60d5ec49c1c5e8a1b8c2e3f2",
  "quantidade": 150,
  "preco": 25.50,
  "descricao": "Arroz integral de alta qualidade",
  "nome": "Arroz Integral 5kg",
  "_id": ObjectId("60d5ec49c1c5e8a1b8c2e3f5"),
{
```json
### Produto

```
}
  "tipo": "funcionario"
  "ativo": true,
  "mercadoId": "60d5ec49c1c5e8a1b8c2e3f1",
  "cargos": "FUNCIONARIO",
  "senha": "$2a$10$...", // bcrypt hash
  "email": "joao.silva@example.com",
  "nomeColaborador": "João Silva",
  "_id": ObjectId("60d5ec49c1c5e8a1b8c2e3f4"),
{
```json
### Usuário (Funcionário)

## 📝 Exemplos de Documentos

---

```
- PRODUTOS → FORNECEDORES (via fornecedorId)
- USUARIOS/FUNCIONARIOS/GERENTES → MERCADOS (via mercadoId)
Relacionamentos:

└─────────────────────────────────────────┘
│      MERCADOS         FORNECEDORES   PRODUTOS
│         ▼                  ▼              ▼
│         ┌────────┴────────┬──────────────┐
│                  │
│         USUARIOS / FUNCIONARIOS / GERENTES
┌─────────────────────────────────────────┐
```

## 🔗 Relacionamentos entre Collections

---

- `MASTER`: Administrador master do sistema
- `GERENTE`: Gerente de mercado
- `FUNCIONARIO`: Funcionário padrão
### Valores

Enumeração de tipos de cargos disponíveis no sistema.
### Descrição

## 7. **CARGOS (enum)**

---

```
db.mercados.createIndex({ "estado": 1 })
db.mercados.createIndex({ "cidade": 1 })
db.mercados.createIndex({ "nome": 1 })
```javascript
### Índices Recomendados

- `estado`: Opcional
- `cidade`: Opcional
- `telefone`: Obrigatório
- `endereco`: Obrigatório
- `nome`: Obrigatório
### Validações

```
}
  "estado": String
  "cidade": String,
  "telefone": String,
  "endereco": String,
  "nome": String,
  "_id": ObjectId,
{
```json
### Estrutura

Collection para informações de unidades de mercado.
### Descrição

## 6. **MERCADOS (mercados)**

---

```
db.fornecedores.createIndex({ "nome": 1 })
db.fornecedores.createIndex({ "email": 1 })
db.fornecedores.createIndex({ "cnpj": 1 }, { unique: true })
```javascript
### Índices Recomendados

- `cnpj`: Único (opcional mas recomendado)
- `endereco`: Obrigatório
- `email`: Obrigatório
- `telefone`: Obrigatório
- `nome`: Obrigatório
### Validações

```
}
  "cnpj": String (único)
  "endereco": String,
  "email": String,
  "telefone": String,
  "nome": String,
  "_id": ObjectId,
{
```json
### Estrutura

Collection para informações de fornecedores de produtos.
### Descrição

## 5. **FORNECEDORES (fornecedores)**

---

```
db.produtos.createIndex({ "preco": 1 })
db.produtos.createIndex({ "categoria": 1 })
db.produtos.createIndex({ "fornecedorId": 1 })
db.produtos.createIndex({ "nome": 1 })
```javascript
### Índices Recomendados

- `fornecedorId`: Obrigatório
- `quantidade`: >= 0
- `preco`: Obrigatório e > 0
- `descricao`: Obrigatória
- `nome`: Obrigatório
### Validações

```
}
  "categoria": String
  "fornecedorId": String (referência à collection fornecedores),
  "quantidade": Integer (>= 0),
  "preco": Double (> 0),
  "descricao": String,
  "nome": String,
  "_id": ObjectId,
{
```json
### Estrutura

Collection que armazena dados de produtos em estoque.
### Descrição

## 4. **PRODUTOS (produtos)**

---

```
db.gerentes.createIndex({ "cargos": 1 })
db.gerentes.createIndex({ "mercadoId": 1 })
db.gerentes.createIndex({ "cpf": 1 }, { unique: true })
```javascript
### Índices Recomendados

- `mercadoId`: Referência ao mercado gerenciado
- `cargos`: Obrigatório
- `cpf`: Obrigatório e único
- `email`: Obrigatório e válido
- `nomeColaborador`: Obrigatório
### Validações

```
}
  "departamento": String
  "cpf": String (único),
  "ativo": Boolean,
  "mercadoId": String,
  "cargos": Enum ("FUNCIONARIO", "GERENTE", "MASTER"),
  "senha": String,
  "email": String,
  "nomeColaborador": String,
  "_id": ObjectId,
{
```json
### Estrutura

Collection para gerentes de mercados com permissões administrativas.
### Descrição

## 3. **GERENTES (gerentes)**

---

```
db.funcionarios.createIndex({ "cargos": 1 })
db.funcionarios.createIndex({ "mercadoId": 1 })
db.funcionarios.createIndex({ "email": 1 })
db.funcionarios.createIndex({ "cpf": 1 }, { unique: true })
```javascript
### Índices Recomendados

- `cargos`: Obrigatório
- `idade`: Mínimo 0, máximo 150
- `cpf`: Obrigatório e único
- `email`: Obrigatório e válido
- `nomeColaborador`: Obrigatório
### Validações

```
}
  "idade": Integer (0-150)
  "cpf": String (único),
  "ativo": Boolean,
  "mercadoId": String,
  "cargos": Enum ("FUNCIONARIO", "GERENTE", "MASTER"),
  "senha": String,
  "email": String,
  "nomeColaborador": String,
  "_id": ObjectId,
{
```json
### Estrutura

Collection especializada para dados detalhados de funcionários.
### Descrição

## 2. **FUNCIONÁRIOS (funcionarios)**

---

```
db.usuarios.createIndex({ "mercadoId": 1 })
db.usuarios.createIndex({ "cargos": 1 })
db.usuarios.createIndex({ "email": 1 }, { unique: true })
```javascript
### Índices Recomendados

- `ativo`: Padrão = true
- `cargos`: Obrigatório
- `senha`: Obrigatória, não vazia
- `email`: Obrigatório, deve ser válido e único
- `nomeColaborador`: Obrigatório, não vazio
### Validações

```
}
  "tipo": String ("funcionario", "gerente")
  "ativo": Boolean,
  "mercadoId": String (referência à collection mercados),
  "cargos": Enum ("FUNCIONARIO", "GERENTE", "MASTER"),
  "senha": String (hasheada),
  "email": String (único),
  "nomeColaborador": String,
  "_id": ObjectId,
{
```json
### Estrutura

Collection que armazena usuários/colaboradores do sistema com diferentes tipos de acesso.
### Descrição

## 1. **USUÁRIOS (usuarios)**

---

Este documento descreve todos os modelos (collections) do banco de dados MongoDB para o projeto SmartStock.

## 📋 Resumo dos Modelos

