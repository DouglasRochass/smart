# 📚 Documentação de Serviços e API

## Serviços Disponíveis

### 🔐 Auth Service (`authService.js`)

#### `login(email, senha)`
Realiza login do usuário.
```javascript
const response = await authService.login('user@example.com', 'senha123');
// Retorna: { sucesso: true, token, ... }
```

#### `logout()`
Faz logout do usuário.
```javascript
await authService.logout();
// Remove token do localStorage
```

#### `recuperarSenha(email)`
Solicita recuperação de senha.
```javascript
const response = await authService.recuperarSenha('user@example.com');
// Envia email com instruções
```

#### `redefinirSenha(token, novaSenha, confirmacaoSenha)`
Redefine a senha do usuário.
```javascript
const response = await authService.redefinirSenha(token, 'nova123', 'nova123');
```

#### `isAuthenticated()`
Verifica se há token armazenado.
```javascript
const logado = authService.isAuthenticated(); // true ou false
```

---

### 📦 Produto Service (`produtoService.js`)

#### `obterProdutos()`
Lista todos os produtos.
```javascript
const response = await produtoService.obterProdutos();
// Retorna: { sucesso: true, dados: [...] }
```

#### `obterProdutoPorId(id)`
Obtém um produto específico.
```javascript
const response = await produtoService.obterProdutoPorId(1);
// Retorna: { sucesso: true, dados: {...} }
```

#### `criarProduto(dados)`
Cria um novo produto.
```javascript
const dados = {
  nome: 'Produto X',
  descricao: 'Descrição do produto',
  categoria: 'Alimentos',
  fornecedorId: 1,
  quantidade: 100,
  preco: 25.50
};
const response = await produtoService.criarProduto(dados);
```

#### `atualizarProduto(id, dados)`
Atualiza um produto existente.
```javascript
const response = await produtoService.atualizarProduto(1, dadosAtualizados);
```

#### `deletarProduto(id)`
Deleta um produto.
```javascript
const response = await produtoService.deletarProduto(1);
// Retorna: { sucesso: true }
```

#### `obterProdutosPorCategoria(categoria)`
Filtra produtos por categoria.
```javascript
const response = await produtoService.obterProdutosPorCategoria('Alimentos');
```

#### `obterProdutosPorFornecedor(fornecedorId)`
Filtra produtos por fornecedor.
```javascript
const response = await produtoService.obterProdutosPorFornecedor(1);
```

---

### 🏢 Fornecedor Service (`fornecedorService.js`)

#### `obterFornecedores()`
Lista todos os fornecedores.
```javascript
const response = await fornecedorService.obterFornecedores();
// Retorna: { sucesso: true, dados: [...] }
```

#### `obterFornecedorPorId(id)`
Obtém um fornecedor específico.
```javascript
const response = await fornecedorService.obterFornecedorPorId(1);
```

#### `criarFornecedor(dados)`
Cria um novo fornecedor.
```javascript
const dados = {
  nome: 'Fornecedor X',
  email: 'fornecedor@email.com',
  telefone: '1122334455',
  endereco: 'Rua X, 123',
  cnpj: '12.345.678/0001-00'
};
const response = await fornecedorService.criarFornecedor(dados);
```

#### `atualizarFornecedor(id, dados)`
Atualiza um fornecedor.
```javascript
const response = await fornecedorService.atualizarFornecedor(1, dadosAtualizados);
```

#### `deletarFornecedor(id)`
Deleta um fornecedor.
```javascript
const response = await fornecedorService.deletarFornecedor(1);
```

---

## 📋 Hooks Customizados

### `useAuth()`
Hook para acessar dados de autenticação.
```javascript
import { useAuth } from '../context/AuthContext';

function MeuComponente() {
  const { user, isLoading, isAuthenticated, login, logout } = useAuth();
  
  return (
    <div>
      {isAuthenticated ? <p>Logado</p> : <p>Não logado</p>}
    </div>
  );
}
```

---

## 🛠️ Funções Utilitárias (`utils/validators.js`)

### Validações

#### `validarEmail(email)`
```javascript
validarEmail('user@example.com'); // true
validarEmail('invalid'); // false
```

#### `validarCPF(cpf)`
```javascript
validarCPF('123.456.789-10'); // true ou false
```

### Formatações

#### `formatarMoeda(valor)`
```javascript
formatarMoeda(100.50); // "R$ 100,50"
```

#### `formatarData(data, formato)`
```javascript
formatarData(new Date(), 'DD/MM/YYYY'); // "06/04/2026"
formatarData(new Date(), 'DD/MM/YYYY HH:mm'); // "06/04/2026 14:30"
```

#### `formatarCPF(cpf)`
```javascript
formatarCPF('12345678910'); // "123.456.789-10"
```

#### `formatarTelefone(telefone)`
```javascript
formatarTelefone('11999999999'); // "(11) 99999-9999"
```

#### `formatarCEP(cep)`
```javascript
formatarCEP('01310100'); // "01310-100"
```

### Armazenamento Local

#### `armazenarLocal(chave, valor)`
```javascript
armazenarLocal('meu_dado', { nome: 'João' });
```

#### `recuperarLocal(chave)`
```javascript
const dados = recuperarLocal('meu_dado');
```

#### `removerLocal(chave)`
```javascript
removerLocal('meu_dado');
```

#### `limparLocal()`
```javascript
limparLocal(); // Limpa todo o localStorage
```

### Otimização

#### `debounce(funcao, delay)`
```javascript
const buscar = debounce((termo) => {
  console.log('Buscando:', termo);
}, 300);

// Será chamada apenas se houver pausa de 300ms
input.addEventListener('input', (e) => buscar(e.target.value));
```

#### `throttle(funcao, limit)`
```javascript
const scroll = throttle(() => {
  console.log('Scrollando');
}, 300);

window.addEventListener('scroll', scroll);
```

---

## 🔄 Fluxo de Requisições

Todas as requisições passam por interceptadores:

```
1. Request Interceptor
   ↓
2. Adiciona token de autenticação
   ↓
3. Envia requisição
   ↓
4. Response Interceptor
   ↓
5. Se status 401 → Redireciona para login
   ↓
6. Retorna resposta processada
```

---

## ⚠️ Tratamento de Erros

Todos os serviços retornam um objeto com:
```javascript
{
  sucesso: boolean,
  dados?: object,
  mensagem?: string,
  codigo?: number
}
```

Exemplo:
```javascript
const response = await produtoService.obterProdutos();

if (response.sucesso) {
  console.log('Produtos:', response.dados);
} else {
  console.error('Erro:', response.mensagem);
}
```

---

## 🔐 Autenticação

### Armazenamento de Token
O token é armazenado em `localStorage` sob a chave `auth_token`.

### Renovação de Token
Quando receber erro 401, o token é automaticamente removido e o usuário é redirecionado para login.

### Headers Automáticos
O Axios adiciona automaticamente o header:
```
Authorization: Bearer <token>
```

---

## 📞 Exemplo de Uso Completo

```javascript
import { useState, useEffect } from 'react';
import produtoService from '../services/produtoService';
import Alert from '../components/Alert';

function MeusProdutos() {
  const [produtos, setProdutos] = useState([]);
  const [loading, setLoading] = useState(false);
  const [erro, setErro] = useState(null);

  useEffect(() => {
    carregarProdutos();
  }, []);

  async function carregarProdutos() {
    setLoading(true);
    setErro(null);
    
    const response = await produtoService.obterProdutos();
    
    if (response.sucesso) {
      setProdutos(response.dados);
    } else {
      setErro(response.mensagem);
    }
    
    setLoading(false);
  }

  return (
    <div>
      {erro && <Alert type="erro" message={erro} />}
      {loading && <p>Carregando...</p>}
      
      {produtos.map(produto => (
        <div key={produto.id}>
          <h3>{produto.nome}</h3>
          <p>{produto.descricao}</p>
          <p>R$ {produto.preco}</p>
        </div>
      ))}
    </div>
  );
}

export default MeusProdutos;
```

---

**Documentação atualizada em 06/04/2026** ✅

