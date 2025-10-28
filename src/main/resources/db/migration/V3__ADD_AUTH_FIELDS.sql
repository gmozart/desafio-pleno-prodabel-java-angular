-- =========================================
-- Adicionar campos de autenticação
-- =========================================

-- Adicionar campo senha na tabela usuario
ALTER TABLE usuario ADD COLUMN senha VARCHAR(255) NOT NULL DEFAULT '$2a$10$defaultHashedPassword123456789012345678901234567890';

-- Adicionar campos email e senha na tabela funcionario
ALTER TABLE funcionario ADD COLUMN email VARCHAR(150) UNIQUE NOT NULL DEFAULT 'funcionario@example.com';
ALTER TABLE funcionario ADD COLUMN senha VARCHAR(255) NOT NULL DEFAULT '$2a$10$defaultHashedPassword123456789012345678901234567890';

