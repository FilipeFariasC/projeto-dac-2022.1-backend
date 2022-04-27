CREATE TABLE IF NOT EXISTS t_bracelet (
	bracelet_id BIGSERIAL,
	name VARCHAR(50) NOT NULL,
	
	CONSTRAINT bracelet_pk PRIMARY KEY (bracelet_id)
);