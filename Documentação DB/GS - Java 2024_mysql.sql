create DATABASE wecare_db;
Use wecare_db;

CREATE TABLE users(
	id INT AUTO_INCREMENT PRIMARY KEY,
	nome VARCHAR(100) NOT NULL,
	idade INT NOT NULL,
	motivacao VARCHAR(255),
	email VARCHAR(150) UNIQUE NOT NULL,
	cpf VARCHAR(14) UNIQUE NOT NULL, 
	endereco VARCHAR(255),
	criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE goals (
	id INT AUTO_INCREMENT PRIMARY KEY,
	titulo VARCHAR(100) NOT NULL,
	descricao VARCHAR(255),
	is_completed BOOLEAN DEFAULT FALSE,
	user_id INT,
	criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
	
);

CREATE TABLE habits (
	id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(100),
	goal_id INT, 
	criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (goal_id) REFERENCES goals(id) ON DELETE CASCADE
)

-- Procedure para add usuário

DELIMITER // 

CREATE PROCEDURE insert_user(
	IN p_nome VARCHAR(100),
	IN p_idade INT,
	IN p_motivacao VARCHAR(255),
	IN p_email VARCHAR(150),
	IN p_cpf VARCHAR(14),
	IN p_endereco VARCHAR(255)
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		ROLLBACK;
		SELECT 'Erro ao inserir usuário. Verifique se o CPF ou email já existem.';
	END;
	START TRANSACTION;
	
	INSERT INTO users (nome, idade, motivacao, email, cpf, endereco)
	VALUES (p_nome, p_idade, p_motivacao, p_email, p_cpf, p_endereco);
	
	COMMIT;
	
END;

//
DELIMITER ;

-- Procedure para exportar metas como JSON

DELIMITER //

CREATE PROCEDURE export_goal(IN user_id INT)
BEGIN 
	SELECT 
		   JSON_OBJECT(
            'users', JSON_OBJECT('id', u.id, 'nome', u.nome),
            'goals', JSON_ARRAYAGG(
                JSON_OBJECT(
                    'id', g.id,
                    'titulo', g.titulo,
                    'descricao', g.descricao,
                    'is_completed', g.is_completed
                )
            )
        ) AS user_goals
	FROM users u
	JOIN goals g ON u.id = g.user_id
	WHERE u.id = user_id
	GROUP BY u.id;
END;
//
DELIMITER ;

-- Validando a idade

DELIMITER //

CREATE TRIGGER validate_idade_user
BEFORE INSERT ON users
FOR EACH ROW
BEGIN
    IF NEW.idade < 18 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'A idade deve ser 18 anos ou mais.';
    END IF;
END;
//

DELIMITER ;

-- Testando script

SHOW PROCEDURE STATUS WHERE Db = 'wecare_db';

CALL insert_user('John Doe', 25, 'Energia Sustentável', 'john.doe@gmail.com', '123.456.789-00', 'Se essa rua se essa rua fosse minha, 123');
Select * From users;

-- Inserindo dados Teste 
INSERT INTO users (nome, idade, motivacao, email, cpf, endereco)
VALUES ('Pain Doe', 21, 'Consumo consciente ', 'pain.doe@gmail.com', '456.123.789-01', 'Eu deixa eu deixa ela brilhar, 456');

INSERT INTO goals( titulo, descricao, is_completed, user_id)
VALUES ('Economizar Energia', 'Desligar luzes ao sair', FALSE, 1),
       ('Reduzir Consumo', 'Evitar uso de ar-condicionado', TRUE, 1);

SELECT * FROM goals WHERE user_id = 1;

CALL export_goal(1);