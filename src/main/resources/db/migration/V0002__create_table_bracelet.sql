CREATE TABLE IF NOT EXISTS bracelets (
	bracelet_id BIGSERIAL,
	name VARCHAR(50) NOT NULL,
	owner BIGINT NOT NULL,
	
	CONSTRAINT bracelet_pk PRIMARY KEY (bracelet_id),
	CONSTRAINT user_id_fk FOREIGN KEY (owner) REFERENCES users(user_id) ON DELETE CASCADE
);