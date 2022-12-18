INSERT INTO person (id, username, password, algorithm) 
	VALUES ('1', 'john', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'BCRYPT');
INSERT INTO person (id, username, password, algorithm) 
	VALUES ('2', 'jane', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'BCRYPT');

INSERT INTO authority (id, name, user_fk) 
	VALUES ('1', 'READ', '1');
INSERT INTO authority (id, name, user_fk) 
	VALUES ('2', 'READ', '2');
INSERT INTO authority (id, name, user_fk) 
	VALUES ('3', 'WRITE', '2');

INSERT INTO product (id, name, price, currency) 
	VALUES ('1', 'Chocolate', '10', 'EUR');
INSERT INTO product (id, name, price, currency) 
	VALUES ('2', 'Sugar', '5', 'EUR');
INSERT INTO product (id, name, price, currency) 
	VALUES ('3', 'Milk', '1', 'EUR');
