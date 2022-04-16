CREATE TABLE IF NOT EXISTS location (
	location_id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	latitude DECIMAL(10,8) NOT NULL,
	longitude DECIMAL(11,8) NOT NULL,
	creation_date TIMESTAMP NOT NULL,
	bracelet_id BIGINT(30) NOT NULL,
	
	CONSTRAINT bracelet_id_fk_location FOREIGN KEY (bracelet_id) REFERENCES bracelet(bracelet_id),
	CONSTRAINT valid_latitude_location CHECK (latitude >= -90 AND latitude <= 90),
	CONSTRAINT valid_longitude_location CHECK (longitude >= -180 AND latitude <= 180)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;