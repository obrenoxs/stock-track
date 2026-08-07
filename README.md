# StockTrack

> ⚠️ **Este README é temporário**, mantido apenas durante o desenvolvimento. Será substituído por uma versão completa (com badges, instruções de instalação definitivas, screenshots etc.) próximo à conclusão do projeto.

Plataforma de gestão e controle de estoque de ferramentas para ambientes industriais, com foco em rastreabilidade completa sobre o uso de equipamentos compartilhados.

Projeto pessoal e independente, desenvolvido para fins de estudo e portfólio. Não é afiliado, patrocinado ou vinculado a nenhuma empresa específica.

---

## Status do Projeto

🚧 Em desenvolvimento — fase inicial (setup do projeto).

---

## Documentação

Toda a documentação técnica do projeto vive em [`docs/`](./docs), e é atualizada **antes** de qualquer implementação correspondente:

| Documento | Conteúdo |
|---|---|
| [`01-Project-Vision.md`](./docs/01-Project-Vision.md) | Visão geral, problema, solução, escopo da V1 |
| [`02-Software-Architecture.md`](./docs/02-Software-Architecture.md) | Arquitetura em camadas, stack, Package by Feature, eventos de domínio |
| [`03-Business-Rules.md`](./docs/03-Business-Rules.md) | Regras de negócio de todos os módulos |
| [`04-Domain-Model.md`](./docs/04-Domain-Model.md) | Entidades, atributos, relacionamentos |
| [`05-Database-Design.md`](./docs/05-Database-Design.md) | Schema do banco MySQL |
| [`06-API-Specification.md`](./docs/06-API-Specification.md) | Contrato de todos os endpoints REST |
| [`08-Development-Standards.md`](./docs/08-Development-Standards.md) | Padrões de código, commits e qualidade |

> `07-Frontend-*.md` será criado quando o Frontend entrar em pauta (avaliação futura, após o Backend estar completo).

---

## Stack Tecnológica

**Backend:** Java 25, Spring Boot, Spring Security, JWT, Spring Data JPA, Hibernate, MySQL, Flyway, MapStruct.

**Testes:** JUnit 5, Mockito, Testcontainers.

**DevOps:** Docker, Docker Compose, GitHub Actions (CI/CD).

**Documentação:** OpenAPI / Swagger.

**Frontend (avaliação futura):** React, Tailwind CSS.

---

## Como Rodar (em construção)

Instruções de setup local (Docker Compose, variáveis de ambiente, migrations) serão adicionadas aqui conforme o projeto avança.

**Nota sobre o ambiente:** o desenvolvimento ocorre em uma máquina Windows; o Docker roda exclusivamente em uma VM Ubuntu via VirtualBox.

---

## Metodologia de Desenvolvimento

- Documentar antes de implementar — nenhuma regra de negócio ou decisão estrutural é codificada sem estar registrada antes em `docs/`.
- Fluxo de implementação em camadas: Migration → Entity → DTO → Mapper → Repository → Exception (se necessário) → Service → Controller.
- Commits granulares, seguindo Conventional Commits (`feat:`, `fix:`, `refactor:`, `docs:`, etc.), anunciados ao final de cada fatia coesa de implementação.

---

## Autor

**Breno Oliveira de Souza**