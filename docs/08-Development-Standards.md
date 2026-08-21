# Development Standards

## Objetivo

Este documento define todos os padrões de desenvolvimento do StockTrack.

Todo código implementado deverá seguir estas diretrizes.

---

# Filosofia

Antes de escrever qualquer código, devemos responder:

- É simples?
- É legível?
- É escalável?
- Está desacoplado?
- Está documentado?
- Segue o SOLID?

Caso qualquer resposta seja "não", a implementação deverá ser revisada.

---

# Princípios

O projeto seguirá: SOLID, Clean Code, KISS, DRY, Separation of Concerns, RESTful Design.

---

# Organização de Pacotes

O projeto segue o padrão **Package by Feature**, detalhado em `02-Software-Architecture.md`.

Regras:

- Nenhuma funcionalidade deve depender diretamente de camadas internas de outra (ex: `tool` não deve importar classes de dentro de `tooltype.service`, apenas o que for exposto via `shared` ou contrato público).
- Uma classe só entra em `shared` se for utilizada por duas ou mais funcionalidades.

---

# Convenções de Nome

## Classes

PascalCase — ex: `ToolService`, `LoanController`, `AuditLogMapper`.

## Métodos

camelCase — ex: `createTool()`, `findAvailableByType()`, `registerCalibration()`.

## Variáveis

camelCase — ex: `toolId`, `currentUser`, `loanRepository`.

## Constantes

UPPER_CASE — ex: `DEFAULT_LOAN_MINIMUM_HOURS`, `DEFAULT_PAGE_SIZE`.

## Enums

Sempre no singular — ex: `ToolStatus`, `Role`, `ActionType`.

---

# Controllers

Responsabilidade única: receber a requisição, enviar ao Service, retornar a resposta.

Nenhuma regra de negócio deverá existir no Controller.

---

# Services

Toda regra de negócio ficará na camada Service. O Service nunca deverá acessar HTTP, nem conhecer DTO de entrada diretamente além do necessário para orquestrar a chamada.

---

# Repository

Repository apenas acessa o banco. Nenhuma regra de negócio deverá existir aqui.

---

# Entities

Entities representam apenas o domínio. Não devem possuir lógica de negócio, chamadas HTTP ou acesso ao banco.

---

# DTOs

Toda comunicação externa utilizará DTO. Nunca retornar Entity diretamente.

Teremos: Request DTO e Response DTO.

---

# Mapper

Todo mapeamento será realizado utilizando MapStruct. Nunca realizar conversões manuais, salvo exceções muito específicas.

---

# Tratamento de Exceções

Todo erro será tratado através de `@RestControllerAdvice`. Nunca utilizar try/catch desnecessário.

---

# Validação

Toda entrada será validada utilizando Jakarta Validation (`@NotBlank`, `@NotNull`, `@Positive`, `@Size`, etc.).

---

# Logs

Utilizar SLF4J / LoggerFactory. Nunca utilizar `System.out.println()`.

---

# Comentários

Evitar comentários desnecessários. Código deve ser autoexplicativo. Comentários apenas quando agregarem contexto real (ex: sinalizar uma decisão de negócio pendente ou uma limitação consciente).

---

# Métodos e Classes

Métodos devem possuir apenas uma responsabilidade, preferencialmente entre 20 e 30 linhas.

Classes devem ser pequenas, com alta coesão e baixo acoplamento.

---

# Dependências

Sempre utilizar Constructor Injection. Nunca utilizar `@Autowired` em atributos.

---

# Eventos de Domínio

Eventos síncronos (`@EventListener`) serão utilizados para operações críticas e atômicas, onde a ausência do efeito colateral (ex: registro de auditoria) representaria um estado inválido do sistema.

Eventos assíncronos (`@Async` + `@TransactionalEventListener`) ficam reservados para operações não-críticas e tolerantes a falha (ex: notificações futuras por e-mail).

Esta distinção deve ser reavaliada, caso a caso, sempre que um novo evento for introduzido — nunca aplicada por padrão sem necessidade real.

---

# Versionamento (Commits)

Commits seguirão o padrão:

```
feat:
fix:
refactor:
docs:
test:
style:
chore:
build:
ci:
```

Exemplos:

```
feat: create tool loan endpoint
fix: validate duplicated serial number
docs: update database design
refactor: extract calibration check to validator
```

---

# Granularidade dos Commits

Cada commit deve representar uma fatia coesa e completa de implementação (não uma classe isolada, nem um módulo inteiro de uma vez). O momento de sugerir o commit deve ser anunciado imediatamente ao final de cada fatia — não acumulado até o fim de um módulo inteiro.

---

# Branches

Enquanto o desenvolvimento for individual, o trabalho ocorrerá diretamente na `main`. Caso o projeto cresça, adotar-se-á Git Flow completo.

---

# Performance

Evitar: consultas N+1, carregamentos desnecessários, duplicação de código.

---

# Segurança

Nunca armazenar senhas em texto, secrets no código ou tokens hardcoded. Utilizar sempre Environment Variables.

---

# Documentação

Toda funcionalidade deverá possuir: documentação, endpoint documentado, regra de negócio registrada — nesta ordem, antes da implementação.

---

# Qualidade

Antes de finalizar qualquer funcionalidade, verificar:

✓ Código limpo
✓ SOLID
✓ DTO
✓ Mapper
✓ Exception
✓ Documentação
✓ REST

---

# Regra de Ouro

Qualquer pessoa deve conseguir abrir este projeto e entender rapidamente como ele foi desenvolvido — e por quê.
