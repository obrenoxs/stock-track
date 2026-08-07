# StockTrack

## Visão Geral

O StockTrack é uma plataforma de gestão e controle de estoque de ferramentas, desenvolvida para ambientes industriais que dependem de rastreabilidade rigorosa sobre o uso de equipamentos — como linhas de manutenção e montagem em contextos de manufatura de precisão.

O sistema foi projetado para que colaboradores consigam localizar, emprestar e devolver ferramentas de forma rápida e organizada, enquanto o setor responsável pelo almoxarifado mantém controle total sobre disponibilidade, estado de conservação, calibração e histórico completo de cada unidade física em estoque.

O objetivo do StockTrack não é apenas registrar entradas e saídas, mas garantir rastreabilidade absoluta: a qualquer momento, deve ser possível responder com precisão quem está com determinada ferramenta, quem a utilizou por último, e todo o histórico de ações realizadas sobre ela.

---

## O Problema

Ambientes industriais que lidam com ferramentas compartilhadas enfrentam dificuldades recorrentes:

- falta de controle sobre quem está com determinada ferramenta em um dado momento;
- dificuldade em localizar rapidamente uma ferramenta disponível;
- ausência de rastreabilidade sobre o histórico de uso e manutenção de cada unidade;
- risco de ferramentas descartadas ou com problemas continuarem em circulação por falta de controle de status;
- instrumentos de medição utilizados além do prazo de calibração, comprometendo a confiabilidade das medições realizadas;
- controle de estoque mínimo feito de forma manual ou inexistente, gerando rupturas inesperadas.

Em contextos onde a rastreabilidade não é opcional — é requisito de qualidade e segurança —, essas falhas representam risco operacional real, não apenas ineficiência administrativa.

---

## Nossa Solução

O StockTrack resolve esses problemas oferecendo:

- cadastro estruturado de ferramentas, organizadas por categoria, tipo e localização física;
- controle de disponibilidade em tempo real, com bloqueio automático de empréstimo para ferramentas indisponíveis;
- rastreabilidade completa: toda ação relevante sobre uma ferramenta é registrada de forma permanente e imutável, identificando quem a realizou, quando e por quê;
- controle de calibração para instrumentos de medição, com bloqueio automático de uso quando a calibração estiver vencida;
- alertas de estoque mínimo, calculados individualmente por tipo de ferramenta;
- perfis de acesso diferenciados, separando as responsabilidades operacionais do colaborador das responsabilidades administrativas do almoxarifado.

---

## Público-Alvo

O StockTrack foi desenvolvido para equipes operacionais de ambientes industriais que compartilham ferramentas e equipamentos entre colaboradores, com destaque para contextos onde a rastreabilidade e o controle de calibração são requisitos críticos — como manutenção e montagem de precisão.

Principais perfis de uso:

- colaboradores de linha, que retiram e devolvem ferramentas no dia a dia;
- responsáveis pelo almoxarifado, que administram o estoque, cadastram itens e mantêm o controle de calibração e disponibilidade.

---

## Objetivos

O projeto possui como principais objetivos:

- garantir rastreabilidade completa sobre o uso de ferramentas compartilhadas;
- reduzir o tempo gasto por colaboradores na localização de ferramentas disponíveis;
- eliminar o uso de instrumentos de medição com calibração vencida;
- automatizar o alerta de reposição de estoque;
- oferecer controle de acesso adequado às responsabilidades de cada perfil de usuário.

---

## Diferenciais

O StockTrack pretende se destacar por oferecer:

- rastreabilidade imutável de todas as ações realizadas sobre cada ferramenta;
- controle de calibração como regra de negócio ativa, não apenas informativa — bloqueando o uso de instrumentos vencidos;
- estoque mínimo calculado de forma individualizada por tipo de ferramenta, refletindo critérios reais de reposição (tempo de reposição, taxa de consumo, criticidade);
- estrutura hierárquica de localização física, permitindo busca precisa por corredor, prateleira e gaveta;
- arquitetura RESTful moderna, documentada e testada, preparada para evolução futura (kits de ferramentas, agendamento de empréstimos).

---

## Escopo da Versão 1

A primeira versão contemplará:

- autenticação de usuários com dois perfis (Colaborador e Almoxarife);
- cadastro de categorias, tipos de ferramenta e localizações físicas;
- cadastro de ferramentas (unidades físicas), individualmente ou em lote por tipo;
- empréstimo e devolução de ferramentas, com registro de motivo (empréstimo) e observação opcional (devolução);
- controle de status da ferramenta (Disponível, Em Uso, Em Manutenção, Descartada);
- controle de calibração para tipos de ferramenta que a exigem, com bloqueio automático de empréstimo quando vencida;
- estoque mínimo por tipo de ferramenta, com alerta visual para o Almoxarife;
- busca e filtros por nome, categoria, status e localização;
- log de auditoria imutável de todas as ações relevantes;
- documentação completa da API (OpenAPI/Swagger);
- arquitetura RESTful, autenticação JWT, Docker, GitHub Actions.

---

## Evoluções Futuras

Entre as funcionalidades planejadas para versões futuras estão:

- agendamento de empréstimos futuros, com verificação de conflito de horário;
- kits e maletas técnicas, como unidades compostas de empréstimo/devolução com checklist;
- código único (QR Code) por ferramenta para leitura rápida via aplicativo móvel;
- notificações ativas (e-mail) para ferramentas em atraso ou calibração próxima do vencimento;
- relatórios avançados de utilização e giro de estoque;
- Frontend completo (React), a ser avaliado conforme o cronograma do projeto.

---

## Princípios do Projeto

Todo desenvolvimento do StockTrack seguirá os seguintes princípios:

- rastreabilidade acima de tudo;
- simplicidade operacional para o colaborador;
- controle rigoroso para o almoxarifado;
- escalabilidade para futuras funcionalidades;
- código limpo, documentado e testado;
- segurança e responsabilidade sobre cada ação registrada.

---

## Missão

Garantir que nenhuma ferramenta compartilhada em um ambiente industrial seja utilizada, extraviada ou mantida em circulação sem rastreabilidade — protegendo tanto a operação quanto a qualidade do trabalho realizado.

---

## Visão

Ser uma referência em controle de estoque de ferramentas para ambientes industriais que não podem abrir mão de rastreabilidade e conformidade.

---

## Valores

- Rastreabilidade
- Responsabilidade
- Confiabilidade
- Simplicidade Operacional
- Qualidade
- Evolução Contínua

---

## Nota sobre Tecnologias e Ambiente de Desenvolvimento

O StockTrack será desenvolvido com a stack já validada no projeto anterior do autor (Monexus Finance), reaproveitando conhecimento técnico consolidado para maximizar velocidade de execução dentro do cronograma disponível:

- **Backend:** Java, Spring Boot, Spring Security (com autorização baseada em papéis — Colaborador e Almoxarife), Spring Data JPA, Hibernate, MySQL, Flyway, MapStruct.
- **Testes:** JUnit 5, Mockito, Testcontainers (testes unitários e de integração).
- **DevOps:** Docker e Docker Compose, GitHub Actions (CI/CD), OpenAPI/Swagger.
- **Frontend (avaliação futura):** React, Tailwind CSS — a ser iniciado somente após o Backend estar completo, e apenas se o cronograma permitir.

**Nota importante sobre o ambiente Docker:** todo o desenvolvimento ocorre em uma máquina Windows, com exceção do Docker, que roda exclusivamente em uma máquina virtual Ubuntu via VirtualBox, devido a uma limitação de virtualização da máquina host. O restante do projeto (IDE, código-fonte, execução local do Backend fora de containers) roda diretamente no Windows. Essa divisão de ambiente deve ser considerada em qualquer etapa que envolva configuração de Docker, Docker Compose ou Testcontainers, para evitar conflitos de configuração entre os dois sistemas.

---

## Sobre a Origem do Projeto

O StockTrack é um projeto pessoal e independente, desenvolvido para fins de estudo e portfólio. O domínio de negócio foi inspirado pela vivência prática do autor em um ambiente real de manufatura aeroespacial, mas o projeto **não é afiliado, patrocinado ou vinculado a nenhuma empresa específica** — trata-se de uma solução genérica para um problema real e recorrente em ambientes industriais que compartilham ferramentas entre colaboradores.
