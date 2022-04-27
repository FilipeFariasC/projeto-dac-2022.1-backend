CREATE TABLE IF NOT EXISTS user_bracelet (
	user_id BIGINT,
	bracelet_id BIGINT,
	
	
	CONSTRAINT user_id_fk_user_bracelet FOREIGN KEY (user_id) REFERENCES t_user(user_id),
	CONSTRAINT bracelet_id_fk_user_bracelet FOREIGN KEY (bracelet_id) REFERENCES t_bracelet(bracelet_id),
	CONSTRAINT user_bracelet_composite_pk PRIMARY KEY (bracelet_id, user_id)
);