// ============================================================
// MongoDB Initialization Script - SmartStock
// Execute este script no MongoDB Compass ou mongosh
// ============================================================

// Selecionar o banco de dados
use smartstock;

// ============================================================
// 1. CRIAR COLLECTIONS
// ============================================================

// Collection: usuarios
db.createCollection("usuarios");

// Collection: funcionarios
db.createCollection("funcionarios");

// Collection: gerentes
db.createCollection("gerentes");

// Collection: produtos
db.createCollection("produtos");

// Collection: fornecedores
db.createCollection("fornecedores");

// Collection: mercados
db.createCollection("mercados");

// ============================================================
// 2. CRIAR ÍNDICES
// ============================================================

// === USUARIOS ===
db.usuarios.createIndex({ "email": 1 }, { unique: true });
db.usuarios.createIndex({ "cargos": 1 });
db.usuarios.createIndex({ "mercadoId": 1 });
db.usuarios.createIndex({ "ativo": 1 });

// === FUNCIONARIOS ===
db.funcionarios.createIndex({ "cpf": 1 }, { unique: true });
db.funcionarios.createIndex({ "email": 1 });
db.funcionarios.createIndex({ "mercadoId": 1 });
db.funcionarios.createIndex({ "cargos": 1 });
db.funcionarios.createIndex({ "ativo": 1 });

// === GERENTES ===
db.gerentes.createIndex({ "cpf": 1 }, { unique: true });
db.gerentes.createIndex({ "email": 1 });
db.gerentes.createIndex({ "mercadoId": 1 });
db.gerentes.createIndex({ "cargos": 1 });
db.gerentes.createIndex({ "ativo": 1 });

// === PRODUTOS ===
db.produtos.createIndex({ "nome": 1 });
db.produtos.createIndex({ "fornecedorId": 1 });
db.produtos.createIndex({ "categoria": 1 });
db.produtos.createIndex({ "preco": 1 });
db.produtos.createIndex({ "quantidade": 1 });

// === FORNECEDORES ===
db.fornecedores.createIndex({ "cnpj": 1 }, { unique: true });
db.fornecedores.createIndex({ "email": 1 });
db.fornecedores.createIndex({ "nome": 1 });
db.fornecedores.createIndex({ "telefone": 1 });

// === MERCADOS ===
db.mercados.createIndex({ "nome": 1 });
db.mercados.createIndex({ "cidade": 1 });
db.mercados.createIndex({ "estado": 1 });
db.mercados.createIndex({ "telefone": 1 });

// ============================================================
// 3. INSERIR DADOS INICIAIS (SEED DATA)
// ============================================================

// === Inserir Mercados de Exemplo ===
db.mercados.insertMany([
  {
    "_id": ObjectId(),
    "nome": "Mercado Centro",
    "endereco": "Rua Principal, 123",
    "telefone": "(11) 98765-4321",
    "cidade": "São Paulo",
    "estado": "SP"
  },
  {
    "_id": ObjectId(),
    "nome": "Mercado Vila Mariana",
    "endereco": "Avenida Paulista, 456",
    "telefone": "(11) 99876-5432",
    "cidade": "São Paulo",
    "estado": "SP"
  },
  {
    "_id": ObjectId(),
    "nome": "Mercado Zona Leste",
    "endereco": "Rua das Flores, 789",
    "telefone": "(11) 91234-5678",
    "cidade": "São Paulo",
    "estado": "SP"
  }
]);

// === Inserir Fornecedores de Exemplo ===
db.fornecedores.insertMany([
  {
    "_id": ObjectId(),
    "nome": "Fornecedor A - Alimentos",
    "telefone": "(11) 3123-4567",
    "email": "contato@fornecedora.com",
    "endereco": "Rua do Comércio, 100",
    "cnpj": "12.345.678/0001-90"
  },
  {
    "_id": ObjectId(),
    "nome": "Fornecedor B - Bebidas",
    "telefone": "(11) 3234-5678",
    "email": "contato@fornecedorb.com",
    "endereco": "Av. Industrial, 200",
    "cnpj": "98.765.432/0001-10"
  },
  {
    "_id": ObjectId(),
    "nome": "Fornecedor C - Limpeza",
    "telefone": "(11) 3345-6789",
    "email": "contato@fornecedorc.com",
    "endereco": "Rua das Indústrias, 300",
    "cnpj": "55.555.555/0001-55"
  }
]);

// === Inserir Produtos de Exemplo ===
db.produtos.insertMany([
  {
    "_id": ObjectId(),
    "nome": "Arroz Integral 5kg",
    "descricao": "Arroz integral de alta qualidade, fonte de fibras",
    "preco": 25.50,
    "quantidade": 150,
    "fornecedorId": db.fornecedores.findOne({ "nome": "Fornecedor A - Alimentos" })._id.toString(),
    "categoria": "Grãos e Cereais"
  },
  {
    "_id": ObjectId(),
    "nome": "Feijão Carioca 1kg",
    "descricao": "Feijão carioca de excelente qualidade",
    "preco": 8.75,
    "quantidade": 300,
    "fornecedorId": db.fornecedores.findOne({ "nome": "Fornecedor A - Alimentos" })._id.toString(),
    "categoria": "Grãos e Cereais"
  },
  {
    "_id": ObjectId(),
    "nome": "Suco Natural 1L",
    "descricao": "Suco natural de laranja, sem conservantes",
    "preco": 6.99,
    "quantidade": 200,
    "fornecedorId": db.fornecedores.findOne({ "nome": "Fornecedor B - Bebidas" })._id.toString(),
    "categoria": "Bebidas"
  },
  {
    "_id": ObjectId(),
    "nome": "Detergente Neutro 500ml",
    "descricao": "Detergente neutro para limpeza geral",
    "preco": 2.50,
    "quantidade": 500,
    "fornecedorId": db.fornecedores.findOne({ "nome": "Fornecedor C - Limpeza" })._id.toString(),
    "categoria": "Produtos de Limpeza"
  },
  {
    "_id": ObjectId(),
    "nome": "Sabão em Pó 1kg",
    "descricao": "Sabão em pó com aroma fresco",
    "preco": 12.00,
    "quantidade": 250,
    "fornecedorId": db.fornecedores.findOne({ "nome": "Fornecedor C - Limpeza" })._id.toString(),
    "categoria": "Produtos de Limpeza"
  }
]);

// ============================================================
// 4. VERIFICAR COLLECTIONS CRIADAS
// ============================================================

print("\n=== COLLECTIONS CRIADAS ===");
db.getCollectionNames().forEach(function(name) {
  print("✓ Collection: " + name + " (" + db[name].countDocuments() + " documentos)");
});

print("\n=== ÍNDICES CRIADOS ===");
db.getCollectionNames().forEach(function(name) {
  var indexes = db[name].getIndexes();
  print("Índices em " + name + ":");
  indexes.forEach(function(index) {
    print("  - " + JSON.stringify(index.key));
  });
});

print("\n✅ Database inicializado com sucesso!");
print("📊 Banco: smartstock");
print("📝 Collections: " + db.getCollectionNames().length);
print("📦 Documentos de exemplo inseridos para: mercados, fornecedores, produtos");

