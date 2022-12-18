
CREATE TABLE  person (
  id INT NOT NULL AUTO_INCREMENT,
  username VARCHAR(45) NULL,
  password TEXT NULL,
  algorithm VARCHAR(45) NULL,
  PRIMARY KEY (id)
);

CREATE TABLE  authority (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(45) NULL,
  user_fk INT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE  product (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(45) NULL,
  price VARCHAR(45) NULL,
  currency VARCHAR(45) NULL,
  PRIMARY KEY (id)
);
