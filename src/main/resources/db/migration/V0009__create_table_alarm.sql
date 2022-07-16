CREATE TABLE IF NOT EXISTS alarms (
	id BIGSERIAL,
	fence_id BIGINT NOT NULL,
	location_id BIGINT NOT NULL,
	distance DECIMAL NOT NULL,
	seen BOOLEAN NOT NULL DEFAULT false,
	
	CONSTRAINT alarm_pk PRIMARY KEY (id),
	CONSTRAINT alarm_min_distance CHECK (distance >= 0),
	CONSTRAINT fence_id_fk_alarm FOREIGN KEY (fence_id) REFERENCES fences(id) ON DELETE CASCADE,
	CONSTRAINT location_id_fk_alarm FOREIGN KEY (location_id) REFERENCES locations(id) ON DELETE CASCADE
);