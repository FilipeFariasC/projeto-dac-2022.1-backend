CREATE TABLE IF NOT EXISTS bracelet_location (
--	bracelet_location_id BIGINT(20) AUTO_INCREMENT,
	bracelet_id BIGINT(20),
	location_id BIGINT(20),
	
	CONSTRAINT bracelet_id_fk_bracelet_locations FOREIGN KEY (bracelet_id) REFERENCES bracelet(bracelet_id),
	CONSTRAINT location_id_fk_bracelet_locations FOREIGN KEY (location_id) REFERENCES location(location_id),
	CONSTRAINT bracelet_locations_composite_pk PRIMARY KEY (bracelet_id, location_id)
--	CONSTRAINT bracelet_locations_composite_pk PRIMARY KEY (bracelet_location_id, bracelet_id, location_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;