# MatriculaFácil — Backend

API do sistema de matrícula desenvolvida como projeto de treinamento da **CATI Jr.** (Trainee 2026).

## Sobre o projeto

API REST em Spring Boot com autenticação via JWT: cadastro/login de usuário, catálogo de disciplinas (com validação de pré-requisito, vagas, conflito de horário e limite de créditos), inscrição e cancelamento de matrícula, e edição de perfil.

## Tecnologias

| Camada | Tecnologia |
|---|---|
| Framework | Spring Boot 4 (Java 21) |
| Persistência | Spring Data JPA + PostgreSQL |
| Autenticação | Spring Security + JWT (java-jwt) |
| Validação | Bean Validation |
| Documentação | springdoc-openapi (Swagger UI) |
| Testes | JUnit + Mockito |

## Pré-requisitos

- Java 21
- Docker (para o PostgreSQL)

## Como rodar localmente

```bash
# 1. Subir o banco de dados
docker compose up -d postgres

# 2. Rodar a aplicação
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`. O frontend (ver README na pasta `frontend-example/`) precisa dela rodando pra funcionar.

Na primeira execução, um `CommandLineRunner` popula o banco com disciplinas e um usuário de teste:
- email: `aluno@matriculafacil.com`
- senha: `senha123`

## Documentação da API

Com a aplicação rodando: `http://localhost:8080/swagger-ui/index.html`

## Endpoints principais

Todos sob o prefixo `/MatriculaFacil`, exceto `/cadastro` e `/login` os demais exigem `Authorization: Bearer <token>`.

| Método | Rota | Descrição |
|---|---|---|
| POST | `/cadastro` | Cria um novo usuário |
| POST | `/login` | Autentica e retorna o token JWT |
| GET | `/user/me` | Dados do perfil do usuário logado |
| PUT | `/user/me` | Atualiza e-mail e/ou senha |
| GET | `/disciplina` | Lista o catálogo de disciplinas |
| GET | `/disciplina/{id}` | Detalhes de uma disciplina |
| POST | `/matricula` | Inscreve o usuário em uma disciplina |
| GET | `/matricula` | Lista as matrículas do usuário |
| DELETE | `/matriculas/{id}` | Cancela uma matrícula |

## Testes

```bash
./mvnw test
```

## Estrutura do projeto

```
src/main/java/com/catijr/backend/
├── config/         # Segurança, filtro JWT, seed de dados iniciais
├── controller/      # Endpoints REST
├── service/         # Regras de negócio
├── repository/       # Interfaces Spring Data JPA
├── model/           # Entidades JPA
├── dto/             # Objetos de requisição/resposta
└── exception/        # Exceções customizadas e handler global
```
