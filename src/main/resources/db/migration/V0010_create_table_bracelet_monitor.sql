
CREATE TABLE IF NOT EXISTS bracelet_monitor (
	bracelet_id BIGINT NOT NULL,
	fence_id BIGINT NOT NULL,
	
	CONSTRAINT bracelet_id_fk_bracelet_monitor FOREIGN KEY (bracelet_id) REFERENCES bracelets(id),
	CONSTRAINT fence_id_fk_bracelet_monitor FOREIGN KEY (fence_id) REFERENCES fences(id),
	CONSTRAINT bracelet_monitor_composite_pk PRIMARY KEY (bracelet_id, fence_id)
);	