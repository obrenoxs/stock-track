# Software Architecture

## Objetivo

Este documento define toda a arquitetura técnica do StockTrack.

Todas as decisões estruturais deverão ser registradas aqui antes de serem implementadas no código.

---

# Arquitetura

O StockTrack seguirá uma arquitetura em camadas (Layered Architecture), separando claramente cada responsabilidade do sistema.

```
Controller
↓
Service
↓
Repository
↓
Database
```

Cada camada possui apenas uma responsabilidade.

---

# Princípios Arquiteturais

O projeto seguirá os princípios:

- SOLID
- Clean Code
- Separation of Concerns
- Single Responsibility
- RESTful Architecture
- Baixo Acoplamento
- Alta Coesão

---

# Stack Tecnológica

## Backend

- Java 25
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- Hibernate
- Validation
- Maven

## Banco de Dados

- MySQL

## Frontend

- Avaliação futura (React), condicionada ao cronograma do projeto. Não faz parte do escopo inicial de implementação.

## DevOps

- Docker
- Docker Compose
- GitHub Actions

## Documentação

- OpenAPI
- Swagger

---

# Organização do Projeto

O projeto seguirá o padrão **Package by Feature**, mesmo padrão já validado em projeto anterior do autor — cada funcionalidade principal possui seu próprio pacote, contendo internamente todas as camadas necessárias (Controller, Service, Repository, Entity, DTO, Mapper).

Pacotes compartilhados entre funcionalidades (configuração, segurança, tratamento de exceções e utilitários) ficam centralizados em um pacote `shared`.

Estrutura geral:

```
com.stocktrack
│
├── user
│   └── (controller, service, repository, entity, dto, mapper)
│
├── category
│   └── (mesma estrutura interna)
│
├── tooltype
│   └── (mesma estrutura interna)
│
├── tool
│   └── (mesma estrutura interna)
│
├── location
│   └── (mesma estrutura interna)
│
├── loan
│   └── (mesma estrutura interna)
│
├── auditlog
│   └── (mesma estrutura interna)
│
└── shared
    ├── config
    ├── security
    ├── exception
    └── utils
```

Cada funcionalidade é responsável apenas pelas suas próprias camadas internas, mantendo alta coesão.

O `shared` concentra apenas o que é, de fato, transversal a múltiplas funcionalidades — nunca regra de negócio de um domínio específico.

---

## Regra de Dependência entre Módulos

Assim como já validado em projeto anterior, nenhuma funcionalidade deve depender diretamente de camadas internas de outra — apenas do que for exposto via contrato público (Service), nunca acessando Repository de outro módulo diretamente.

A cadeia de dependência natural entre os módulos deste projeto é:

```
loan → tool → tooltype → category
loan → tool → location
loan → user
auditlog → tool
auditlog → user
```

`tool` depende de `tooltype` e `location` (contratos públicos dos respectivos Services).
`tooltype` depende de `category` (contrato público do Service).
`loan` depende de `tool` e `user`.
`auditlog` depende de `tool` e `user`, mas nenhum outro módulo depende de `auditlog` — ele é, por natureza, um módulo "de escuta", registrando eventos de outros módulos sem que eles precisem conhecê-lo diretamente (ver seção sobre eventos, abaixo).

---

## Registro de Auditoria via Eventos

Para evitar que todos os módulos (`tool`, `loan`, `category`, `tooltype`, `location`) precisem depender diretamente de `auditlog` para registrar cada ação, o registro de auditoria será implementado através de **eventos de domínio**, seguindo o mesmo padrão já validado em projeto anterior do autor.

Cada módulo publica um evento correspondente à ação relevante ocorrida (ex: `ToolCreatedEvent`, `ToolLoanedEvent`, `ToolReturnedEvent`, `ToolDiscardedEvent`), e o módulo `auditlog` escuta esses eventos, criando o registro correspondente — sem que o módulo de origem precise conhecer `auditlog` diretamente.

### Síncrono, não assíncrono

Diferente de casos de uso não-críticos (como envio de e-mail, que pode ser assíncrono e tolerante a falha), o registro de auditoria é **crítico para a integridade do sistema** — uma ação sobre uma ferramenta nunca deve ocorrer sem o correspondente registro em log.

Por isso, os listeners de auditoria serão implementados de forma **síncrona**, dentro da mesma transação da ação que os originou. Caso o registro de auditoria falhe por qualquer motivo, a ação original (ex: empréstimo) deve sofrer rollback — não deve ser possível uma ação existir sem seu registro correspondente na trilha de auditoria.

---

# API

A API seguirá integralmente os princípios REST.

Cada recurso possuirá seu próprio endpoint, sob o prefixo:

```
/api/v1
```

Exemplo:

```
/users
/categories
/tool-types
/tools
/locations
/loans
/audit-logs
```

Os verbos HTTP serão utilizados corretamente: GET, POST, PUT, PATCH, DELETE.

---

# Segurança

O sistema utilizará:

- Spring Security
- JWT
- BCrypt

A autenticação será realizada utilizando:

```
RE (Registro do Empregado) + Senha
```

Diferente de projeto anterior do autor (que utilizava e-mail como identificador de login), este projeto utiliza o RE como identificador único de autenticação, refletindo o contexto corporativo do domínio.

## Autorização por Papel

O sistema utilizará autorização baseada em papéis (`Role`), com dois níveis: `COLLABORATOR` e `WAREHOUSE_MANAGER`.

Endpoints administrativos (cadastro/edição/exclusão de Categoria, Tipo de Ferramenta, Localização, Ferramenta, alteração de status, definição de estoque mínimo, cadastro de usuários) exigirão o papel `WAREHOUSE_MANAGER`.

Endpoints de empréstimo e devolução estarão disponíveis para ambos os papéis, já que o Almoxarife também pode emprestar ferramentas para si mesmo.

---

## Token JWT

O token de acesso expira em 1 hora. Não existe endpoint de refresh token na V1 — expirado o
token, o usuário realiza login novamente. Chave de assinatura e tempo de expiração são lidos
via variável de ambiente, nunca hardcoded (ver 08-Development-Standards.md, seção Segurança).

---


# Convenções

## Identificadores

Todas as entidades utilizarão:

```
Long
```

com geração automática pelo banco.

## Datas

Será utilizado:

```
LocalDate
LocalDateTime
```

Nunca será utilizado Date.

## DTOs

Toda comunicação entre API e cliente será realizada através de DTOs. Entidades nunca serão retornadas diretamente ao cliente.

## Mapper

A conversão entre Entity e DTO será realizada utilizando MapStruct.

## Tratamento de Erros

Todas as exceções serão centralizadas através de:

```
@RestControllerAdvice
```

---

# Escalabilidade

O sistema será desenvolvido pensando em futuras expansões, entre elas:

- kits e maletas técnicas;
- agendamento de empréstimos futuros;
- código único (QR Code) por ferramenta;
- notificações ativas.

---

# Filosofia de Desenvolvimento

Antes de implementar qualquer funcionalidade, devemos responder:

- Esta funcionalidade segue os princípios REST?
- Está respeitando o SOLID?
- Está preparada para evolução futura?
- Está simples?
- Está desacoplada?
- Está documentada?

Caso qualquer resposta seja "não", a implementação deverá ser revista antes de continuar.
