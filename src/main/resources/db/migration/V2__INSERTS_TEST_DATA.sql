-- =========================================
-- Inserts iniciais
-- =========================================

-- Bairros
INSERT INTO bairro (nome, cep, cidade, estado) VALUES
('Centro', '28010-000', 'Campos dos Goytacazes', 'RJ'),
('Jardim América', '28020-000', 'Campos dos Goytacazes', 'RJ'),
('Vila Nova', '28040-000', 'Campos dos Goytacazes', 'RJ');

-- Usuários
INSERT INTO usuario (nome, email, bairro) VALUES
('Alice Silva', 'alice@email.com', 'Centro'),
('Bruno Souza', 'bruno@email.com', 'Jardim América'),
('Carla Pereira', 'carla@email.com', 'Vila Nova');

-- Funcionários
INSERT INTO funcionario (nome, cargo) VALUES
('João Carlos', 'ATENDENTE'),
('Mariana Rocha', 'GERENTE'),
('Ricardo Lima', 'SUPORTE');

-- Solicitações
INSERT INTO solicitacao (descricao, status, bairro_id, usuario_id, funcionario_id) VALUES
('Solicitação de informação sobre documentos', 'ABERTA', 1, 1, 1),
('Reclamação sobre atendimento', 'ABERTA', 2, 2, 2),
('Solicitação de suporte técnico', 'FINALIZADA', 3, 3, 3);
