-- Seed do primeiro WAREHOUSE_MANAGER do sistema.
-- Não existe fluxo de "promoção" de usuário via API na V1 (ver 03-Business-Rules.md,
-- seção Usuário > Criação de Conta) — este é o único ponto de entrada para o primeiro Almoxarife.
--
-- Credenciais iniciais (TROCAR IMEDIATAMENTE após o primeiro login, via PUT /users/me):
-- RE: 00000001
-- Senha: Stock@2026

INSERT INTO users (name, re, area, password, role, created_at, updated_at)
VALUES (
    'Administrador',
    '00000001',
    'Almoxarifado',
    '$2b$10$0WiVsMDk/h33QZ65fhs9WOQ9ohG4gf23HepERWWdcO3B0BDL2n9cm',
    'WAREHOUSE_MANAGER',
    NOW(),
    NOW()
       );