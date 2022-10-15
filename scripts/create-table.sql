USE datajob;

CREATE TABLE pessoa
(
    id SERIAL,
    name varchar(50),
    cpf varchar(11),
    CONSTRAINT pessoa_pkey PRIMARY KEY (id)
);

INSERT INTO pessoa(name, cpf) VALUES
 ('Gabriela', '11111111111'),
 ('Henrique', '2222222222');