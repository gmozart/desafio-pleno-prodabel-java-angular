-- =========================================
-- Atualizar dados de teste com senhas
-- =========================================

-- Atualizar senhas dos usuários existentes
-- Senha: "senha123" (hash BCrypt)
UPDATE usuario SET senha = '$2a$10$N9qo8uLOickgx2ZMRZoMye2J.JYm5lqUKl/7M6vKbBGQBWXNjzKSO' WHERE email = 'alice@email.com';
UPDATE usuario SET senha = '$2a$10$N9qo8uLOickgx2ZMRZoMye2J.JYm5lqUKl/7M6vKbBGQBWXNjzKSO' WHERE email = 'bruno@email.com';
UPDATE usuario SET senha = '$2a$10$N9qo8uLOickgx2ZMRZoMye2J.JYm5lqUKl/7M6vKbBGQBWXNjzKSO' WHERE email = 'carla@email.com';

-- Atualizar email e senhas dos funcionários existentes
-- Senha: "senha123" (hash BCrypt)
UPDATE funcionario SET email = 'joao@email.com', senha = '$2a$10$N9qo8uLOickgx2ZMRZoMye2J.JYm5lqUKl/7M6vKbBGQBWXNjzKSO' WHERE nome = 'João Carlos';
UPDATE funcionario SET email = 'mariana@email.com', senha = '$2a$10$N9qo8uLOickgx2ZMRZoMye2J.JYm5lqUKl/7M6vKbBGQBWXNjzKSO' WHERE nome = 'Mariana Rocha';
UPDATE funcionario SET email = 'ricardo@email.com', senha = '$2a$10$N9qo8uLOickgx2ZMRZoMye2J.JYm5lqUKl/7M6vKbBGQBWXNjzKSO' WHERE nome = 'Ricardo Lima';

