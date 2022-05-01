CREATE TABLE IF NOT EXISTS users (
	user_id BIGSERIAL,
	name VARCHAR(50) NOT NULL,
	email VARCHAR(255) NOT NULL,
	password VARCHAR(255) NOT NULL,
	
	
	CONSTRAINT user_id_pk PRIMARY KEY (user_id),
	CONSTRAINT user_email_unique UNIQUE (email)
	
);