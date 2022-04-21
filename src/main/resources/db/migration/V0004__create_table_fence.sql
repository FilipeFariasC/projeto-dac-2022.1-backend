CREATE TABLE IF NOT EXISTS fence (
	fence_id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	latitude DECIMAL(10,8) NOT NULL,
	longitude DECIMAL(11,8) NOT NULL,
	start_time TIME,
	finish_time TIME,
	status BOOLEAN NOT NULL,
	radius DOUBLE DEFAULT 0.0,
	
	CONSTRAINT valid_latitude_fence CHECK (latitude >= -90 AND latitude <= 90),
	CONSTRAINT valid_longitude_fence CHECK (longitude >= -180 AND latitude <= 180),
	CONSTRAINT valid_radius CHECK (radius >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;