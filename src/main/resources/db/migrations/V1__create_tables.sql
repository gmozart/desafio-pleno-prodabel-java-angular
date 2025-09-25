-- Tabela de usuários (cidadãos)
CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    bairro VARCHAR(100) NOT NULL
);

-- Tabela de funcionários
CREATE TABLE funcionario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    cargo VARCHAR(100) NOT NULL
);

-- Tabela de solicitações
CREATE TABLE solicitacao (
    id BIGSERIAL PRIMARY KEY,
    descricao TEXT NOT NULL,
    data_abertura TIMESTAMP NOT NULL DEFAULT NOW(),
    status VARCHAR(50) NOT NULL,

    usuario_id BIGINT NOT NULL,
    funcionario_id BIGINT,

    CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id),
    CONSTRAINT fk_funcionario FOREIGN KEY (funcionario_id) REFERENCES funcionario (id)
);