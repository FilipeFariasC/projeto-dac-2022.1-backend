CREATE TABLE IF NOT EXISTS roles(
	role_id BIGSERIAL,
	authority VARCHAR NOT NULL,
	
	CONSTRAINT role_pk PRIMARY KEY (role_id),
	CONSTRAINT authority_unique UNIQUE (authority)
);

INSERT INTO roles (role_id, authority) VALUES (nextval('roles_role_id_seq'), 'USER');
INSERT INTO roles (role_id, authority) VALUES (nextval('roles_role_id_seq'), 'ADMIN');