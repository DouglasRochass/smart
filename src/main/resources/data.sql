-- Seed de dados para SQLite com IDs inteiros auto-incremento
-- Limpa tabelas (ordem para FK simples)
DELETE FROM vendas;
DELETE FROM produtos;
DELETE FROM fornecedores;
DELETE FROM funcionarios;
DELETE FROM gerentes;
DELETE FROM usuarios;
DELETE FROM mercados;
DELETE FROM importacoes;

-- Mercados
INSERT INTO mercados (id, nome, endereco, telefone, cidade, estado) VALUES
 (1, 'Mercado Centro', 'Rua A, 123', '11999990001', 'São Paulo', 'SP'),
 (2, 'Mercado Norte', 'Av. B, 456', '11999990002', 'São Paulo', 'SP'),
 (3, 'Mercado Sul', 'Rua C, 789', '31999990003', 'Belo Horizonte', 'MG'),
 (4, 'Mercado Leste', 'Av. D, 111', '21999990004', 'Rio de Janeiro', 'RJ'),
 (5, 'Mercado Oeste', 'Rua E, 222', '85999990005', 'Curitiba', 'PR');

-- Fornecedores
INSERT INTO fornecedores (id, nome, telefone, email, endereco, cnpj) VALUES
 (1, 'Forn Center', '1122334455', 'contato@forncenter.com', 'Rua das Flores, 10', '12.345.678/0001-00'),
 (2, 'Forn Norte', '1199887766', 'vendas@fornnorte.com', 'Av. Central, 200', '23.456.789/0001-11'),
 (3, 'Forn Sul', '3199776655', 'suporte@fornsul.com', 'Praça Sul, 50', '34.567.890/0001-22'),
 (4, 'Forn Leste', '2133445566', 'info@fornleste.com', 'Rua Leste, 30', '45.678.901/0001-33');

-- Usuarios (cargos: 0=MASTER, 1=GERENTE, 2=FUNCIONARIO)
INSERT INTO usuarios (id, nome_colaborador, email, senha, cargos, mercado_id, ativo, cpf, tipo) VALUES
 (1, 'Alice Admin', 'alice.admin@example.com', 'senha123', 0, 1, 1, '00011122233', 'admin'),
 (2, 'Bob Operador', 'bob.operador@example.com', 'senha123', 2, 1, 1, '11122233344', 'operador'),
 (3, 'Clara Operadora', 'clara.operadora@example.com', 'senha123', 2, 2, 1, '22233344455', 'operador'),
 (4, 'Diego Supervisor', 'diego.supervisor@example.com', 'senha123', 1, 2, 1, '33344455566', 'gerente'),
 (5, 'Elaine Master', 'elaine.master@example.com', 'senha123', 0, 3, 1, '44455566677', 'admin'),
 (6, 'Fernando Func', 'fernando.func@example.com', 'senha123', 2, 3, 1, '55566677788', 'operador');

-- Funcionarios (cargos: 2=FUNCIONARIO)
INSERT INTO funcionarios (id, nome_colaborador, email, senha, cargos, mercado_id, ativo, cpf, idade) VALUES
 (1, 'Carlos Func', 'carlos.func@example.com', 'senha123', 2, 1, 1, '66677788899', 30),
 (2, 'Diana Func', 'diana.func@example.com', 'senha123', 2, 1, 1, '77788899900', 28),
 (3, 'Henrique Func', 'henrique.func@example.com', 'senha123', 2, 2, 1, '88899900011', 35),
 (4, 'Isabela Func', 'isabela.func@example.com', 'senha123', 2, 2, 1, '99900011122', 26),
 (5, 'Joana Func', 'joana.func@example.com', 'senha123', 2, 3, 1, '00011122244', 32),
 (6, 'Kaique Func', 'kaique.func@example.com', 'senha123', 2, 3, 1, '11122233355', 29),
 (7, 'Lucas Func', 'lucas.func@example.com', 'senha123', 2, 4, 1, '22233344566', 31),
 (8, 'Marina Func', 'marina.func@example.com', 'senha123', 2, 5, 1, '33344455677', 27);

-- Gerentes (cargos: 1=GERENTE)
INSERT INTO gerentes (id, nome_colaborador, email, senha, cargos, mercado_id, ativo, cpf, departamento) VALUES
 (1, 'Eva Gerente', 'eva.gerente@example.com', 'senha123', 1, 1, 1, '44455566788', 'Vendas'),
 (2, 'Felipe Gerente', 'felipe.gerente@example.com', 'senha123', 1, 1, 1, '55566677899', 'Operações'),
 (3, 'Gustavo Gerente', 'gustavo.gerente@example.com', 'senha123', 1, 2, 1, '66677788900', 'Logística'),
 (4, 'Helena Gerente', 'helena.gerente@example.com', 'senha123', 1, 3, 1, '77788899011', 'Compras'),
 (5, 'Igor Gerente', 'igor.gerente@example.com', 'senha123', 1, 4, 1, '88899900122', 'Estoque'),
 (6, 'Julia Gerente', 'julia.gerente@example.com', 'senha123', 1, 5, 1, '99900011233', 'RH');

-- Produtos
INSERT INTO produtos (id, nome, descricao, preco, quantidade, fornecedor_id, categoria) VALUES
 (1, 'Arroz 5kg', 'Arroz branco 5kg', 25.90, 200, 1, 'Alimentos'),
 (2, 'Feijão 1kg', 'Feijão carioca 1kg', 7.50, 300, 1, 'Alimentos'),
 (3, 'Macarrão 500g', 'Espaguete 500g', 4.80, 250, 2, 'Alimentos'),
 (4, 'Azeite 500ml', 'Azeite extra virgem', 18.00, 150, 2, 'Mercearia'),
 (5, 'Detergente 500ml', 'Detergente neutro', 2.80, 400, 3, 'Limpeza'),
 (6, 'Sabão em pó 1kg', 'Sabão em pó multiuso', 12.50, 180, 3, 'Limpeza'),
 (7, 'Óleo 1L', 'Óleo de soja 1L', 8.90, 220, 4, 'Mercearia'),
 (8, 'Café 500g', 'Café moído 500g', 14.50, 180, 4, 'Alimentos'),
 (9, 'Leite 1L', 'Leite integral 1L', 5.50, 400, 1, 'Laticínios'),
 (10, 'Pão integral 500g', 'Pão integral 500g', 6.80, 150, 2, 'Padaria');

-- Vendas
INSERT INTO vendas (id, produto_id, nome_produto, quantidade_vendida, preco_unitario, preco_total, mercado_id, nome_mercado, funcionario_id, nome_funcionario, data_venda, categoria, observacoes, numero_nota, ativo, data_criacao, data_atualizacao) VALUES
 (1, 1, 'Arroz 5kg', 2, 25.90, 51.80, 1, 'Mercado Centro', 1, 'Carlos Func', CURRENT_TIMESTAMP, 'Alimentos', 'Venda balcão', 'NF-1001', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
 (2, 2, 'Feijão 1kg', 3, 7.50, 22.50, 1, 'Mercado Centro', 2, 'Diana Func', CURRENT_TIMESTAMP, 'Alimentos', 'Venda app', 'NF-1002', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
 (3, 5, 'Detergente 500ml', 5, 2.80, 14.00, 2, 'Mercado Norte', 3, 'Henrique Func', CURRENT_TIMESTAMP, 'Limpeza', 'Venda balcão', 'NF-2001', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
 (4, 6, 'Sabão em pó 1kg', 4, 12.50, 50.00, 3, 'Mercado Sul', 5, 'Joana Func', CURRENT_TIMESTAMP, 'Limpeza', 'Venda delivery', 'NF-3001', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
 (5, 4, 'Azeite 500ml', 2, 18.00, 36.00, 1, 'Mercado Centro', 1, 'Carlos Func', CURRENT_TIMESTAMP, 'Mercearia', 'Venda boleto', 'NF-1003', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
 (6, 9, 'Leite 1L', 10, 5.50, 55.00, 2, 'Mercado Norte', 4, 'Isabela Func', CURRENT_TIMESTAMP, 'Laticínios', 'Venda pix', 'NF-2002', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
 (7, 8, 'Café 500g', 3, 14.50, 43.50, 4, 'Mercado Leste', 7, 'Lucas Func', CURRENT_TIMESTAMP, 'Alimentos', 'Venda dinheiro', 'NF-4001', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
 (8, 10, 'Pão integral 500g', 6, 6.80, 40.80, 5, 'Mercado Oeste', 8, 'Marina Func', CURRENT_TIMESTAMP, 'Padaria', 'Venda manhã', 'NF-5001', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
 (9, 3, 'Macarrão 500g', 5, 4.80, 24.00, 1, 'Mercado Centro', 2, 'Diana Func', CURRENT_TIMESTAMP, 'Alimentos', 'Promoção', 'NF-1004', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
 (10, 7, 'Óleo 1L', 2, 8.90, 17.80, 3, 'Mercado Sul', 6, 'Kaique Func', CURRENT_TIMESTAMP, 'Mercearia', 'Venda online', 'NF-3002', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

