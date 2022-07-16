CREATE TABLE IF NOT EXISTS roles(
	id BIGSERIAL,
	name VARCHAR NOT NULL,
	
	CONSTRAINT role_pk PRIMARY KEY (id),
	CONSTRAINT role_name_unique UNIQUE (name)
);

INSERT INTO roles (id, name) VALUES (nextval('roles_id_seq'), 'ADMIN');
INSERT INTO roles (id, name) VALUES (nextval('roles_id_seq'), 'USER');