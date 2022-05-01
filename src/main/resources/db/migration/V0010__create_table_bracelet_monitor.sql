CREATE TABLE IF NOT EXISTS bracelet_monitor (
	bracelet_id_fk BIGINT NOT NULL,
	fence_id_fk BIGINT NOT NULL,
	
	CONSTRAINT bracelet_id_fk_bracelet_monitor FOREIGN KEY (bracelet_id_fk) REFERENCES bracelets(bracelet_id),
	CONSTRAINT fence_id_fk_bracelet_monitor FOREIGN KEY (fence_id_fk) REFERENCES fences(fence_id),
	CONSTRAINT bracelet_monitor_composite_pk PRIMARY KEY (bracelet_id_fk, fence_id_fk)
);