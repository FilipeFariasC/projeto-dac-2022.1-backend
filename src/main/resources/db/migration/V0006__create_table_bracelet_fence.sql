CREATE TABLE IF NOT EXISTS bracelet_fence (
	fence_id BIGINT(20),
	bracelet_id BIGINT(20),
	
	CONSTRAINT fence_id_fk_fence_bracelet FOREIGN KEY (fence_id) REFERENCES fence(fence_id),
	CONSTRAINT bracelet_id_fk_fence_bracelet FOREIGN KEY (bracelet_id) REFERENCES bracelet(bracelet_id),
	CONSTRAINT fence_bracelet_composite_pk PRIMARY KEY (fence_id, bracelet_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;