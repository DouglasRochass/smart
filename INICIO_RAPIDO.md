# 🚀 Guia de Início Rápido

## Pré-requisitos
- Node.js 16+ instalado
- npm ou yarn
- API backend rodando em `http://localhost:8080/api`

## Passos para começar

### 1️⃣ Configurar Variáveis de Ambiente
```bash
# Abra o arquivo .env
# VITE_API_URL=http://localhost:8080/api
```

### 2️⃣ Iniciar o Servidor de Desenvolvimento
```bash
npm run dev
```

O servidor iniciará em: **http://localhost:5173**

### 3️⃣ Fazer Login
- Email: seu email registrado
- Senha: sua senha

### 4️⃣ Explorar o Dashboard
- Visualizar produtos
- Cadastrar novos produtos
- Gerenciar fornecedores

## Scripts Disponíveis

| Comando | Descrição |
|---------|-----------|
| `npm run dev` | Inicia servidor de desenvolvimento com HMR |
| `npm run build` | Faz build para produção |
| `npm run preview` | Preview do build |
| `npm run lint` | Verifica o código com ESLint |

## Estrutura de Pastas

```
src/
├── components/     → Componentes reutilizáveis (Alert, Loader, PrivateRoute)
├── context/        → Context API (Autenticação)
├── pages/          → Páginas da aplicação (Login, Dashboard, etc)
├── services/       → Serviços de API e autenticação
├── styles/         → Arquivos CSS organizados por página
├── utils/          → Funções utilitárias e validações
└── main.jsx        → Ponto de entrada
```

## Fluxo de Login

1. Usuário acessa a aplicação
2. É redirecionado para a tela de login se não autenticado
3. Insere email e senha
4. Sistema faz requisição `POST /login`
5. Se sucesso, token é salvo no localStorage
6. Usuário é redirecionado para o Dashboard

## Conexão com API

A aplicação faz requisições para:
- `POST /login` - Autenticação
- `GET /produtos` - Listar produtos
- `POST /produtos` - Criar produto
- `GET /fornecedores` - Listar fornecedores
- E muitos outros...

## Troubleshooting

### Erro: "Não consegue conectar à API"
- Verifique se a API está rodando
- Confira a URL no arquivo `.env`
- Teste a URL em um navegador: `http://localhost:8080/api/health`

### Erro: "Token inválido"
- Limpe o localStorage: `localStorage.clear()`
- Faça login novamente

### Erro: "Componente não encontrado"
- Verifique se o arquivo está na pasta correta
- Verifique os imports (sensível a maiúsculas/minúsculas)

## Customização

### Alterar Cores
Edite as cores principais em `src/styles/`:
- Laranja: `#FF8C00`
- Secundária: `#F4A460`
- Azul: `#00BFFF`

### Adicionar Nova Página
1. Crie o arquivo em `src/pages/NovaPage.jsx`
2. Importe em `src/App.jsx`
3. Adicione a rota no `<Routes>`

### Adicionar Novo Serviço
1. Crie o arquivo em `src/services/novoService.js`
2. Exporte as funções
3. Importe e use nas páginas

## Deploy

### Para Vercel
```bash
npm run build
```
Faça upload da pasta `dist/` para Vercel

### Para outras plataformas
```bash
npm run build
# Hospede a pasta dist/
```

---

**Boa sorte! 🎉**

