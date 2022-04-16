CREATE TABLE IF NOT EXISTS user_bracelet (
	user_id BIGINT(20),
	bracelet_id BIGINT(20),
	
	
	CONSTRAINT user_id_fk_user_bracelet FOREIGN KEY (user_id) REFERENCES user(user_id),
	CONSTRAINT bracelet_id_fk_user_bracelet FOREIGN KEY (bracelet_id) REFERENCES bracelet(bracelet_id),
	CONSTRAINT user_bracelet_composite_pk PRIMARY KEY (bracelet_id, user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;