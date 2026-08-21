# API Specification

## Objetivo

Este documento define o contrato oficial da API REST do StockTrack.

Nenhum endpoint poderá ser implementado sem estar previamente documentado.

---

# Padrão REST

Todos os endpoints seguirão os princípios RESTful.

Métodos HTTP utilizados: GET, POST, PUT, PATCH, DELETE.

---

# Base URL

```
/api/v1
```

---

# Autenticação

## Login

POST

```
/auth/login
```

Descrição: realiza autenticação do usuário via RE + senha.

Resposta: JWT Access Token.

## Cadastro (Colaborador)

POST /auth/register
Descrição: autocadastro público. Cria usuário com role COLLABORATOR.
Corpo: name, re, area, password.

Resposta: 201 Created, com o usuário criado e o token JWT (login automático após o cadastro).

---

# Usuários

## Criar usuário

POST

```
/users
```

Acesso: exclusivo WAREHOUSE_MANAGER.

## Criar usuário (administrativo)

POST /users
Acesso: exclusivo WAREHOUSE_MANAGER.
Cria exclusivamente um novo WAREHOUSE_MANAGER. A role não é informada no corpo da
requisição — é sempre WAREHOUSE_MANAGER. Corpo: name, re, area, password.

## Atualizar dados do usuário autenticado

PUT /users/me
Acesso: qualquer usuário autenticado.
Campos editáveis: name, area, password (com confirmação da senha atual).
`re` não pode ser alterado.

## Listar usuários

GET

```
/users
```

Acesso: exclusivo WAREHOUSE_MANAGER.

## Buscar usuário autenticado

GET

```
/users/me
```

Acesso: qualquer usuário autenticado.

---

# Categorias

## Listar categorias

GET

```
/categories
```

Acesso: qualquer usuário autenticado.

## Buscar categoria

GET

```
/categories/{id}
```

## Criar categoria

POST

```
/categories
```

Acesso: exclusivo WAREHOUSE_MANAGER.

## Atualizar categoria

PUT

```
/categories/{id}
```

Acesso: exclusivo WAREHOUSE_MANAGER.

## Excluir categoria

DELETE

```
/categories/{id}
```

Acesso: exclusivo WAREHOUSE_MANAGER.

---

# Tipos de Ferramenta

## Listar tipos de ferramenta

GET

```
/tool-types
```

Filtros: `category`, `requiresCalibration`.

Para ferramentas cujo Tipo de Ferramenta exija calibração, retorna também `calibrationOverdue`
(booleano, calculado dinamicamente).

## Buscar tipo de ferramenta

GET

```
/tool-types/{id}
```

## Criar tipo de ferramenta

POST

```
/tool-types
```

Acesso: exclusivo WAREHOUSE_MANAGER.

## Atualizar tipo de ferramenta

PUT

```
/tool-types/{id}
```

Acesso: exclusivo WAREHOUSE_MANAGER.

## Excluir tipo de ferramenta

DELETE

```
/tool-types/{id}
```

Acesso: exclusivo WAREHOUSE_MANAGER.

Permitido apenas se não existir nenhuma unidade (Tool) vinculada a este tipo (ver
`03-Business-Rules.md`, seção Tipo de Ferramenta > Exclusão).

## Alterar status

PATCH

```
/tools/{id}/status
```

Acesso: exclusivo WAREHOUSE_MANAGER.

Permite as transições: AVAILABLE ↔ IN_MAINTENANCE, e qualquer status → DISCARDED (irreversível, conforme `03-Business-Rules.md`).

Este é o único endpoint responsável por qualquer mudança de status, incluindo o descarte — não existe endpoint de exclusão (`DELETE`) para Ferramentas, já que nenhuma unidade é removida fisicamente do sistema.

---

# Localizações

## Listar localizações

GET

```
/locations
```

## Buscar localização

GET

```
/locations/{id}
```

## Criar localização

POST

```
/locations
```

Acesso: exclusivo WAREHOUSE_MANAGER.

## Atualizar localização

PUT

```
/locations/{id}
```

Acesso: exclusivo WAREHOUSE_MANAGER.

## Excluir localização

DELETE

```
/locations/{id}
```

Acesso: exclusivo WAREHOUSE_MANAGER.

---

# Ferramentas

## Listar ferramentas

GET

```
/tools
```

Filtros: `name` (busca por trecho do nome do Tipo de Ferramenta associado), `category`, `status`, `location`, `toolTypeId`.

Retorna, para cada ferramenta, seu status atual e, quando aplicável, a previsão de devolução (se estiver em uso) — conforme `03-Business-Rules.md`, seção Consulta de Status.

## Buscar ferramenta

GET

```
/tools/{id}
```

Também aceita busca por número de série:

GET

```
/tools/search?serialNumber={serialNumber}
```

## Criar ferramenta(s)

POST

```
/tools
```

Acesso: exclusivo WAREHOUSE_MANAGER.

Cria uma única unidade por requisição. Cadastro em lote adiado para versão futura.

## Atualizar dados da ferramenta

PUT

```
/tools/{id}
```

Acesso: exclusivo WAREHOUSE_MANAGER.

## Alterar status (manutenção)

PATCH

```
/tools/{id}/status
```

Acesso: exclusivo WAREHOUSE_MANAGER.

Permite transições: AVAILABLE ↔ IN_MAINTENANCE, e qualquer status → DISCARDED (irreversível).

## Registrar calibração

PATCH

```
/tools/{id}/calibration
```

Acesso: exclusivo WAREHOUSE_MANAGER.

Registra a data de calibração realizada; o sistema recalcula automaticamente a próxima data de calibração prevista.

## Descartar ferramenta

DELETE

```
/tools/{id}
```

Acesso: exclusivo WAREHOUSE_MANAGER.

Não remove o registro fisicamente — altera o status para DISCARDED (ver `03-Business-Rules.md`, seção Ferramenta — Exclusão/Descarte).

---

# Empréstimos

## Emprestar ferramenta

POST

```
/loans
```

Acesso: qualquer usuário autenticado (empréstimo sempre para si mesmo).

Corpo: `toolId`, `reason` (obrigatório), `expectedReturnDate` (opcional).

## Devolver ferramenta

PATCH

```
/loans/{id}/return
```

Acesso: qualquer usuário autenticado.

Corpo: `observation` (opcional).

## Buscar empréstimo

GET

```
/loans/{id}
```

Acesso: WAREHOUSE_MANAGER vê qualquer empréstimo; COLLABORATOR vê apenas os próprios.

## Listar empréstimos

GET

```
/loans
```

Filtros: `toolId`, `userId`, `status` (ABERTO / DEVOLVIDO / ATRASADO — calculado dinamicamente).

Acesso: WAREHOUSE_MANAGER vê todos; COLLABORATOR vê apenas os próprios.

---

# Log de Auditoria

## Listar registros de auditoria

GET

```
/audit-logs
```

Filtros: `toolId`, `userId`, `actionType`.

Acesso: exclusivo WAREHOUSE_MANAGER.

Não existe endpoint de criação, atualização ou exclusão manual — todos os registros são gerados automaticamente pelo sistema através de eventos de domínio (ver `02-Software-Architecture.md`, seção Registro de Auditoria via Eventos).

---

# HTTP Status

## Sucesso

200 OK, 201 Created, 204 No Content

## Cliente

400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found, 409 Conflict, 422 Unprocessable Entity

## Servidor

500 Internal Server Error

---

# Versionamento

Toda API utilizará `/api/v1`. Novas versões seguirão `/api/v2`.

---

# Paginação

Endpoints de listagem com potencial de grande volume (`/tools`, `/loans`, `/audit-logs`, `/users`)
utilizarão paginação:

```
?page=0&size=10&sort=createdAt,desc
```

Endpoints de catálogo com volume tipicamente pequeno (`/categories`, `/locations`) seguem sem paginação na V1 — mesma decisão consciente já validada em projeto anterior do autor para entidades de baixo volume esperado.

---

# Segurança

Todos os endpoints, exceto `/auth/login`, exigirão JWT válido.

Endpoints administrativos exigem, além do JWT válido, o papel WAREHOUSE_MANAGER — validado via Spring Security.

---

# Padronização de Respostas

Exemplo de erro:

```json
{
  "timestamp": "...",
  "status": 404,
  "error": "Not Found",
  "message": "...",
  "path": "..."
}
```

Exemplo de erro de validação (múltiplos campos):

```json
{
  "timestamp": "2026-08-06T10:00:00",
  "status": 422,
  "error": "Unprocessable Entity",
  "message": "Erro de validação nos dados enviados",
  "errors": [
    { "field": "reason", "message": "Motivo do empréstimo é obrigatório" }
  ],
  "path": "/api/v1/loans"
}
```

---

# OpenAPI

Toda a documentação da API será gerada automaticamente através do SpringDoc OpenAPI.
