CREATE TABLE IF NOT EXISTS user_fence (
	user_id BIGINT,
	fence_id BIGINT,
	
	
	CONSTRAINT user_id_fk_user_fence FOREIGN KEY (user_id) REFERENCES t_user(user_id),
	CONSTRAINT fence_id_fk_user_fence FOREIGN KEY (fence_id) REFERENCES t_fence(fence_id),
	CONSTRAINT user_fence_composite_pk PRIMARY KEY (user_id, fence_id)
);