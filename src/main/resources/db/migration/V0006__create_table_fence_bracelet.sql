CREATE TABLE IF NOT EXISTS fence_bracelet (
	fence_id BIGINT,
	bracelet_id BIGINT,
	
	CONSTRAINT fence_id_fk_fence_bracelet FOREIGN KEY (fence_id) REFERENCES fences(id),
	CONSTRAINT bracelet_id_fk_fence_bracelet FOREIGN KEY (bracelet_id) REFERENCES bracelets(id),
	CONSTRAINT fence_bracelet_composite_pk PRIMARY KEY (fence_id, bracelet_id)
);