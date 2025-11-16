-- Adicionar campo email sem restrição UNIQUE inicialmente
ALTER TABLE funcionario ADD COLUMN email VARCHAR(150);

-- Preencher os emails já existentes com valor único
UPDATE funcionario SET email = CONCAT('funcionario', id, '@example.com') WHERE email IS NULL;

-- Alterar para NOT NULL
ALTER TABLE funcionario ALTER COLUMN email SET NOT NULL;

-- Adicionar restrição UNIQUE
ALTER TABLE funcionario ADD CONSTRAINT funcionario_email_unique UNIQUE(email);

-- Adicionar campo senha
ALTER TABLE funcionario ADD COLUMN senha VARCHAR(255) NOT NULL DEFAULT '$2a$10$defaultHashedPassword';

-- Adicionar campo senha para usuario
ALTER TABLE usuario ADD COLUMN senha VARCHAR(255) NOT NULL DEFAULT '$2a$10$defaultHashedPassword123456789012345678901234567890';