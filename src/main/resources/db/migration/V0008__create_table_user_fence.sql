CREATE TABLE IF NOT EXISTS user_fence (
	user_id BIGINT(20),
	fence_id BIGINT(20),
	
	
	CONSTRAINT user_id_fk_user_fence FOREIGN KEY (user_id) REFERENCES user(user_id),
	CONSTRAINT fence_id_fk_user_fence FOREIGN KEY (fence_id) REFERENCES fence(fence_id),
	CONSTRAINT user_bracelet_composite_pk PRIMARY KEY (user_id, fence_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;