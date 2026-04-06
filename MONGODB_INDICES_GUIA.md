# 📇 Como Criar Índices no MongoDB

## 🎯 O que são Índices?

Índices no MongoDB são estruturas de dados que melhoram a velocidade de consultas. Quando você cria um índice em um campo, o MongoDB cria uma referência ordenada para esse campo, acelerando buscas.

---

## 📊 Índices do seu Projeto

Com base nos documentos, você tem estes campos que precisam de índices:

### Collection: `usuarios`
```javascript
db.usuarios.createIndex({ "email": 1 }, { unique: true })
db.usuarios.createIndex({ "cargos": 1 })
db.usuarios.createIndex({ "mercadoId": 1 })
db.usuarios.createIndex({ "ativo": 1 })
```

### Collection: `funcionarios`
```javascript
db.funcionarios.createIndex({ "email": 1 }, { unique: true })
db.funcionarios.createIndex({ "cargos": 1 })
db.funcionarios.createIndex({ "mercadoId": 1 })
db.funcionarios.createIndex({ "ativo": 1 })
db.funcionarios.createIndex({ "cpf": 1 }, { unique: true })
```

### Collection: `gerentes`
```javascript
db.gerentes.createIndex({ "email": 1 }, { unique: true })
db.gerentes.createIndex({ "cargos": 1 })
db.gerentes.createIndex({ "mercadoId": 1 })
db.gerentes.createIndex({ "ativo": 1 })
db.gerentes.createIndex({ "cpf": 1 }, { unique: true })
```

### Collection: `produtos`
```javascript
db.produtos.createIndex({ "fornecedorId": 1 })
db.produtos.createIndex({ "categoria": 1 })
db.produtos.createIndex({ "preco": 1 })
```

### Collection: `vendas`
```javascript
db.vendas.createIndex({ "produtoId": 1 })
db.vendas.createIndex({ "mercadoId": 1 })
db.vendas.createIndex({ "funcionarioId": 1 })
db.vendas.createIndex({ "dataVenda": 1 })
db.vendas.createIndex({ "categoria": 1 })
```

---

## 🚀 3 Formas de Criar Índices

### Opção 1️⃣: MongoDB Compass (GUI - Mais Fácil)

1. Abra **MongoDB Compass**
2. Conecte ao seu MongoDB
3. Selecione o banco `smartstock`
4. Selecione a collection (ex: `usuarios`)
5. Vá para a aba **"Indexes"**
6. Clique em **"Create Index"**
7. Adicione o campo (ex: `email`) com tipo `Ascending (1)`
8. Marque **"Unique"** se necessário
9. Clique **"Create"**

Repita para cada índice necessário.

---

### Opção 2️⃣: MongoDB Shell (mongosh)

```bash
# Conectar ao MongoDB local
mongosh mongodb://localhost:27017/smartstock

# Ou ao MongoDB Atlas
mongosh "mongodb+srv://smartstock:smart%40stock@smartstock.erkrji7.mongodb.net/smartstock"

# Depois execute os comandos acima
db.usuarios.createIndex({ "email": 1 }, { unique: true })
db.usuarios.createIndex({ "cargos": 1 })
# ... etc
```

---

### Opção 3️⃣: Script MongoDB (arquivo .js)

Crie um arquivo `criar_indices.js`:

```javascript
// Conectar ao banco smartstock
use smartstock

// ========== USUARIOS ==========
db.usuarios.createIndex({ "email": 1 }, { unique: true })
db.usuarios.createIndex({ "cargos": 1 })
db.usuarios.createIndex({ "mercadoId": 1 })
db.usuarios.createIndex({ "ativo": 1 })

// ========== FUNCIONARIOS ==========
db.funcionarios.createIndex({ "email": 1 }, { unique: true })
db.funcionarios.createIndex({ "cargos": 1 })
db.funcionarios.createIndex({ "mercadoId": 1 })
db.funcionarios.createIndex({ "ativo": 1 })
db.funcionarios.createIndex({ "cpf": 1 }, { unique: true })

// ========== GERENTES ==========
db.gerentes.createIndex({ "email": 1 }, { unique: true })
db.gerentes.createIndex({ "cargos": 1 })
db.gerentes.createIndex({ "mercadoId": 1 })
db.gerentes.createIndex({ "ativo": 1 })
db.gerentes.createIndex({ "cpf": 1 }, { unique: true })

// ========== PRODUTOS ==========
db.produtos.createIndex({ "fornecedorId": 1 })
db.produtos.createIndex({ "categoria": 1 })
db.produtos.createIndex({ "preco": 1 })

// ========== VENDAS ==========
db.vendas.createIndex({ "produtoId": 1 })
db.vendas.createIndex({ "mercadoId": 1 })
db.vendas.createIndex({ "funcionarioId": 1 })
db.vendas.createIndex({ "dataVenda": 1 })
db.vendas.createIndex({ "categoria": 1 })
```

Execute com:
```bash
mongosh "mongodb+srv://smartstock:smart%40stock@smartstock.erkrji7.mongodb.net/smartstock" < criar_indices.js
```

---

### Opção 4️⃣: MongoDB Atlas Web UI

1. Acesse: https://www.mongodb.com/cloud/atlas
2. Clique no seu cluster
3. Vá para **"Browse Collections"**
4. Selecione a collection
5. Vá para a aba **"Indexes"**
6. Clique **"Create"**
7. Defina os campos

---

## 📋 Significado dos Índices

### `{ "email": 1 }` 
- `1` = Ordem ascendente (A-Z)
- `-1` = Ordem descendente (Z-A)
- Acelera buscas por email

### `{ unique: true }`
- Garante que não há valores duplicados
- Exemplo: email e cpf devem ser únicos

### `{ "dataVenda": 1 }`
- Índice para ordenar por data
- Útil para relatórios

---

## ✅ Verificar Índices Criados

```bash
# Listar todos os índices da collection
db.usuarios.getIndexes()

# Retorna algo como:
[
  { v: 2, key: { _id: 1 } },              // Índice padrão
  { v: 2, key: { email: 1 }, unique: true },
  { v: 2, key: { cargos: 1 } },
  { v: 2, key: { mercadoId: 1 } },
  { v: 2, key: { ativo: 1 } }
]
```

---

## 🗑️ Deletar um Índice

```bash
# Deletar índice específico
db.usuarios.dropIndex("email_1")

# Deletar todos os índices (menos _id)
db.usuarios.dropIndexes()
```

---

## ⚡ Performance

Índices melhoram:
- ✅ Velocidade de buscas (WHERE)
- ✅ Ordenação (ORDER BY)
- ✅ Aggregations

Mas consomem:
- ❌ Espaço em disco
- ❌ Tempo de inserção/atualização

**Dica:** Crie índices apenas para campos que você usa frequentemente em buscas!

---

## 🎯 Recomendação para seu Projeto

Crie os índices em:
1. **email** - Necessário para login (ÚNICO)
2. **cpf** - Necessário para identificação (ÚNICO)
3. **mercadoId** - Para filtros por mercado
4. **cargos** - Para filtros por cargo
5. **ativo** - Para filtros de status

---

## ✅ Próximas Ações

1. Escolha uma das 4 opções acima
2. Execute os comandos
3. Verifique com `db.collection.getIndexes()`
4. Teste as buscas (devem ficar mais rápidas)

**Pronto! Seus índices estão criados! 🚀**

