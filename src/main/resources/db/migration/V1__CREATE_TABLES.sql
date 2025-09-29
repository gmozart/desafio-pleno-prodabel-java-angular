-- Criação de enums
-- =========================================
CREATE TYPE cargo_funcionario AS ENUM ('ATENDENTE', 'GERENTE', 'SUPORTE');
CREATE TYPE status_solicitacao AS ENUM ('ABERTA', 'FINALIZADA');

-- =========================================
-- Criação de tabelas
-- =========================================

-- Tabela de bairros
CREATE TABLE bairro (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    cep VARCHAR(20) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    CONSTRAINT uq_bairro UNIQUE(nome, cep)
);

-- Índice para buscas rápidas por nome (case insensitive)
CREATE UNIQUE INDEX idx_bairro_nome ON bairro(LOWER(nome));

-- Tabela de usuários
CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL
);

-- Tabela de funcionários
CREATE TABLE funcionario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    cargo cargo_funcionario NOT NULL
);

-- Tabela de solicitações
CREATE TABLE solicitacao (
    id BIGSERIAL PRIMARY KEY,
    descricao TEXT NOT NULL,
    status status_solicitacao NOT NULL,
    data_criacao TIMESTAMP NOT NULL DEFAULT NOW(),

    usuario_id BIGINT NOT NULL,
    funcionario_id BIGINT,
    bairro_id BIGINT NOT NULL,

    CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id),
    CONSTRAINT fk_funcionario FOREIGN KEY (funcionario_id) REFERENCES funcionario (id),
    CONSTRAINT fk_bairro FOREIGN KEY (bairro_id) REFERENCES bairro (id)
);
