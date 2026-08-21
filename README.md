# StockTrack

Plataforma de gestão e controle de estoque de ferramentas para ambientes industriais, com foco em rastreabilidade completa sobre o uso de equipamentos compartilhados.

Projeto pessoal e independente, desenvolvido para fins de estudo e portfólio. Não é afiliado, patrocinado ou vinculado a nenhuma empresa específica — o domínio de negócio foi inspirado pela vivência prática do autor em ambiente real de manufatura aeroespacial.

---

## Status do Projeto

✅ Backend completo — todos os módulos de domínio implementados e funcionais, ponta a ponta.

---

## Sobre o Sistema

O StockTrack permite que colaboradores localizem, emprestem e devolvam ferramentas de forma rápida e rastreável, enquanto o Almoxarife mantém controle total sobre disponibilidade, calibração de instrumentos e estoque mínimo.

Principais capacidades:

- Autenticação via JWT, com dois perfis de acesso (Colaborador e Almoxarife);
- Cadastro de categorias, tipos de ferramenta e localizações físicas;
- Cadastro e rastreamento de unidades individuais de ferramenta, com número de série único;
- Empréstimo e devolução, com bloqueio automático de ferramentas indisponíveis ou com calibração vencida;
- Controle de calibração por tipo de ferramenta, com intervalo configurável e bloqueio automático de uso quando vencida;
- Alerta de estoque mínimo, calculado dinamicamente por tipo de ferramenta;
- Log de auditoria imutável, alimentado automaticamente via eventos de domínio — toda ação relevante sobre uma ferramenta é registrada sem que os módulos de origem precisem conhecer o módulo de auditoria;
- Documentação completa da API via OpenAPI/Swagger.

---

## Módulos

| Módulo | Responsabilidade |
|---|---|
| `user` | Autenticação, autocadastro de Colaborador, cadastro administrativo de Almoxarife |
| `category` | Classificação de Tipos de Ferramenta |
| `tooltype` | Modelo/tipo de ferramenta, estoque mínimo, exigência e intervalo de calibração |
| `location` | Catálogo de posições físicas (corredor/prateleira/gaveta) |
| `tool` | Unidade física individual, status, calibração |
| `loan` | Ciclo de empréstimo e devolução |
| `auditlog` | Trilha de auditoria imutável, alimentada via eventos de domínio |
| `shared` | Configuração, segurança (JWT), tratamento de exceções, utilitários |

---

## Stack Tecnológica

**Backend:** Java 25, Spring Boot, Spring Security (JWT), Spring Data JPA, Hibernate, MySQL, Flyway, MapStruct, Lombok.

**Documentação:** OpenAPI / Swagger.

---

## Documentação Técnica

Toda a documentação de arquitetura e regras de negócio vive em [`docs/`](./docs), mantida atualizada ao longo de todo o desenvolvimento:

| Documento | Conteúdo |
|---|---|
| [`01-Project-Vision.md`](./docs/01-Project-Vision.md) | Visão geral, problema, solução, escopo |
| [`02-Software-Architecture.md`](./docs/02-Software-Architecture.md) | Arquitetura em camadas, Package by Feature, eventos de domínio |
| [`03-Business-Rules.md`](./docs/03-Business-Rules.md) | Regras de negócio de todos os módulos |
| [`04-Domain-Model.md`](./docs/04-Domain-Model.md) | Entidades, atributos, relacionamentos |
| [`05-Database-Design.md`](./docs/05-Database-Design.md) | Schema do banco MySQL |
| [`06-API-Specification.md`](./docs/06-API-Specification.md) | Contrato de todos os endpoints REST |
| [`08-Development-Standards.md`](./docs/08-Development-Standards.md) | Padrões de código, commits e qualidade |

---

## Como Rodar Localmente

**Pré-requisitos:** Java 25, Maven, MySQL rodando localmente.

1. Crie um schema `stocktrack` no MySQL (ou deixe a aplicação criar automaticamente).
2. Defina as variáveis de ambiente:

DB_USER=<usuário do MySQL>
DB_PASSWORD=<senha do MySQL>
JWT_SECRET=<chave Base64 de 256 bits>

3. Rode a aplicação (`mvn spring-boot:run` ou pela IDE). O Flyway aplica as migrations automaticamente, incluindo o seed do primeiro Almoxarife (ver `V2__seed_first_warehouse_manager.sql` para as credenciais iniciais).
4. Acesse a documentação interativa em:

http://localhost:8080/api/v1/swagger-ui/index.html


---

## Segurança

- Autenticação stateless via JWT (token expira em 1 hora, sem refresh token na V1);
- Senhas com hash BCrypt;
- Autorização por papel (`COLLABORATOR` / `WAREHOUSE_MANAGER`) via `@PreAuthorize`;
- Respostas de erro padronizadas em todos os fluxos de autenticação/autorização.

---

## Autor

**Breno Oliveira de Souza**