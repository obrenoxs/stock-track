# Business Rules

## Objetivo

Este documento centraliza todas as regras de negócio do StockTrack.

Nenhuma funcionalidade poderá ser implementada sem que sua regra esteja previamente documentada.

---

# Usuário

## Criação de Conta

Existem dois fluxos de criação de conta:

- **Autocadastro (Colaborador):** qualquer pessoa pode se cadastrar via `POST /auth/register`,
  informando RE e senha. Toda conta criada por este fluxo nasce obrigatoriamente com role COLLABORATOR.
- **Cadastro administrativo (Almoxarife):** o `WAREHOUSE_MANAGER` pode cadastrar outro
  Almoxarife via `POST /users`. A role não é informada na requisição — é sempre
  `WAREHOUSE_MANAGER`, atribuída automaticamente pelo Service. Não existe mais cadastro
  assistido de Colaborador por este endpoint; o Colaborador sempre se autocadastra via
  `POST /auth/register`.
- O primeiro WAREHOUSE_MANAGER do sistema é inserido via migration Flyway (seed inicial),
  já que não existe fluxo de "promoção" de usuário via API na V1.

## RE (Registro do Empregado)

O RE deve ser único no sistema. Caso já exista um usuário cadastrado com determinado RE, uma nova conta não poderá ser criada com o mesmo valor — a pessoa deve apenas realizar login.

[sem alteração — RE único, imutável após criação em qualquer um dos dois fluxos]

## Senha

- Tamanho mínimo: 8 caracteres.
- Tamanho máximo: 100 caracteres.
- Aceita acentuação e caracteres Unicode, sem restrição de padrão/complexidade.

## Área

A área de atuação do colaborador é um campo de texto livre, preenchido no momento do cadastro (pelo Almoxarife), utilizado apenas como informação de controle.

## Papéis (Roles)

Existem exatamente dois papéis no sistema:

- **COLLABORATOR**: pode emprestar e devolver ferramentas para si mesmo. Não possui acesso a funcionalidades administrativas.
- **WAREHOUSE_MANAGER**: possui todas as permissões de um colaborador (pode emprestar/devolver ferramentas para si mesmo), além de exclusividade sobre: cadastro/edição/exclusão de Categorias, Tipos de Ferramenta, Localizações e Ferramentas; alteração de status de ferramentas (envio/retorno de manutenção, descarte); definição de estoque mínimo; cadastro de novos usuários.

## Atualização de Dados

Um usuário autenticado pode atualizar apenas os próprios dados, via `PUT /users/me`:
name, area e password são editáveis. **O RE nunca pode ser alterado** (é o identificador de login).

Não existe endpoint administrativo de edição de outro usuário na V1 — o Almoxarife não edita
dados de outros usuários, apenas os cadastra.

Para alteração de senha, é obrigatório informar a senha atual como confirmação, além da nova senha.

---

# Categoria

Categorias são criadas, editadas e excluídas exclusivamente pelo Almoxarife.

Um Tipo de Ferramenta pode estar associado a mais de uma categoria simultaneamente.

## Exclusão

Uma categoria somente poderá ser excluída caso não exista nenhum Tipo de Ferramenta vinculado a ela.

Caso exista vínculo, a exclusão deverá ser impedida.

---

# Tipo de Ferramenta

Tipos de Ferramenta são criados, editados e excluídos exclusivamente pelo Almoxarife.

## Cadastro

Todo Tipo de Ferramenta deverá possuir obrigatoriamente:

- nome;
- marca;
- modelo;
- descrição (detalhes técnicos, como voltagem ou tamanho);
- estoque mínimo;
- pelo menos uma categoria associada;
- indicação se exige controle de calibração (`requiresCalibration`).

## Intervalo de Calibração

Aplicável apenas quando `requiresCalibration = true`. O Almoxarife define, no momento do
cadastro, o intervalo em meses entre calibrações consecutivas (ex: 1, 3, 7 meses). Esse valor
é usado para calcular `nextCalibrationDate` sempre que uma calibração é registrada em uma
unidade (Tool) desse tipo.

## Fluxo de Cadastro de Unidades

O número de série é sempre obrigatório no momento do cadastro de uma unidade — não existe unidade de ferramenta sem número de série definido.

No momento da criação de um Tipo de Ferramenta, o Almoxarife pode optar por:

- cadastrar uma única unidade, informando seu número de série; ou
- cadastrar múltiplas unidades de uma vez, informando o número de série de cada uma individualmente na mesma operação.

Em ambos os casos, cada unidade só é efetivamente criada no sistema com seu número de série já definido — não existe conceito de unidade "incompleta" ou "aguardando preenchimento" no Backend. A possibilidade de cadastro em lote é uma conveniência de fluxo (por exemplo, um formulário que permite informar vários números de série de uma vez), não uma exceção à regra de obrigatoriedade.

## Estoque Mínimo

O estoque mínimo é definido individualmente por Tipo de Ferramenta, no momento do seu cadastro, e nunca por Categoria — categorias são apenas classificação, sem relação com controle de estoque.

A definição do valor de estoque mínimo é responsabilidade do Almoxarife, que deve considerá-lo com base em critérios como tempo de reposição, taxa de consumo/desgaste e criticidade da ferramenta para a operação. O sistema não impõe cálculo automático desse valor — apenas armazena e utiliza o valor informado para gerar alertas.

## Exclusão

Um Tipo de Ferramenta somente poderá ser excluído caso não exista nenhuma unidade (Ferramenta) cadastrada vinculada a ele.

---

# Ferramenta (Unidade Física)

## Cadastro

Toda unidade de ferramenta deverá possuir obrigatoriamente:

- número de série;
- vínculo com um Tipo de Ferramenta;
- localização física.

## Número de Série

O número de série é um identificador estritamente único no sistema — não pode se repetir entre unidades, independentemente de categoria, marca, modelo ou tipo.

## Status

Uma ferramenta pode assumir exatamente um dos seguintes status:

- **AVAILABLE** (Disponível): pode ser emprestada.
- **IN_USE** (Em Uso): emprestada a um colaborador no momento.
- **IN_MAINTENANCE** (Em Manutenção): fora de circulação para reparo; pode retornar para Disponível.
- **DISCARDED** (Descartada): estado final e irreversível.

Somente o Almoxarife pode alterar manualmente o status de uma ferramenta para IN_MAINTENANCE, de volta para AVAILABLE, ou para DISCARDED.

A transição entre AVAILABLE e IN_USE ocorre automaticamente através das ações de empréstimo e devolução (ver seção Empréstimo).

Uma ferramenta com status DISCARDED nunca pode retornar a nenhum outro status.

## Calibração

Aplica-se apenas a unidades cujo Tipo de Ferramenta possua `requiresCalibration = true`.

O Almoxarife é responsável por registrar a data da última calibração realizada, o que atualiza automaticamente a data da próxima calibração prevista.

Uma ferramenta é considerada com **calibração vencida** quando a data da próxima calibração já passou. Esta condição não é armazenada como um valor fixo — é sempre calculada dinamicamente no momento da consulta, a partir da data de próxima calibração registrada.

Uma ferramenta com calibração vencida não pode ser emprestada, mesmo que seu status esteja como AVAILABLE. O sistema deve informar claramente o motivo do bloqueio.

## Exclusão (Descarte)

Uma ferramenta nunca é excluída fisicamente do sistema — apenas descartada (status DISCARDED), de forma permanente e irreversível.

Ao ser descartada:

- o vínculo com sua Localização é removido — a ferramenta deixa de ocupar uma posição física, mas a Localização em si permanece disponível no catálogo para outras ferramentas;
- seu número de série é liberado, podendo ser utilizado no cadastro de uma nova unidade (um novo registro, sem qualquer relação com a unidade descartada).

Toda ação de descarte é obrigatoriamente registrada no Log de Auditoria, identificando quem a realizou e quando.

---

# Localização

Localizações são criadas, editadas e excluídas exclusivamente pelo Almoxarife.

## Estrutura

Toda localização é composta por três níveis fixos: corredor, prateleira e gaveta.

## Reutilização

Uma mesma Localização pode estar associada a diversas ferramentas simultaneamente — não existe relação exclusiva entre uma unidade de ferramenta e sua localização.

## Exclusão

Uma localização somente poderá ser excluída caso não exista nenhuma ferramenta ativa (não descartada) vinculada a ela no momento.

A validação de unicidade da combinação (corridor, shelf, drawer) se aplica também na edição
(`PUT /locations/{id}`) — não é permitido editar uma localização para uma combinação já
ocupada por outra.

---

# Empréstimo e Devolução

## Empréstimo

Qualquer usuário autenticado — Colaborador ou Almoxarife — pode realizar um empréstimo para si mesmo.

Uma ferramenta só pode ser emprestada caso:

- seu status esteja como AVAILABLE; e
- não possua calibração vencida (quando aplicável).

Caso a ferramenta não esteja disponível para empréstimo, o sistema deve bloquear a operação e informar claramente o motivo (ex.: "esta ferramenta já está em uso").

O motivo do empréstimo é obrigatório.

A data prevista de devolução é opcional. Caso o colaborador não informe uma data específica, considera-se um prazo mínimo implícito de 7 horas a partir do momento do empréstimo. Caso o colaborador informe uma data própria (por exemplo, previsão de uso de 3 dias), essa data prevalece.

O colaborador tem liberdade para devolver a ferramenta antes do prazo previsto, a qualquer momento.

Ao ser emprestada, a ferramenta tem seu status automaticamente alterado para IN_USE.

## Devolução

A devolução pode ser realizada por qualquer usuário autenticado, não necessariamente o mesmo colaborador que retirou a ferramenta. O sistema sempre identifica e registra quem efetivamente realizou a devolução.

Na devolução, é obrigatório apenas identificar o usuário que está devolvendo (via sessão autenticada). Um campo de observação é opcional, permitindo ao colaborador registrar qualquer informação relevante (ex.: "ferramenta com desgaste, recomendo avaliação").

Ao ser devolvida, a ferramenta retorna automaticamente ao status AVAILABLE. O envio para manutenção, caso necessário, é uma ação separada e posterior, realizada exclusivamente pelo Almoxarife.

## Atraso

Um empréstimo é considerado em atraso quando sua data prevista de devolução já passou e a ferramenta ainda não foi devolvida.

Esta condição não é armazenada como um valor fixo — é sempre calculada dinamicamente no momento da consulta, comparando a data prevista de devolução com a data atual.

---

# Log de Auditoria

Todo o histórico de ações relevantes sobre uma ferramenta é registrado permanentemente, cobrindo os seguintes tipos de ação:

- criação;
- empréstimo;
- devolução;
- envio para manutenção;
- retorno de manutenção;
- descarte;
- mudança de localização;
- edição de dados.

## Imutabilidade

Um registro do Log de Auditoria, uma vez criado, nunca é editado ou excluído — sob nenhuma circunstância, por nenhum papel de usuário.

## Conteúdo do Registro

Cada registro guarda apenas o estado final da ação realizada (ex.: "ferramenta enviada para manutenção"), identificando obrigatoriamente: qual ferramenta, qual usuário realizou a ação, e quando ocorreu. Não é registrado o estado anterior lado a lado com o novo estado.

---

# Estoque Mínimo e Alertas

O estoque mínimo é definido por Tipo de Ferramenta, no momento do seu cadastro, pelo Almoxarife.

Quando a quantidade de unidades disponíveis (status AVAILABLE) de um determinado Tipo de Ferramenta atingir ou ficar abaixo do estoque mínimo definido, o sistema deve sinalizar essa condição.

O alerta é uma notificação passiva: aparece ao Almoxarife ao consultar os dados do sistema (ex.: destaque visual na listagem ou dashboard). Não é enviado ativamente (e-mail ou notificação push) na V1.

O colaborador comum não visualiza esse tipo de alerta — o controle de estoque é responsabilidade exclusiva do Almoxarife.

---

# Consulta de Status

Ao consultar ferramentas (disponíveis, em uso, em manutenção ou descartadas), o sistema deve sempre retornar o estado atual e real de cada uma no momento da consulta — incluindo, quando aplicável, a previsão de retorno (data esperada de devolução) para ferramentas em uso.

---

# Datas

Todas as datas utilizarão:

- LocalDate
- LocalDateTime

---

# Futuras Funcionalidades

As funcionalidades abaixo NÃO pertencem à versão 1. Serão avaliadas para versões futuras:

- kits e maletas técnicas, como unidades compostas de empréstimo/devolução;
- agendamento de empréstimos futuros, com verificação de conflito de horário;
- código único (QR Code) por ferramenta, reaproveitando o número de série já existente, para leitura rápida via aplicativo móvel;
- notificações ativas (e-mail) para atraso de devolução e calibração próxima do vencimento.

---

# Regra Geral

Sempre que surgir uma nova funcionalidade, sua regra deverá ser documentada antes da implementação.
