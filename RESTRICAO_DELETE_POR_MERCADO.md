# 🔐 Restrição de Deletar Funcionários por Mercado

## Descrição
Somente Gerentes podem excluir funcionários, e apenas do mercado onde eles são gerentes.
Masters podem excluir de qualquer mercado.
Funcionários não podem excluir ninguém.

## Matriz de Permissões

| Quem | Pode Deletar | Restrição |
|-----|-------------|-----------|
| Funcionário | ❌ Não | N/A |
| Gerente | ✅ Sim | Apenas do SEU mercado |
| Master | ✅ Sim | De qualquer mercado |

## Exemplos de Uso

### Caso 1: Gerente tenta deletar funcionário do seu mercado (SUCESSO)
```bash
curl -X DELETE http://localhost:8080/api/funcionarios/5 \
  -H "Content-Type: application/json" \
  -H "gerente-cargo: GERENTE" \
  -H "usuario-mercado-id: 1"
# Funcionário #5 é do mercado 1
# Resposta: 204 No Content
```

### Caso 2: Gerente tenta deletar funcionário de outro mercado (FALHA)
```bash
curl -X DELETE http://localhost:8080/api/funcionarios/10 \
  -H "Content-Type: application/json" \
  -H "gerente-cargo: GERENTE" \
  -H "usuario-mercado-id: 1"
# Funcionário #10 é do mercado 2
# Resposta: 403 Forbidden
# Mensagem: "Gerente só pode deletar funcionários do seu próprio mercado (Mercado 1). Este funcionário é do Mercado 2"
```

### Caso 3: Gerente tenta deletar sem fornecer seu mercado (FALHA)
```bash
curl -X DELETE http://localhost:8080/api/funcionarios/5 \
  -H "Content-Type: application/json" \
  -H "gerente-cargo: GERENTE"
# Header usuario-mercado-id está faltando
# Resposta: 400 Bad Request
# Mensagem: "Gerentes devem fornecer usuario-mercado-id"
```

### Caso 4: Funcionário tenta deletar outro funcionário (FALHA)
```bash
curl -X DELETE http://localhost:8080/api/funcionarios/5 \
  -H "Content-Type: application/json" \
  -H "gerente-cargo: FUNCIONARIO" \
  -H "usuario-mercado-id: 1"
# Resposta: 403 Forbidden
# Mensagem: "Funcionários não podem deletar outros funcionários"
```

### Caso 5: Master deleta funcionário de qualquer mercado (SUCESSO)
```bash
curl -X DELETE http://localhost:8080/api/funcionarios/10 \
  -H "Content-Type: application/json" \
  -H "gerente-cargo: MASTER"
# Master pode deletar de qualquer lugar
# Resposta: 204 No Content
```

### Caso 6: Tentar deletar funcionário inexistente (FALHA)
```bash
curl -X DELETE http://localhost:8080/api/funcionarios/999 \
  -H "Content-Type: application/json" \
  -H "gerente-cargo: GERENTE" \
  -H "usuario-mercado-id: 1"
# Resposta: 404 Not Found
```

## Validações Implementadas

1. ✅ Verifica se o usuário é Funcionário → 403 Forbidden
2. ✅ Verifica se o usuário é Gerente e forneceu `usuario-mercado-id` → 400 Bad Request se não
3. ✅ Verifica se o funcionário a ser deletado está no mesmo mercado → 403 Forbidden se não
4. ✅ Verifica se o funcionário existe → 404 Not Found se não
5. ✅ Master pode deletar sem restrição de mercado

## Fluxo de Validação

```
DELETE /api/funcionarios/{id}
  ↓
1. Encontrar funcionário no BD
   └─ Não encontrado? → 404 Not Found
  ↓
2. Verificar cargo do usuário (gerente-cargo)
   ├─ FUNCIONARIO? → 403 Forbidden
   ├─ GERENTE?
   │  ├─ usuario-mercado-id fornecido?
   │  │  └─ Não → 400 Bad Request
   │  └─ Funcionário está no mercado do gerente?
   │     └─ Não → 403 Forbidden
   └─ MASTER? → Sem restrição
  ↓
3. Deletar funcionário
   └─ 204 No Content
```

## Headers Necessários

### Para Gerente (OBRIGATÓRIO)
```
gerente-cargo: GERENTE
usuario-mercado-id: {id-do-mercado-do-gerente}
```

### Para Master (OPCIONAL)
```
gerente-cargo: MASTER
# usuario-mercado-id é opcional
```

## Dados de Teste

Supondo os seguintes dados no BD:

| ID | Nome | Mercado | Email |
|----|------|---------|-------|
| 1 | João | 1 | joao@example.com |
| 2 | Maria | 1 | maria@example.com |
| 3 | Pedro | 2 | pedro@example.com |
| 4 | Ana | 2 | ana@example.com |

Gerente do Mercado 1 pode deletar: #1, #2
Gerente do Mercado 1 NÃO pode deletar: #3, #4
Gerente do Mercado 2 pode deletar: #3, #4
Gerente do Mercado 2 NÃO pode deletar: #1, #2
Master pode deletar: #1, #2, #3, #4

## Resposta HTTP Detalhada

### 204 No Content (Sucesso)
```
HTTP/1.1 204 No Content
```

### 400 Bad Request (Falta Header)
```
HTTP/1.1 400 Bad Request
Content-Type: application/json

"Gerentes devem fornecer usuario-mercado-id"
```

### 403 Forbidden (Permissão Negada)
```
HTTP/1.1 403 Forbidden
Content-Type: application/json

"Gerente só pode deletar funcionários do seu próprio mercado (Mercado 1). Este funcionário é do Mercado 2"
```

```
HTTP/1.1 403 Forbidden
Content-Type: application/json

"Funcionários não podem deletar outros funcionários"
```

### 404 Not Found (Não Encontrado)
```
HTTP/1.1 404 Not Found
```

## Relacionamento com Outras Restrições

Esta restrição funciona em conjunto com:

1. **Um funcionário por mercado** - Cada funcionário tem um mercado específico
2. **Gerentes controlam seu mercado** - Garante que gerentes só trabalham com seus dados
3. **Segregação de dados** - Impede que gerentes vejam/manipulem funcionários de outros mercados

## Notas Importantes

- ✅ Gerentes devem sempre fornecer seu `usuario-mercado-id` ao deletar
- ✅ Masters NÃO precisam fornecer `usuario-mercado-id` para deletar
- ✅ Se o funcionário não existe, retorna 404 antes de validar permissões
- ✅ Funcionários nunca podem deletar ninguém, independente de qual mercado

