-- Script para criação completa do banco de dados `wecare_db`
-- Inclui tabelas, rotinas, e correções de tipos

DROP SCHEMA IF EXISTS wecare_db;
CREATE SCHEMA wecare_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE wecare_db;

-- Tabela Users
CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    idade INT NOT NULL CHECK (idade >= 18),
    motivacao VARCHAR(255) DEFAULT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    endereco VARCHAR(255) DEFAULT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Tabela Goals
CREATE TABLE goals (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descricao VARCHAR(255) DEFAULT NULL,
    is_completed TINYINT(1) DEFAULT 0,
    user_id BIGINT NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Tabela Habits
CREATE TABLE habits (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    goal_id BIGINT NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (goal_id) REFERENCES goals(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Tabela Logs (para histórico de alterações)
CREATE TABLE logs (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    action VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- População inicial de dados
INSERT INTO users (nome, idade, motivacao, email, cpf, endereco) 
VALUES ('Nitro Santos', 30, 'Inserindo através do banco', 'db.doe@example.com', '123.756.789-00', 'Rua Verde, 123');


INSERT INTO goals (titulo, descricao, is_completed, user_id) 
VALUES ('Economizar Energia', 'Desligar luzes ao sair', 0, 1);

INSERT INTO habits (nome, goal_id) 
VALUES ('Desligar luzes ao sair', 1);

-- Função para validar CPF
DELIMITER $$
CREATE FUNCTION validate_cpf(p_cpf VARCHAR(14)) RETURNS TINYINT(1)
DETERMINISTIC
BEGIN
    RETURN p_cpf REGEXP '^[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}-[0-9]{2}$';
END$$
DELIMITER ;

-- Procedure para inserir usuário
DELIMITER $$
CREATE PROCEDURE insert_user(
    IN p_nome VARCHAR(100),
    IN p_idade INT,
    IN p_motivacao VARCHAR(255),
    IN p_email VARCHAR(150),
    IN p_cpf VARCHAR(14),
    IN p_endereco VARCHAR(255)
)
BEGIN
    -- Valida CPF
    IF validate_cpf(p_cpf) = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'CPF inválido. Formato esperado: XXX.XXX.XXX-XX';
    END IF;

    -- Insere usuário
    INSERT INTO users (nome, idade, motivacao, email, cpf, endereco)
    VALUES (p_nome, p_idade, p_motivacao, p_email, p_cpf, p_endereco);
END$$
DELIMITER ;

-- Procedure para exportar metas em JSON
DELIMITER $$
CREATE PROCEDURE export_goals(
    IN p_user_id BIGINT,
    OUT p_result JSON
)
BEGIN
    SELECT JSON_OBJECT(
        'user', JSON_OBJECT('id', u.id, 'nome', u.nome),
        'goals', JSON_ARRAYAGG(
            JSON_OBJECT(
                'id', g.id,
                'titulo', g.titulo,
                'descricao', g.descricao,
                'is_completed', g.is_completed
            )
        )
    )
    INTO p_result
    FROM users u
    LEFT JOIN goals g ON u.id = g.user_id
    WHERE u.id = p_user_id;
END$$
DELIMITER ;

-- Procedure para inserir meta
DELIMITER $$
CREATE PROCEDURE insert_goal(
    IN p_titulo VARCHAR(100),
    IN p_descricao VARCHAR(255),
    IN p_user_id BIGINT
)
BEGIN
    -- Verifica se o usuário existe
    IF NOT EXISTS (SELECT 1 FROM users WHERE id = p_user_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Usuário não encontrado.';
    END IF;

    -- Insere meta
    INSERT INTO goals (titulo, descricao, user_id)
    VALUES (p_titulo, p_descricao, p_user_id);
END$$
DELIMITER ;

-- Procedure para inserir hábito
DELIMITER $$
CREATE PROCEDURE insert_habit(
    IN p_nome VARCHAR(100),
    IN p_goal_id BIGINT
)
BEGIN
    -- Verifica se a meta existe
    IF NOT EXISTS (SELECT 1 FROM goals WHERE id = p_goal_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Meta não encontrada.';
    END IF;

    -- Insere hábito
    INSERT INTO habits (nome, goal_id)
    VALUES (p_nome, p_goal_id);
END$$
DELIMITER ;

-- Triggers
-- Trigger para validar idade
DELIMITER $$
CREATE TRIGGER validate_age_before_insert
BEFORE INSERT ON users
FOR EACH ROW
BEGIN
    IF NEW.idade < 18 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Idade deve ser maior ou igual a 18.';
    END IF;
END$$
DELIMITER ;

-- Trigger para log de alterações em goals
DELIMITER $$
CREATE TRIGGER after_goal_insert
AFTER INSERT ON goals
FOR EACH ROW
BEGIN
    INSERT INTO logs (action, description)
    VALUES ('INSERT', CONCAT('Meta criada com ID: ', NEW.id));
END$$
DELIMITER ;

-- Trigger para impedir exclusão de metas com hábitos associados
DELIMITER $$
CREATE TRIGGER before_goal_delete
BEFORE DELETE ON goals
FOR EACH ROW
BEGIN
    IF EXISTS (SELECT 1 FROM habits WHERE goal_id = OLD.id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Não é possível excluir uma meta com hábitos associados.';
    END IF;
END$$
DELIMITER ;

-- Trigger para log de alterações em hábitos
DELIMITER $$
CREATE TRIGGER after_habit_insert
AFTER INSERT ON habits
FOR EACH ROW
BEGIN
    INSERT INTO logs (action, description)
    VALUES ('INSERT', CONCAT('Hábito criado com ID: ', NEW.id));
END$$
DELIMITER ;

DELETE FROM users WHERE id = 1;
DELETE FROM goals WHERE id = 1;
DELETE FROM habits WHERE id = 1;


SELECT * FROM users;
SELECT * FROM goals;
SELECT * FROM habits;
SHOW PROCEDURE STATUS WHERE Db = 'wecare_db';
DROP PROCEDURE IF EXISTS insert_user;
ALTER TABLE users MODIFY COLUMN cpf VARCHAR(14);

