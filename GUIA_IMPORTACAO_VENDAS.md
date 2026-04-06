# 📥 Guia de Importação de Histórico de Vendas

## 📋 Visão Geral

Este guia descreve como usar a funcionalidade de importação de histórico de vendas do SmartStock. O sistema suporta importação de dados via:

- **CSV** (.csv)
- **EXCEL** (.xlsx)

---

## 🎯 Modelos Criados

### 1. **Venda**
Representa uma transação de venda no sistema.

**Campos:**
- `id`: ID único da venda (ObjectId)
- `produtoId`: ID do produto vendido
- `nomeProduto`: Nome do produto
- `quantidadeVendida`: Quantidade vendida
- `precoUnitario`: Preço por unidade
- `precoTotal`: Preço total (auto-calculado)
- `mercadoId`: ID do mercado onde foi vendido
- `nomeMercado`: Nome do mercado
- `funcionarioId`: ID do funcionário que realizou a venda
- `nomeFuncionario`: Nome do funcionário
- `dataVenda`: Data e hora da venda
- `categoria`: Categoria do produto
- `observacoes`: Observações adicionais
- `numeroNota`: Número da nota fiscal
- `ativo`: Status ativo/inativo

### 2. **Importacao**
Registra metadados de cada importação realizada.

**Campos:**
- `id`: ID único da importação
- `nomeArquivo`: Nome do arquivo importado
- `tipoArquivo`: CSV ou EXCEL
- `usuarioId`: ID do usuário que realizou a importação
- `nomeUsuario`: Nome do usuário
- `dataImportacao`: Quando foi importado
- `totalRegistros`: Total de linhas/registros
- `registrosComSucesso`: Quantos foram salvos
- `registrosComErro`: Quantos tiveram erro
- `status`: PENDENTE, PROCESSANDO, CONCLUIDO, ERRO
- `mensagemErro`: Detalhes do erro (se houver)
- `tempoProcessamento`: Tempo em segundos

---

## 🚀 Como Usar

### Passo 1: Preparar o Arquivo

#### Opção A: CSV

Crie um arquivo CSV com as seguintes colunas (nesta ordem):

```
produtoId,nomeProduto,quantidadeVendida,precoUnitario,mercadoId,nomeMercado,funcionarioId,nomeFuncionario,dataVenda,categoria,observacoes,numeroNota
```

**Exemplo:**
```csv
prod001,Arroz Integral 5kg,10,25.50,mer001,Mercado Centro,func001,João Silva,2026-03-20 10:30:00,Grãos,Venda normal,NF-001
prod002,Feijão Carioca 1kg,20,8.75,mer001,Mercado Centro,func002,Maria Santos,2026-03-20 11:15:00,Grãos,Oferta especial,NF-002
prod003,Suco Natural 1L,15,6.99,mer002,Mercado Vila,func001,João Silva,2026-03-20 12:00:00,Bebidas,,NF-003
```

#### Opção B: EXCEL (.xlsx)

Crie uma planilha Excel com as mesmas colunas:

| produtoId | nomeProduto | quantidadeVendida | precoUnitario | mercadoId | nomeMercado | funcionarioId | nomeFuncionario | dataVenda | categoria | observacoes | numeroNota |
|-----------|------------|-------------------|---------------|-----------|-------------|---------------|-----------------|-----------|-----------|-------------|-----------|
| prod001 | Arroz Integral 5kg | 10 | 25.50 | mer001 | Mercado Centro | func001 | João Silva | 2026-03-20 10:30:00 | Grãos | Venda normal | NF-001 |

**Dicas:**
- A primeira linha é o cabeçalho (será ignorada)
- Datas no formato: `YYYY-MM-DD HH:MM:SS` ou `DD/MM/YYYY HH:MM:SS`
- Números decimais usam ponto (`.`) como separador
- Campos vazios são permitidos (exceto os obrigatórios)

### Passo 2: Chamar a API

#### Usando curl (Terminal):

```bash
# Importar CSV
curl -X POST http://localhost:8080/api/importacao/csv \
  -F "file=@vendas.csv" \
  -F "usuarioId=user123" \
  -F "nomeUsuario=Admin User"

# Importar EXCEL
curl -X POST http://localhost:8080/api/importacao/excel \
  -F "file=@vendas.xlsx" \
  -F "usuarioId=user123" \
  -F "nomeUsuario=Admin User"
```

#### Usando Postman:

1. **Criar nova requisição POST**
   - URL: `http://localhost:8080/api/importacao/csv` (ou `/excel`)
   
2. **Abrir aba "Body"**
   - Selecionar: `form-data`
   
3. **Adicionar parâmetros:**
   - Key: `file` | Type: `File` | Value: (selecione o arquivo)
   - Key: `usuarioId` | Type: `Text` | Value: `user123`
   - Key: `nomeUsuario` | Type: `Text` | Value: `Seu Nome`
   
4. **Enviar (Send)**

#### Usando JavaScript (Frontend):

```javascript
const uploadFile = async () => {
  const fileInput = document.getElementById('fileInput');
  const file = fileInput.files[0];
  
  const formData = new FormData();
  formData.append('file', file);
  formData.append('usuarioId', 'user123');
  formData.append('nomeUsuario', 'Admin User');
  
  try {
    const response = await fetch('http://localhost:8080/api/importacao/csv', {
      method: 'POST',
      body: formData
    });
    
    const resultado = await response.json();
    console.log('Importação concluída:', resultado);
    console.log(`✓ ${resultado.registrosComSucesso} registros importados`);
    console.log(`✗ ${resultado.registrosComErro} registros com erro`);
  } catch (error) {
    console.error('Erro na importação:', error);
  }
};
```

---

## 📊 Resposta da API

A API retorna um objeto `ImportacaoResultadoDTO`:

```json
{
  "importacaoId": "60d5ec49c1c5e8a1b8c2e3f1",
  "nomeArquivo": "vendas.csv",
  "tipoArquivo": "CSV",
  "totalRegistros": 100,
  "registrosComSucesso": 98,
  "registrosComErro": 2,
  "status": "CONCLUIDO",
  "erros": [
    "Linha 15: Quantidade inválida",
    "Linha 42: Preço negativo"
  ],
  "tempoProcessamento": 2.345,
  "mensagem": "Importação concluída com sucesso"
}
```

---

## 🔍 Endpoints Disponíveis

### Importação

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| **POST** | `/api/importacao/csv` | Importar arquivo CSV |
| **POST** | `/api/importacao/excel` | Importar arquivo EXCEL |
| **GET** | `/api/importacao` | Listar todas as importações |
| **GET** | `/api/importacao/{id}` | Buscar importação por ID |
| **GET** | `/api/importacao/usuario/{usuarioId}` | Listar importações de um usuário |
| **GET** | `/api/importacao/status/{status}` | Listar por status (CONCLUIDO, ERRO, etc) |
| **GET** | `/api/importacao/stats` | Estatísticas gerais de importação |
| **DELETE** | `/api/importacao/{id}` | Deletar registro de importação |

### Vendas

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| **GET** | `/api/vendas` | Listar todas as vendas |
| **GET** | `/api/vendas/{id}` | Buscar venda por ID |
| **GET** | `/api/vendas/produto/{produtoId}` | Vendas de um produto |
| **GET** | `/api/vendas/mercado/{mercadoId}` | Vendas de um mercado |
| **GET** | `/api/vendas/funcionario/{funcionarioId}` | Vendas de um funcionário |
| **GET** | `/api/vendas/periodo` | Vendas por período |
| **POST** | `/api/vendas` | Criar venda manualmente |
| **PUT** | `/api/vendas/{id}` | Atualizar venda |
| **DELETE** | `/api/vendas/{id}` | Deletar venda |

---

## 📈 Exemplos de Uso

### Exemplo 1: Importar Vendas de Março

```bash
# Arquivo: vendas_marco_2026.csv
curl -X POST http://localhost:8080/api/importacao/csv \
  -F "file=@vendas_marco_2026.csv" \
  -F "usuarioId=user001" \
  -F "nomeUsuario=Gerente João"
```

### Exemplo 2: Listar Vendas de um Período

```bash
curl "http://localhost:8080/api/vendas/periodo?dataInicio=2026-03-01T00:00:00&dataFim=2026-03-31T23:59:59"
```

### Exemplo 3: Vendas por Mercado e Período

```bash
curl "http://localhost:8080/api/vendas/mercado/mer001/periodo?dataInicio=2026-03-01T00:00:00&dataFim=2026-03-31T23:59:59"
```

### Exemplo 4: Verificar Status de Importação

```bash
# Listar todas as importações concluídas
curl "http://localhost:8080/api/importacao/status/CONCLUIDO"

# Listar importações com erro
curl "http://localhost:8080/api/importacao/status/ERRO"
```

### Exemplo 5: Estatísticas

```bash
curl "http://localhost:8080/api/importacao/stats"

# Resposta:
# {
#   "totalImportacoes": 5,
#   "importacoesSucesso": 4,
#   "importacoesErro": 1,
#   "totalVendasImportadas": 450
# }
```

---

## ⚠️ Validações e Erros

### Erros Comuns

| Erro | Causa | Solução |
|------|-------|--------|
| "Arquivo vazio" | Arquivo não contém dados | Verifique se o arquivo possui linhas |
| "Arquivo muito grande" | Tamanho > 10MB | Divida o arquivo em partes menores |
| "Tipo de arquivo inválido" | Extensão errada | Verifique se é .csv ou .xlsx |
| "Linha N: Quantidade inválida" | Valor não é número | Verifique formatação dos dados |
| "Linha N: Preço negativo" | Preço < 0 | Preços devem ser positivos |

### Validações Obrigatórias

✓ **Campos obrigatórios:**
- `produtoId`
- `nomeProduto`
- `quantidadeVendida` (> 0)
- `precoUnitario` (> 0)
- `mercadoId`
- `nomeMercado`
- `funcionarioId`
- `nomeFuncionario`
- `dataVenda`

**Campos opcionais:**
- `categoria`
- `observacoes`
- `numeroNota`

---

## 📦 Dados de Teste

### Arquivo CSV de Exemplo

```csv
produtoId,nomeProduto,quantidadeVendida,precoUnitario,mercadoId,nomeMercado,funcionarioId,nomeFuncionario,dataVenda,categoria,observacoes,numeroNota
PROD001,Arroz Integral 5kg,10,25.50,MER001,Mercado Centro,FUNC001,João Silva,2026-03-20 08:00:00,Grãos,Estoque inicial,NF-2026-0001
PROD002,Feijão Carioca 1kg,25,8.75,MER001,Mercado Centro,FUNC002,Maria Santos,2026-03-20 09:30:00,Grãos,Reabastecimento,NF-2026-0002
PROD003,Suco Natural 1L,15,6.99,MER002,Mercado Vila,FUNC001,João Silva,2026-03-20 10:15:00,Bebidas,Promoção,NF-2026-0003
PROD004,Leite Integral 1L,30,4.50,MER001,Mercado Centro,FUNC003,Pedro Costa,2026-03-20 11:00:00,Lácteos,,NF-2026-0004
PROD005,Pão Francês kg,20,12.00,MER002,Mercado Vila,FUNC002,Maria Santos,2026-03-20 06:00:00,Padaria,Venda matinal,NF-2026-0005
```

### Estrutura de Collections MongoDB

```javascript
// Coleção: vendas
{
  _id: ObjectId("..."),
  produtoId: "PROD001",
  nomeProduto: "Arroz Integral 5kg",
  quantidadeVendida: 10,
  precoUnitario: 25.50,
  precoTotal: 255.00,
  mercadoId: "MER001",
  nomeMercado: "Mercado Centro",
  funcionarioId: "FUNC001",
  nomeFuncionario: "João Silva",
  dataVenda: ISODate("2026-03-20T08:00:00Z"),
  categoria: "Grãos",
  observacoes: "Estoque inicial",
  numeroNota: "NF-2026-0001",
  ativo: true,
  dataCriacao: ISODate("2026-03-20T...Z"),
  dataAtualizacao: ISODate("2026-03-20T...Z")
}

// Coleção: importacoes
{
  _id: ObjectId("..."),
  nomeArquivo: "vendas.csv",
  tipoArquivo: "CSV",
  usuarioId: "user123",
  nomeUsuario: "Admin User",
  dataImportacao: ISODate("2026-03-20T...Z"),
  totalRegistros: 100,
  registrosComSucesso: 98,
  registrosComErro: 2,
  status: "CONCLUIDO",
  mensagemErro: null,
  tempoProcessamento: 2.345,
  ativo: true
}
```

---

## 🔐 Considerações de Segurança

✅ **Implementado:**
- Validação de tamanho de arquivo (máx 10MB)
- Validação de tipo de arquivo
- Auditoria de importações (quem importou, quando)
- Tratamento de erros seguro

⚠️ **Próximos passos:**
- Autenticação obrigatória
- Autorização (apenas gerentes/master)
- Criptografia de dados sensíveis
- Backup automático de importações

---

## 📚 Próximas Funcionalidades

- [ ] Importação de produtos
- [ ] Importação de fornecedores
- [ ] Atualização em lote de preços
- [ ] Exportação de dados
- [ ] Agendamento de importações
- [ ] Histórico de alterações
- [ ] Rollback de importações

---

**Data:** 2026-03-20
**Versão:** 1.0.0
**Status:** ✅ Pronto para uso


