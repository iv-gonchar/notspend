-- -----------------------------------------------------
-- Schema notspenddb
-- -----------------------------------------------------
CREATE DATABASE IF NOT EXISTS `notspenddb`
  CHARACTER SET utf8
  COLLATE utf8_general_ci;

-- -----------------------------------------------------
-- Table `notspenddb`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `notspenddb`.`user` (
  `username` VARCHAR(50) NOT NULL UNIQUE,
  `email` VARCHAR(50) NOT NULL,
  `enabled` BOOLEAN NOT NULL,
  `name` VARCHAR(50) NULL,
  `password` VARCHAR(200) NOT NULL,
  `surname` VARCHAR(50) NULL,
  PRIMARY KEY (`username`),
  )
ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

-- -----------------------------------------------------
-- Table `notspenddb`.`authority`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `notspenddb`.`authority` (
  `username` VARCHAR(50) NOT NULL,
  `authority` VARCHAR(50) NOT NULL,
  CONSTRAINT `fk_authority_user`
    FOREIGN KEY (`username`)
    REFERENCES `notspenddb`.`user` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
  )
ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

-- -----------------------------------------------------
-- Table `notspenddb`.`currency`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `notspenddb`.`currency` (
  `code` VARCHAR(3) NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `number` INT,
  `symbol` VARCHAR(50),
  PRIMARY KEY (`code`)
  )
ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

-- -----------------------------------------------------
-- Table `notspenddb`.`mcc`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `notspenddb`.`mcc` (
  `mcc_id` INT NOT NULL,
  `description` VARCHAR(300),
  `category_name` VARCHAR(50),
  PRIMARY KEY (`mcc_id`)
  )
ENGINE = InnoDB CHARACTER SET cp1251 COLLATE cp1251_general_ci;

-- -----------------------------------------------------
-- Table `notspenddb`.`category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `notspenddb`.`category` (
  `category_id` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(50) NULL,
  `income` BOOLEAN DEFAULT FALSE,
  `name` VARCHAR(50) NOT NULL,
  `username` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`category_id`),
  CONSTRAINT `fk_category_user`
    FOREIGN KEY (`username`)
    REFERENCES `notspenddb`.`user` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
  )
ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

-- -----------------------------------------------------
-- Table `notspenddb`.`account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `notspenddb`.`account` (
  `account_id` INT NOT NULL AUTO_INCREMENT,
  `currency_code` VARCHAR(3) NOT NULL DEFAULT 'USD',
  `type` VARCHAR(50) NOT NULL,
  `summary` DECIMAL(18,2) NOT NULL,
  `description` VARCHAR(300) NOT NULL,
  `synchronization_id` VARCHAR(50),
  `synchronization_time` BIGINT,
  `synchronization_token` VARCHAR(50),
  `username` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`account_id`),
  CONSTRAINT `fk_account_user`
    FOREIGN KEY (`username`)
    REFERENCES `notspenddb`.`user` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_account_currency_code`
    FOREIGN KEY (`currency_code`)
    REFERENCES `notspenddb`.`currency` (`code`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  )
ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

-- -----------------------------------------------------
-- Table `notspenddb`.`expense`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `notspenddb`.`expense` (
  `account_id` INT(11) NOT NULL,
  `currency_code` VARCHAR(3) NOT NULL DEFAULT 'USD',
  `expense_id` INT NOT NULL AUTO_INCREMENT,
  `sum` DECIMAL(18,2) NOT NULL,
  `category_id` INT NOT NULL,
  `date` DATE NOT NULL,
  `time` TIME(0) NOT NULL,
  `comment` VARCHAR(50) NULL,
  `username` VARCHAR(50) NOT NULL
  PRIMARY KEY (`expense_id`),
  INDEX `fk_expense_category1_idx` (`category_id` ASC),
  CONSTRAINT `fk_account_id`
    FOREIGN KEY (`account_id`)
    REFERENCES `notspenddb`.`account` (`account_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_expense_currency_code`
    FOREIGN KEY (`currency_code`)
    REFERENCES `notspenddb`.`currency` (`code`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_expense_category1`
    FOREIGN KEY (`category_id`)
    REFERENCES `notspenddb`.`category` (`category_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_expense_user`
    FOREIGN KEY (`username`)
    REFERENCES `notspenddb`.`user` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
  )
ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;