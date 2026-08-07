# Database Design

## Objetivo

Este documento define toda a estrutura do banco de dados do StockTrack.

Todas as tabelas, colunas, tipos, relacionamentos e restrições deverão ser definidos aqui antes da implementação.

---

# Banco de Dados

```
MySQL
```

---

# Convenções

## Nome das tabelas

Todas as tabelas utilizarão letras minúsculas, snake_case, nomes no plural.

Exemplos:

```
users
categories
tool_types
tools
locations
loans
audit_logs
```

## Nome das colunas

```
snake_case
```

## Chaves Primárias

Todas as tabelas utilizarão:

```
id BIGINT AUTO_INCREMENT
```

---

# Tabela users

| Campo | Tipo | Restrição |
|--------|------|-----------|
| id | BIGINT | PK |
| name | VARCHAR(150) | NOT NULL |
| re | VARCHAR(30) | UNIQUE, NOT NULL |
| area | VARCHAR(100) | NOT NULL |
| password | VARCHAR(255) | NOT NULL |
| role | VARCHAR(20) | NOT NULL |
| created_at | DATETIME | NOT NULL |
| updated_at | DATETIME | NOT NULL |

---

# Tabela categories

| Campo | Tipo | Restrição |
|--------|------|-----------|
| id | BIGINT | PK |
| name | VARCHAR(100) | NOT NULL, UNIQUE |
| created_at | DATETIME | NOT NULL |
| updated_at | DATETIME | NOT NULL |

---

# Tabela tool_types

| Campo | Tipo | Restrição |
|--------|------|-----------|
| id | BIGINT | PK |
| name | VARCHAR(150) | NOT NULL |
| brand | VARCHAR(100) | NOT NULL |
| model | VARCHAR(100) | NOT NULL |
| description | TEXT | NULL |
| minimum_stock | INT | NOT NULL |
| requires_calibration | BOOLEAN | NOT NULL DEFAULT FALSE |
| created_at | DATETIME | NOT NULL |
| updated_at | DATETIME | NOT NULL |

---

# Tabela tool_types_categories (tabela associativa N:N)

| Campo | Tipo | Restrição |
|--------|------|-----------|
| tool_type_id | BIGINT | FK, PK composta |
| category_id | BIGINT | FK, PK composta |

---

# Tabela locations

| Campo | Tipo | Restrição |
|--------|------|-----------|
| id | BIGINT | PK |
| corridor | VARCHAR(50) | NOT NULL |
| shelf | VARCHAR(50) | NOT NULL |
| drawer | VARCHAR(50) | NOT NULL |
| created_at | DATETIME | NOT NULL |
| updated_at | DATETIME | NOT NULL |

Restrição adicional: a combinação (corridor, shelf, drawer) deve ser única — evita duplicidade da mesma posição física cadastrada duas vezes.

---

# Tabela tools

| Campo | Tipo | Restrição |
|--------|------|-----------|
| id | BIGINT | PK |
| serial_number | VARCHAR(100) | UNIQUE, NOT NULL |
| status | VARCHAR(20) | NOT NULL |
| last_calibration_date | DATE | NULL |
| next_calibration_date | DATE | NULL |
| tool_type_id | BIGINT | FK, NOT NULL |
| location_id | BIGINT | FK, NULL |
| created_at | DATETIME | NOT NULL |
| updated_at | DATETIME | NOT NULL |

`location_id` é anulável propositalmente: quando uma ferramenta é descartada, seu vínculo com a Localização é removido (ver `03-Business-Rules.md`, seção Ferramenta — Exclusão/Descarte).

---

# Tabela loans

| Campo | Tipo | Restrição |
|--------|------|-----------|
| id | BIGINT | PK |
| reason | VARCHAR(255) | NOT NULL |
| observation | TEXT | NULL |
| loan_date | DATETIME | NOT NULL |
| expected_return_date | DATETIME | NULL |
| return_date | DATETIME | NULL |
| tool_id | BIGINT | FK, NOT NULL |
| borrowed_by_user_id | BIGINT | FK, NOT NULL |
| returned_by_user_id | BIGINT | FK, NULL |
| created_at | DATETIME | NOT NULL |
| updated_at | DATETIME | NOT NULL |

`returned_by_user_id` é anulável e distinto de `borrowed_by_user_id`, refletindo a regra de que a devolução pode ser feita por um usuário diferente de quem retirou a ferramenta (ver `03-Business-Rules.md`, seção Devolução). Enquanto `return_date` for nulo, o empréstimo é considerado em aberto.

---

# Tabela audit_logs

| Campo | Tipo | Restrição |
|--------|------|-----------|
| id | BIGINT | PK |
| action_type | VARCHAR(30) | NOT NULL |
| reason | VARCHAR(255) | NULL |
| tool_id | BIGINT | FK, NOT NULL |
| user_id | BIGINT | FK, NOT NULL |
| created_at | DATETIME | NOT NULL |

Esta tabela não possui `updated_at` — reflete a regra de imutabilidade (`03-Business-Rules.md`, seção Log de Auditoria): um registro nunca é atualizado após sua criação.

---

# Relacionamentos

```
users 1---N loans (como borrowed_by_user_id)
users 1---N loans (como returned_by_user_id)
users 1---N audit_logs

categories N---N tool_types

tool_types 1---N tools

locations 1---N tools

tools 1---N loans

tools 1---N audit_logs
```

---

# Chaves Estrangeiras

```
tool_types_categories.tool_type_id      → tool_types.id
tool_types_categories.category_id       → categories.id

tools.tool_type_id                      → tool_types.id
tools.location_id                       → locations.id

loans.tool_id                           → tools.id
loans.borrowed_by_user_id               → users.id
loans.returned_by_user_id               → users.id

audit_logs.tool_id                      → tools.id
audit_logs.user_id                      → users.id
```

---

# Índices

```
users.re                    (busca de login)

tools.serial_number         (busca rápida por número de série)
tools.status                (filtro por status)
tools.tool_type_id          (junção com tipo)
tools.location_id           (filtro por localização)

loans.tool_id                (histórico por ferramenta)
loans.borrowed_by_user_id    (histórico por colaborador)
loans.return_date            (identificar empréstimos em aberto)

audit_logs.tool_id           (histórico por ferramenta)
audit_logs.user_id           (histórico por usuário)
```

---

# Integridade

Não será permitido:

- Tool sem ToolType.
- Loan sem Tool.
- Loan sem usuário que emprestou (borrowed_by_user_id).
- AuditLog sem Tool.
- AuditLog sem User.

---

# Exclusões

## Category

Não será permitida a exclusão caso exista ToolType vinculado (validação em nível de aplicação, não cascade automático no banco — mesma filosofia já validada em projeto anterior: regra de negócio pertence à camada Service, não ao schema).

## ToolType

Não será permitida a exclusão caso exista Tool vinculada.

## Location

Não será permitida a exclusão caso exista Tool ativa (não descartada) vinculada.

## Tool

Nunca é excluída fisicamente — apenas tem seu status alterado para DISCARDED e seu `location_id` definido como NULL.

## User, Loan, AuditLog

Não há exclusão prevista para estas entidades na V1.

---

# Datas

Datas simples:

```
DATE
```

Datas de auditoria e eventos com horário:

```
DATETIME
```

---

# Auditoria de Schema

Todas as tabelas possuirão `created_at`, exceto `audit_logs` que também não possui `updated_at`, por ser imutável.

As demais tabelas possuirão `created_at` e `updated_at`.

---

# Escalabilidade

O banco foi modelado para permitir futuras expansões sem alterações estruturais significativas, entre elas:

- kits/maletas técnicas (nova tabela agrupando múltiplas Tools);
- agendamento de empréstimos futuros (campos adicionais em `loans` ou nova tabela de reservas);
- notificações (nova tabela de histórico de notificações enviadas).
