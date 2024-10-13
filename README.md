# Command to create DB
CREATE SCHEMA `ltm` ;

CREATE TABLE `ltm`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `win` INT NULL DEFAULT 0,
  `tie` INT NULL DEFAULT 0,
  `lose` INT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE);

CREATE TABLE `ltm`.`pair` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `image1` VARCHAR(45) NOT NULL,
  `image2` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE);

CREATE TABLE `ltm`.`point` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `x` INT NOT NULL,
  `y` INT NOT NULL,
  `pairId` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  INDEX `pairId_idx` (`pairId` ASC) VISIBLE,
  CONSTRAINT `pairId`
    FOREIGN KEY (`pairId`)
    REFERENCES `ltm`.`pair` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `ltm`.`game` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `userId1` INT NOT NULL,
  `userId2` INT NOT NULL,
  `pairId` INT NOT NULL,
  `score1` INT NULL DEFAULT 0,
  `score2` INT NULL DEFAULT 0,
  `state` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  INDEX `userId1_idx` (`userId1` ASC) VISIBLE,
  INDEX `userId2_idx` (`userId2` ASC) VISIBLE,
  INDEX `pairId_idx` (`pairId` ASC) VISIBLE,
  CONSTRAINT `fk_userId1`
    FOREIGN KEY (`userId1`)
    REFERENCES `ltm`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_userId2`
    FOREIGN KEY (`userId2`)
    REFERENCES `ltm`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_pairId`
    FOREIGN KEY (`pairId`)
    REFERENCES `ltm`.`pair` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE TABLE `ltm`.`click` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `x` INT NOT NULL,
  `y` INT NOT NULL,
  `userId` INT NOT NULL,
  `gameId` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  INDEX `gameId_idx` (`gameId` ASC) VISIBLE,
  CONSTRAINT `fk_userId`
    FOREIGN KEY (`userId`)
    REFERENCES `ltm`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_gameId`
    FOREIGN KEY (`gameId`)
    REFERENCES `ltm`.`game` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);