CREATE TABLE IF NOT EXISTS roles(
	id BIGSERIAL,
	authority VARCHAR NOT NULL,
	
	CONSTRAINT role_pk PRIMARY KEY (id),
	CONSTRAINT authority_unique UNIQUE (authority)
);

INSERT INTO roles (id, authority) VALUES (nextval('roles_id_seq'), 'ADMIN');
INSERT INTO roles (id, authority) VALUES (nextval('roles_id_seq'), 'USER');