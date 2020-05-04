
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';


CREATE SCHEMA IF NOT EXISTS `autobasedb` DEFAULT CHARACTER SET utf8 ;
USE `autobasedb` ;

-- -----------------------------------------------------
-- Table `autobasedb`.`role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `autobasedb`.`role` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `role` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `autobasedb`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `autobasedb`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `role_id` INT NOT NULL,
  `login` VARCHAR(20) NOT NULL,
  `password` VARCHAR(200) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `user_role_idx` (`role_id` ASC) VISIBLE,
  CONSTRAINT `user_role`
    FOREIGN KEY (`role_id`)
    REFERENCES `autobasedb`.`role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `autobasedb`.`driver`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `autobasedb`.`driver` (
  `user_id` INT NOT NULL,
  `passport` VARCHAR(45) NOT NULL,
  `telephone_number` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `driver_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `autobasedb`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `autobasedb`.`driver_shipping_request`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `autobasedb`.`driver_shipping_request` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `driver_id` INT NOT NULL,
  `carrying_capacity` INT NOT NULL,
  `passengers_capaity` INT NOT NULL,
  `vehicle_condition` VARCHAR(45) NOT NULL,
  `shipping_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `request_driver_idx` (`driver_id` ASC) VISIBLE,
  INDEX `request_shipping_idx` (`shipping_id` ASC) VISIBLE,
  CONSTRAINT `request_driver`
    FOREIGN KEY (`driver_id`)
    REFERENCES `autobasedb`.`driver` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `request_shipping`
    FOREIGN KEY (`shipping_id`)
    REFERENCES `autobasedb`.`shipping` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `autobasedb`.`firm`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `autobasedb`.`firm` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `autobasedb`.`car`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `autobasedb`.`car` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `car_model` VARCHAR(45) NOT NULL,
  `carrying_capacity` INT NOT NULL,
  `passengers_capacity` INT NOT NULL,
  `car_firm_id` INT NOT NULL,
  `statusCar` INT NOT NULL,
  `vehicle_condition` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `car_firm_idx` (`car_firm_id` ASC) VISIBLE,
  CONSTRAINT `car_firm`
    FOREIGN KEY (`car_firm_id`)
    REFERENCES `autobasedb`.`firm` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `autobasedb`.`shipping`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `autobasedb`.`shipping` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `dispatcher_id` INT NOT NULL,
  `status` VARCHAR(45) NOT NULL,
  `suitable_driver_shipping_request_id` INT NULL,
  `car_id` INT NULL,
  `arrival_city` VARCHAR(45) NOT NULL,
  `departure_city` VARCHAR(45) NOT NULL,
  `creation_timestamp` TIMESTAMP NOT NULL,
  `departure_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `shipping_dispatcher_idx` (`dispatcher_id` ASC) VISIBLE,
  INDEX `shipping_request_idx` (`suitable_driver_shipping_request_id` ASC) VISIBLE,
  INDEX `car_shipping_idx` (`car_id` ASC) VISIBLE,
  CONSTRAINT `shipping_dispatcher`
    FOREIGN KEY (`dispatcher_id`)
    REFERENCES `autobasedb`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `shipping_request`
    FOREIGN KEY (`suitable_driver_shipping_request_id`)
    REFERENCES `autobasedb`.`driver_shipping_request` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `car_shipping`
    FOREIGN KEY (`car_id`)
    REFERENCES `autobasedb`.`car` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
INSERT INTO role VALUES(DEFAULT, 'admin');
INSERT INTO role VALUES(DEFAULT, 'driver');
INSERT INTO role VALUES(DEFAULT, 'dispatcher');

INSERT INTO firm VALUES(DEFAULT, 'Chevrolet');

INSERT INTO users VALUES(DEFAULT,1, 'admin','admin','Vova', 'Sorokin');
INSERT INTO driver VALUES(2, '1231231AB', '+380943564545');
INSERT INTO driver VALUES(4, '1231231AB2', '+380943564546');
INSERT INTO shipping VALUES(DEFAULT,3, 'Open',NULL,NULL, 'testCity','testCity','2020/12/31 11*30*45','2020/12/31 11*30*45');
INSERT INTO car VALUES(DEFAULT,'Camaro', '1000000','10','1', '0','top');