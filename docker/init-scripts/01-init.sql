-- Script de inicialização do banco de dados
-- Este script roda automaticamente quando o container do PostgreSQL é criado pela primeira vez

-- Configurar timezone
SET timezone = 'America/Sao_Paulo';

-- Garantir que o banco existe
SELECT 'CREATE DATABASE gestao_atendimentos'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'gestao_atendimentos')\gexec

-- Conectar ao banco
\c gestao_atendimentos;

-- Log de inicialização
DO $$
BEGIN
    RAISE NOTICE 'Banco de dados gestao_atendimentos inicializado com sucesso!';
END $$;
