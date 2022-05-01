CREATE TABLE IF NOT EXISTS bracelets (
	bracelet_id BIGSERIAL,
	name VARCHAR(50) NOT NULL,
	
	CONSTRAINT bracelet_pk PRIMARY KEY (bracelet_id)
);