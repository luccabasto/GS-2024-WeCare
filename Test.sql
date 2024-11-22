-- MySQL Script generated by MySQL Workbench
-- Fri Nov 22 11:47:13 2024
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema wecare_db
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema wecare_db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `wecare_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `wecare_db` ;

-- -----------------------------------------------------
-- Table `wecare_db`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wecare_db`.`users` ;

CREATE TABLE IF NOT EXISTS `wecare_db`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(100) NOT NULL,
  `idade` INT NOT NULL,
  `motivacao` VARCHAR(255) NULL DEFAULT NULL,
  `email` VARCHAR(150) NOT NULL,
  `cpf` VARCHAR(14) NOT NULL,
  `endereco` VARCHAR(255) NULL DEFAULT NULL,
  `criado_em` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email` (`email` ASC) VISIBLE,
  UNIQUE INDEX `cpf` (`cpf` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `wecare_db`.`goals`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wecare_db`.`goals` ;

CREATE TABLE IF NOT EXISTS `wecare_db`.`goals` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `titulo` VARCHAR(100) NOT NULL,
  `descricao` VARCHAR(255) NULL DEFAULT NULL,
  `is_completed` TINYINT(1) NULL DEFAULT '0',
  `user_id` INT NULL DEFAULT NULL,
  `criado_em` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `user_id` (`user_id` ASC) VISIBLE,
  CONSTRAINT `goals_ibfk_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `wecare_db`.`users` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `wecare_db`.`habits`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wecare_db`.`habits` ;

CREATE TABLE IF NOT EXISTS `wecare_db`.`habits` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NULL DEFAULT NULL,
  `goal_id` INT NULL DEFAULT NULL,
  `criado_em` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `goal_id` (`goal_id` ASC) VISIBLE,
  CONSTRAINT `habits_ibfk_1`
    FOREIGN KEY (`goal_id`)
    REFERENCES `wecare_db`.`goals` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

USE `wecare_db` ;

-- -----------------------------------------------------
-- procedure export_goal
-- -----------------------------------------------------

USE `wecare_db`;
DROP procedure IF EXISTS `wecare_db`.`export_goal`;

DELIMITER $$
USE `wecare_db`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `export_goal`(IN user_id INT)
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
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure insert_user
-- -----------------------------------------------------

USE `wecare_db`;
DROP procedure IF EXISTS `wecare_db`.`insert_user`;

DELIMITER $$
USE `wecare_db`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_user`(
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
		-- Me retorna uma mensagem de erro caso o CPF ou email sejam duplicados
		ROLLBACK;
		SELECT 'Erro ao inserir usuário. Verifique se o CPF ou email já existem.';
	END;
	START TRANSACTION;
	
	INSERT INTO users (nome, idade, motivacao, email, cpf, endereco)
	VALUES (p_nome, p_idade, p_motivacao, p_email, p_cpf, p_endereco);
	
	COMMIT;
	
END$$

DELIMITER ;

-- -----------------------------------------------------
-- function validate_cpf
-- -----------------------------------------------------

USE `wecare_db`;
DROP function IF EXISTS `wecare_db`.`validate_cpf`;

DELIMITER $$
USE `wecare_db`$$
CREATE DEFINER=`root`@`localhost` FUNCTION `validate_cpf`(p_cpf VARCHAR(14)) RETURNS tinyint(1)
    DETERMINISTIC
BEGIN
	DECLARE valid_format BOOLEAN;
	SET valid_format = p_cpf REGEXP '^[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}-[0-9]{2}$';
	RETURN valid_format;
    
END$$

DELIMITER ;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
USE `wecare_db`;

DELIMITER $$

USE `wecare_db`$$
DROP TRIGGER IF EXISTS `wecare_db`.`validate_idade_user` $$
USE `wecare_db`$$
CREATE
DEFINER=`root`@`localhost`
TRIGGER `wecare_db`.`validate_idade_user`
BEFORE INSERT ON `wecare_db`.`users`
FOR EACH ROW
BEGIN
    IF NEW.idade < 18 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'A idade deve ser 18 anos ou mais.';
    END IF;
END$$


USE `wecare_db`$$
DROP TRIGGER IF EXISTS `wecare_db`.`validate_cpf` $$
USE `wecare_db`$$
CREATE TRIGGER validate_cpf
BEFORE UPDATE on users
FOR EACH ROW
BEGIN
    IF NEW.cpf NOT REGEXP '^[0-9]{3}\.[0-9]{3}\.[0-9]{3}-[0-9]{2}$' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'CPF inválido. Formato esperado: XXX.XXX.XXX-XX';
    END IF;
END;$$


USE `wecare_db`$$
DROP TRIGGER IF EXISTS `wecare_db`.`validate_idade` $$
USE `wecare_db`$$
CREATE TRIGGER validate_idade
BEFORE INSERT ON users
FOR EACH ROW
BEGIN
    IF NEW.idade < 18 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Idade deve ser maior ou igual a 18.';
    END IF;
END;$$


USE `wecare_db`$$
DROP TRIGGER IF EXISTS `wecare_db`.`log_insert` $$
USE `wecare_db`$$
CREATE TRIGGER log_insert
AFTER INSERT ON users
FOR EACH ROW
BEGIN
    INSERT INTO logs (action, description, created_at)
    VALUES ('INSERT', CONCAT('Usuário inserido com ID: ', NEW.id), NOW());
END;$$


USE `wecare_db`$$
DROP TRIGGER IF EXISTS `wecare_db`.`validate_cpf` $$
USE `wecare_db`$$
CREATE TRIGGER validate_cpf
BEFORE UPDATE on users
FOR EACH ROW
BEGIN
    IF NEW.cpf NOT REGEXP '^[0-9]{3}\.[0-9]{3}\.[0-9]{3}-[0-9]{2}$' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'CPF inválido. Formato esperado: XXX.XXX.XXX-XX';
    END IF;
END;$$


USE `wecare_db`$$
DROP TRIGGER IF EXISTS `wecare_db`.`after_goal_insert` $$
USE `wecare_db`$$
CREATE TRIGGER after_goal_insert
AFTER INSERT ON goals
FOR EACH ROW
BEGIN
    INSERT INTO audit_logs (table_name, action, timestamp)
    VALUES ('goals', 'INSERT', NOW());
END;$$


USE `wecare_db`$$
DROP TRIGGER IF EXISTS `wecare_db`.`after_goal_update` $$
USE `wecare_db`$$
CREATE TRIGGER after_goal_update
AFTER UPDATE ON goals
FOR EACH ROW
BEGIN
    INSERT INTO audit_logs (table_name, action, timestamp)
    VALUES ('goals', 'UPDATE', NOW());
END;$$


USE `wecare_db`$$
DROP TRIGGER IF EXISTS `wecare_db`.`before_goal_delete` $$
USE `wecare_db`$$
CREATE TRIGGER before_goal_delete
BEFORE DELETE ON goals
FOR EACH ROW
BEGIN
    IF EXISTS (SELECT 1 FROM habits WHERE goal_id = OLD.id) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Cannot delete goal with associated habits.';
    END IF;
END;$$


USE `wecare_db`$$
DROP TRIGGER IF EXISTS `wecare_db`.`after_habit_insert` $$
USE `wecare_db`$$
CREATE TRIGGER after_habit_insert
AFTER INSERT ON habits
FOR EACH ROW
BEGIN
    INSERT INTO audit_logs (table_name, action, timestamp)
    VALUES ('habits', 'INSERT', NOW());
END;$$


USE `wecare_db`$$
DROP TRIGGER IF EXISTS `wecare_db`.`before_habit_delete` $$
USE `wecare_db`$$
CREATE TRIGGER before_habit_delete
BEFORE DELETE ON habits
FOR EACH ROW
BEGIN
    IF EXISTS (SELECT 1 FROM goals WHERE id = OLD.goal_id AND is_completed = 0) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Cannot delete habit associated with an active goal.';
    END IF;
END;$$


USE `wecare_db`$$
DROP TRIGGER IF EXISTS `wecare_db`.`after_habit_update` $$
USE `wecare_db`$$
CREATE TRIGGER after_habit_update
AFTER UPDATE ON habits
FOR EACH ROW
BEGIN
    INSERT INTO audit_logs (table_name, action, timestamp)
    VALUES ('habits', 'UPDATE', NOW());
END;$$


DELIMITER ;