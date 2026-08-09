# Domain Model

## Objetivo

Este documento define o modelo de domínio do StockTrack.

Todas as entidades, seus relacionamentos e responsabilidades são definidos aqui antes da implementação.

---

# Visão Geral

O StockTrack foi modelado seguindo princípios de alta coesão e baixo acoplamento, com foco central em rastreabilidade: toda ação relevante sobre uma ferramenta deve poder ser reconstruída a partir do histórico registrado.

---

# Modelo Geral

```
User (Colaborador ou Almoxarife)
│
├── Loan (como colaborador que empresta/devolve)
└── AuditLog (como responsável pela ação)

Category
│
└── ToolType (N:N)

ToolType
│
└── Tool

Location
│
└── Tool

Tool
│
├── Loan
└── AuditLog
```

---

# Entidade User

## Responsabilidade

Representa qualquer pessoa que utiliza o sistema — colaborador de linha ou responsável pelo almoxarifado.

Não é responsável por regras de estoque; apenas identifica quem realiza cada ação no sistema.

## Atributos

- id
- name
- re (Registro do Empregado — identificador único)
- area (texto livre, informado pelo próprio colaborador)
- password
- role (COLLABORATOR ou WAREHOUSE_MANAGER)
- createdAt
- updatedAt

`re` é imutável após a criação, em ambos os fluxos de cadastro.

## Relacionamentos

Um usuário possui:

- Loans (como colaborador que emprestou/devolveu)
- AuditLogs (como responsável pela ação registrada)

---

# Enum Role

Representa o papel do usuário no sistema.

Valores:

- COLLABORATOR
- WAREHOUSE_MANAGER

### Regra de criação de conta

Existem dois fluxos: autocadastro público (`POST /auth/register`, sempre gera COLLABORATOR)
e cadastro administrativo pelo Almoxarife (`POST /users`). O primeiro
WAREHOUSE_MANAGER é inserido via seed Flyway.

---

# Entidade Category

## Responsabilidade

Classificar Tipos de Ferramenta (ex: "Ferramentas Manuais", "Instrumentos de Medição").

Um Tipo de Ferramenta pode pertencer a mais de uma categoria simultaneamente.

## Atributos

- id
- name
- createdAt
- updatedAt

## Relacionamentos

É utilizada por:

- ToolTypes (N:N)

### Regra de exclusão

Uma categoria só pode ser excluída caso não exista nenhum Tipo de Ferramenta vinculado a ela.

---

# Entidade ToolType

## Responsabilidade

Representa o modelo/tipo de uma ferramenta (ex: "Furadeira Bosch XYZ", "Chave de Fenda Phillips 10mm") — não uma unidade física específica.

Concentra os atributos que são comuns a todas as unidades daquele modelo, incluindo a exigência (ou não) de controle de calibração.

## Atributos

- id
- name
- brand
- model
- description (detalhes técnicos: voltagem, tamanho, etc.)
- minimumStock (estoque mínimo, definido individualmente por tipo)
- requiresCalibration (boolean — define se as unidades deste tipo exigem controle de calibração)
- createdAt
- updatedAt

## Relacionamentos

Pertence a:

- Categories (N:N)

Possui:

- Tools (unidades físicas)

### Regra de exclusão

Um Tipo de Ferramenta só pode ser excluído caso não exista nenhuma unidade (Tool) cadastrada vinculada a ele.

### Nota de design

`requiresCalibration` é um campo booleano explícito no Tipo de Ferramenta — não é inferido a partir do nome de nenhuma Categoria. Isso evita que a regra de bloqueio de calibração dependa de comparação de texto (frágil e propensa a erro de digitação/formatação), tornando-a uma regra de domínio robusta e explícita.

---

# Entidade Tool

## Responsabilidade

Representa uma unidade física individual de ferramenta, com número de série próprio e status individual.

## Atributos

- id
- serialNumber (único no sistema, entre todas as ferramentas, independente do tipo)
- status (AVAILABLE, IN_USE, IN_MAINTENANCE, DISCARDED)
- lastCalibrationDate (nullable — relevante apenas se o ToolType exigir calibração)
- nextCalibrationDate (nullable)
- createdAt
- updatedAt

## Relacionamentos

Pertence a:

- ToolType (obrigatório)
- Location (opcional — pode não ter localização após ser descartada)

Possui:

- Loans
- AuditLogs

### Regra de status

Status possíveis e suas transições:

- **AVAILABLE**: disponível para empréstimo.
- **IN_USE**: emprestada a um colaborador no momento.
- **IN_MAINTENANCE**: enviada para manutenção pelo Almoxarife; pode retornar para AVAILABLE.
- **DISCARDED**: estado final e irreversível. A ferramenta não pode retornar a nenhum outro status.

Apenas o Almoxarife pode alterar o status de uma ferramenta (enviar para manutenção, retornar da manutenção, descartar). O colaborador apenas empresta e devolve — a devolução não altera diretamente o status para além do ciclo de empréstimo (ver Entidade Loan).

### Regra de calibração vencida

`calibrationOverdue` **não é um campo persistido**. É calculado dinamicamente, no momento da consulta, comparando `nextCalibrationDate` com a data atual — apenas quando `ToolType.requiresCalibration` for verdadeiro.

Esta decisão segue a mesma filosofia já validada em projetos anteriores do autor: valores calculáveis a partir de outros dados nunca são persistidos, evitando divergência entre o dado armazenado e a realidade.

Quando `calibrationOverdue` é verdadeiro, a ferramenta não pode ser emprestada, independentemente do seu `status` estar como AVAILABLE.

### Regra de exclusão (descarte)

Uma ferramenta nunca é excluída fisicamente do sistema. Ao ser descartada:

- seu status é alterado para DISCARDED, permanentemente;
- o vínculo com sua Location é removido (a ferramenta deixa de ocupar uma localização, mas a Location em si permanece no catálogo, podendo ser usada por outras ferramentas);
- seu `serialNumber` é liberado — pode ser reutilizado no cadastro de uma nova unidade (que será um registro novo e independente).

---

# Entidade Location

## Responsabilidade

Representa uma posição física estruturada dentro do almoxarifado, seguindo uma hierarquia fixa: corredor, prateleira e gaveta.

Funciona como catálogo reutilizável — uma mesma Location pode estar associada a diversas ferramentas simultaneamente.

## Atributos

- id
- corridor
- shelf
- drawer
- createdAt
- updatedAt

## Relacionamentos

É utilizada por:

- Tools

### Regra de criação e edição

Apenas o Almoxarife cadastra e edita localizações.

### Regra de exclusão

Uma localização só pode ser excluída caso não exista nenhuma ferramenta (Tool) ativa vinculada a ela no momento.

---

# Entidade Loan

## Responsabilidade

Representa o ciclo de empréstimo de uma ferramenta a um colaborador — desde a retirada até a devolução.

## Atributos

- id
- reason (motivo do empréstimo — obrigatório)
- observation (observação da devolução — opcional)
- loanDate
- expectedReturnDate (opcional; se não informado, assume-se um mínimo implícito de 7 horas a partir do empréstimo)
- returnDate (nulo enquanto o empréstimo está em aberto)
- createdAt
- updatedAt

## Relacionamentos

Pertence a:

- Tool
- User (colaborador que realizou o empréstimo)

### Regras de negócio

- Qualquer usuário autenticado (Colaborador ou Almoxarife) pode realizar um empréstimo para si mesmo, desde que a ferramenta esteja com status AVAILABLE e sem calibração vencida.
- Um colaborador pode ter múltiplas ferramentas emprestadas simultaneamente, sem limite.
- A devolução pode ser realizada por qualquer usuário autenticado, não necessariamente o mesmo que retirou a ferramenta. O sistema sempre registra quem devolveu, mesmo que seja pessoa diferente de quem emprestou.
- Ao devolver, a ferramenta retorna automaticamente ao status AVAILABLE (a menos que o Almoxarife decida, em ação separada, enviá-la para manutenção).
- "Atraso" não é um campo persistido — é calculado dinamicamente comparando `expectedReturnDate` com a data atual, no momento da consulta.

---

# Entidade AuditLog

## Responsabilidade

Registrar, de forma permanente e imutável, toda ação relevante realizada sobre uma ferramenta.

É a fonte de verdade para rastreabilidade completa do sistema.

## Atributos

- id
- actionType (enum — ver abaixo)
- reason (nullable — motivo ou observação associada à ação, quando aplicável)
- timestamp

## Relacionamentos

Pertence a:

- Tool (sobre qual ferramenta a ação foi realizada)
- User (quem realizou a ação)

### Regra de imutabilidade

Um registro de AuditLog, uma vez criado, nunca é editado ou excluído. Não existe endpoint de atualização ou remoção para esta entidade.

Cada registro guarda apenas o estado final da ação (ex: "ferramenta enviada para manutenção"), não o estado anterior e posterior lado a lado.

---

# Enum ToolStatus

Valores:

- AVAILABLE
- IN_USE
- IN_MAINTENANCE
- DISCARDED

---

# Enum ActionType

Representa o tipo de ação registrada no AuditLog.

Valores:

- CREATED
- LOANED
- RETURNED
- SENT_TO_MAINTENANCE
- RETURNED_FROM_MAINTENANCE
- DISCARDED
- LOCATION_CHANGED
- EDITED

---

# Relacionamentos

## Category

```
N ---- N ToolType
```

## ToolType

```
1 ---- N Tool
```

## Location

```
1 ---- N Tool
```

## Tool

```
1 ---- N Loan

1 ---- N AuditLog
```

## User

```
1 ---- N Loan

1 ---- N AuditLog
```

---

# Cardinalidades

| Entidade | Relacionamento | Cardinalidade |
|----------|----------------|----------------|
| Category | ToolType | N : N |
| ToolType | Tool | 1 : N |
| Location | Tool | 1 : N |
| Tool | Loan | 1 : N |
| Tool | AuditLog | 1 : N |
| User | Loan | 1 : N |
| User | AuditLog | 1 : N |

---

# Regras de Dependência

Category NÃO conhece ToolType diretamente (a relação N:N é navegada a partir de ToolType).

Location NÃO conhece Tool (navegação unidirecional: Tool aponta para Location, nunca o inverso) — mesmo princípio já validado em projetos anteriores do autor, evitando carregamento desnecessário de coleções potencialmente grandes.

Toda informação de rastreabilidade (Loan, AuditLog) depende obrigatoriamente de uma Tool e de um User — nenhuma ação é registrada de forma anônima ou desassociada de uma ferramenta específica.

---

# Responsabilidade das Entidades

## User

Identifica quem realiza cada ação no sistema. Não carrega regra de estoque.

## Category

Classificação reutilizável de Tipos de Ferramenta.

## ToolType

Representa o modelo da ferramenta e concentra as regras que se aplicam a todas as suas unidades (estoque mínimo, exigência de calibração).

## Tool

Representa a unidade física individual — o objeto real que é emprestado, devolvido e rastreado.

## Location

Representa a estrutura física de armazenamento, reutilizável entre diversas ferramentas.

## Loan

Representa o ciclo operacional de empréstimo/devolução em andamento ou concluído.

## AuditLog

Representa a trilha histórica e imutável de tudo que já aconteceu no sistema — mais ampla que o Loan, cobrindo qualquer tipo de ação sobre qualquer ferramenta.

---

# Preparação para Evolução

O domínio foi projetado para permitir futuramente, sem necessidade de grandes alterações estruturais:

- kits/maletas técnicas, como agrupamento de múltiplas Tools emprestadas/devolvidas em conjunto;
- agendamento de empréstimos futuros, com verificação de conflito de horário;
- código único (QR Code) por ferramenta, reaproveitando o próprio `serialNumber` já existente;
- notificações ativas (e-mail) para atraso de devolução e calibração próxima do vencimento.

---

# Filosofia do Modelo

Cada entidade possui apenas uma responsabilidade. Toda lógica de negócio ficará concentrada na camada Service.

Valores calculáveis a partir de outros dados (atraso de devolução, calibração vencida) nunca são persistidos — são sempre calculados dinamicamente no momento da consulta, evitando divergência entre o dado armazenado e a realidade operacional.

A rastreabilidade é tratada como requisito de primeira classe do domínio, não como funcionalidade acessória.
