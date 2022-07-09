
ALTER TABLE bracelets
	ADD COLUMN bracelet_monitor BIGINT;
	
ALTER TABLE bracelets
	ADD CONSTRAINT fence_fk_bracelet_monitor FOREIGN KEY (bracelet_monitor) REFERENCES fences(id);


