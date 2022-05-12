CREATE TABLE IF NOT EXISTS user_roles(
	user_id BIGINT,
	role_id BIGINT,
	
	CONSTRAINT user_id_fk_user_roles FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
	CONSTRAINT role_id_fk_user_roles FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE,
	CONSTRAINT user_roles_pk PRIMARY KEY (user_id,role_id)
);