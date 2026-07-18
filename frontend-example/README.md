# MatriculaFácil — Frontend

Interface web do sistema de matrícula desenvolvida como projeto de treinamento da **CATI Jr.** (Trainee 2026).

## Sobre o projeto

Aplicação React que integra com a API real do backend (Spring Boot): login, cadastro, catálogo de disciplinas, inscrição/cancelamento de matrícula, histórico em "Minhas Matérias" e edição de perfil.

## Tecnologias

| Camada | Tecnologia |
|---|---|
| Framework | React 18 + TypeScript |
| Build | Vite 5 |
| Estilização | Tailwind CSS 3 |
| HTTP | Axios |

## Pré-requisitos

- Node.js 20+
- npm 10+

## Como rodar localmente

```bash
# Instalar dependências
npm install

# Configurar a URL da API (arquivo .env, chave VITE_API_URL)

# Iniciar servidor de desenvolvimento
npm run dev
```

O servidor sobe em `http://localhost:5173`. A API precisa estar rodando (ver README do backend).

## Scripts disponíveis

| Comando | Descrição |
|---|---|
| `npm run dev` | Servidor de desenvolvimento com hot reload |
| `npm run build` | Build de produção (saída em `dist/`) |
| `npm run preview` | Pré-visualização do build de produção |

## Estrutura do projeto

```
src/
├── assets/         # Ícones SVG como componentes React
├── components/     # Componentes reutilizáveis (cards, formulários, header, modal de detalhes)
├── pages/          # LoginPage, SignupPage, DashboardPage
├── services/       # api.ts — instância do axios (token, erro 401)
├── types/          # index.ts — tipos batendo com os DTOs do backend
├── App.tsx         # Componente raiz e roteamento por estado
└── main.tsx        # Entry point do React
```

## Páginas / funcionalidades

- **Login / Cadastro** — autenticação real via API
- **Catálogo** — disciplinas em cards, com motor de validação (pré-requisito, conflito de horário, limite de créditos) e inscrição
- **Minhas Matérias** — histórico de matrículas com cancelamento e filtro por semestre/ano
- **Meu Perfil** — edição de e-mail e troca de senha
