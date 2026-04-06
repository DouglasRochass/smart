# SmartStock - Frontend React

Um projeto frontend moderno desenvolvido em React com Vite para gerenciamento de estoque.

## 📋 Características

- ✅ Autenticação de usuários
- ✅ Gerenciamento de produtos
- ✅ Gestão de fornecedores
- ✅ Dashboard intuitivo
- ✅ Recuperação de senha
- ✅ Interface responsiva
- ✅ Validação de formulários
- ✅ Tratamento de erros

## 🛠️ Tecnologias Utilizadas

- **React 18** - Biblioteca UI
- **Vite** - Ferramentas de build
- **React Router v6** - Roteamento
- **Axios** - Cliente HTTP
- **CSS3** - Estilos

## 📦 Instalação

1. Clone o repositório:
```bash
git clone <seu-repositorio>
cd front-end
```

2. Instale as dependências:
```bash
npm install
```

3. Configure as variáveis de ambiente:
```bash
cp .env.example .env
```

4. Edite o arquivo `.env` com a URL da sua API:
```env
VITE_API_URL=http://localhost:8080/api
```

## 🚀 Desenvolvimento

Para iniciar o servidor de desenvolvimento:

```bash
npm run dev
```

O aplicativo estará disponível em `http://localhost:5173`

## 📦 Build para Produção

```bash
npm run build
```

## 📁 Estrutura do Projeto

```
src/
├── components/          # Componentes reutilizáveis
│   ├── Alert.jsx
│   ├── Loader.jsx
│   └── PrivateRoute.jsx
├── context/            # Context API
│   └── AuthContext.jsx
├── pages/             # Páginas do aplicativo
│   ├── Login.jsx
│   ├── Dashboard.jsx
│   ├── CadastroProdutos.jsx
│   └── RecuperarSenha.jsx
├── services/          # Serviços de API
│   ├── api.js
│   ├── authService.js
│   ├── produtoService.js
│   └── fornecedorService.js
├── styles/            # Estilos CSS
│   ├── Login.css
│   ├── Dashboard.css
│   ├── CadastroProdutos.css
│   ├── RecuperarSenha.css
│   ├── Alert.css
│   └── Loader.css
├── utils/             # Utilitários
│   └── validators.js
├── App.jsx
└── main.jsx
```

## 🔑 Funcionalidades Principais

### Autenticação
- Login com email e senha
- Recuperação de senha
- Redefinição de senha
- Token JWT armazenado no localStorage

### Gestão de Produtos
- Criar novo produto
- Listar todos os produtos
- Filtrar por categoria ou fornecedor
- Editar produto
- Deletar produto

### Dashboard
- Visualizar produtos cadastrados
- Estatísticas gerais
- Acesso rápido às principais funcionalidades

## 🔒 Segurança

- Autenticação baseada em JWT
- Rotas protegidas com PrivateRoute
- Validação de entrada em formulários
- Tratamento de erros

## 📝 Variáveis de Ambiente

| Variável | Descrição | Padrão |
|----------|-----------|--------|
| `VITE_API_URL` | URL base da API | `http://localhost:8080/api` |

## 📞 Suporte

Para dúvidas ou problemas, entre em contato com o time de desenvolvimento.

## 📄 Licença

Este projeto está sob a licença MIT.

## React Compiler

The React Compiler is not enabled on this template because of its impact on dev & build performances. To add it, see [this documentation](https://react.dev/learn/react-compiler/installation).

## Expanding the ESLint configuration

If you are developing a production application, we recommend using TypeScript with type-aware lint rules enabled. Check out the [TS template](https://github.com/vitejs/vite/tree/main/packages/create-vite/template-react-ts) for information on how to integrate TypeScript and [`typescript-eslint`](https://typescript-eslint.io) in your project.
