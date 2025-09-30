-- =========================================
-- Criação de tabelas
-- =========================================

-- Tabela de bairros
CREATE TABLE bairro (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cep VARCHAR(10) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    estado VARCHAR(2) NOT NULL
);

-- Tabela de usuários
CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    bairro_id BIGINT NOT NULL,
    CONSTRAINT fk_usuario_bairro FOREIGN KEY (bairro_id) REFERENCES bairro(id)
);

-- Tabela de funcionários
CREATE TABLE funcionario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    cargo VARCHAR(50) NOT NULL
);

-- Tabela de solicitações
CREATE TABLE solicitacao (
    id BIGSERIAL PRIMARY KEY,
    descricao TEXT NOT NULL,
    status VARCHAR(50) NOT NULL,
    bairro_id BIGINT NOT NULL,
    data_criacao TIMESTAMP NOT NULL DEFAULT NOW(),
    usuario_id BIGINT NOT NULL,
    funcionario_id BIGINT,

    CONSTRAINT fk_solicitacao_bairro FOREIGN KEY (bairro_id) REFERENCES bairro(id),
    CONSTRAINT fk_solicitacao_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    CONSTRAINT fk_solicitacao_funcionario FOREIGN KEY (funcionario_id) REFERENCES funcionario(id)
);
