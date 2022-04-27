CREATE TABLE IF NOT EXISTS bracelet_location (
	bracelet_id BIGINT,
	location_id BIGINT,
	
	CONSTRAINT bracelet_id_fk_bracelet_locations FOREIGN KEY (bracelet_id) REFERENCES t_bracelet(bracelet_id),
	CONSTRAINT location_id_fk_bracelet_locations FOREIGN KEY (location_id) REFERENCES t_location(location_id),
	CONSTRAINT bracelet_locations_composite_pk PRIMARY KEY (bracelet_id, location_id)
);