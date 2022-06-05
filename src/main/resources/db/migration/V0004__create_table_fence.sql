CREATE TABLE IF NOT EXISTS fences (
	fence_id BIGSERIAL,
	name VARCHAR(50) NOT NULL,
	latitude DECIMAL(10,8) NOT NULL,
	longitude DECIMAL(11,8) NOT NULL,
	start_time TIME,
	finish_time TIME,
	active BOOLEAN NOT NULL,
	radius NUMERIC(4,2) DEFAULT 1.0,
	
	CONSTRAINT fence_pk PRIMARY KEY (fence_id),
	CONSTRAINT valid_latitude_fence CHECK (latitude >= -90 AND latitude <= 90),
	CONSTRAINT valid_longitude_fence CHECK (longitude >= -180 AND latitude <= 180),
	CONSTRAINT valid_radius CHECK (radius >= 0)
);