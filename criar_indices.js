// Script para criar todos os índices do projeto smartstock
// Executar com: mongosh "mongodb+srv://smartstock:smart%40stock@smartstock.erkrji7.mongodb.net/smartstock" < criar_indices.js

use smartstock

// ============================================================
// USUARIOS - Índices
// ============================================================
db.usuarios.createIndex({ "email": 1 }, { unique: true, name: "email_unique" })
db.usuarios.createIndex({ "cargos": 1 }, { name: "cargos_index" })
db.usuarios.createIndex({ "mercadoId": 1 }, { name: "mercadoId_index" })
db.usuarios.createIndex({ "ativo": 1 }, { name: "ativo_index" })

// ============================================================
// FUNCIONARIOS - Índices
// ============================================================
db.funcionarios.createIndex({ "email": 1 }, { unique: true, name: "email_unique" })
db.funcionarios.createIndex({ "cpf": 1 }, { unique: true, name: "cpf_unique" })
db.funcionarios.createIndex({ "cargos": 1 }, { name: "cargos_index" })
db.funcionarios.createIndex({ "mercadoId": 1 }, { name: "mercadoId_index" })
db.funcionarios.createIndex({ "ativo": 1 }, { name: "ativo_index" })

// ============================================================
// GERENTES - Índices
// ============================================================
db.gerentes.createIndex({ "email": 1 }, { unique: true, name: "email_unique" })
db.gerentes.createIndex({ "cpf": 1 }, { unique: true, name: "cpf_unique" })
db.gerentes.createIndex({ "cargos": 1 }, { name: "cargos_index" })
db.gerentes.createIndex({ "mercadoId": 1 }, { name: "mercadoId_index" })
db.gerentes.createIndex({ "ativo": 1 }, { name: "ativo_index" })

// ============================================================
// MERCADOS - Índices
// ============================================================
db.mercados.createIndex({ "nome": 1 }, { name: "nome_index" })
db.mercados.createIndex({ "cidade": 1 }, { name: "cidade_index" })

// ============================================================
// FORNECEDORES - Índices
// ============================================================
db.fornecedores.createIndex({ "email": 1 }, { unique: true, name: "email_unique" })
db.fornecedores.createIndex({ "cnpj": 1 }, { unique: true, name: "cnpj_unique" })
db.fornecedores.createIndex({ "nome": 1 }, { name: "nome_index" })

// ============================================================
// PRODUTOS - Índices
// ============================================================
db.produtos.createIndex({ "fornecedorId": 1 }, { name: "fornecedorId_index" })
db.produtos.createIndex({ "categoria": 1 }, { name: "categoria_index" })
db.produtos.createIndex({ "preco": 1 }, { name: "preco_index" })
db.produtos.createIndex({ "nome": 1 }, { name: "nome_index" })

// ============================================================
// VENDAS - Índices
// ============================================================
db.vendas.createIndex({ "produtoId": 1 }, { name: "produtoId_index" })
db.vendas.createIndex({ "mercadoId": 1 }, { name: "mercadoId_index" })
db.vendas.createIndex({ "funcionarioId": 1 }, { name: "funcionarioId_index" })
db.vendas.createIndex({ "dataVenda": 1 }, { name: "dataVenda_index" })
db.vendas.createIndex({ "categoria": 1 }, { name: "categoria_index" })
db.vendas.createIndex({ "numeroNota": 1 }, { unique: true, name: "numeroNota_unique" })

// ============================================================
// CONFIRMAÇÃO
// ============================================================
print("\n========== ÍNDICES CRIADOS COM SUCESSO ==========\n")
print("✅ usuarios - 4 índices")
print("✅ funcionarios - 5 índices")
print("✅ gerentes - 5 índices")
print("✅ mercados - 2 índices")
print("✅ fornecedores - 3 índices")
print("✅ produtos - 4 índices")
print("✅ vendas - 6 índices")
print("\nTotal: 29 índices criados!")
print("\n================================================\n")

