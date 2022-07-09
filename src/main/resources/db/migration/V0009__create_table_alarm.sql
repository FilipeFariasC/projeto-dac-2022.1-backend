CREATE TABLE IF NOT EXISTS alarms (
	id BIGSERIAL,
	fence_id BIGINT NOT NULL,
	location_id BIGINT NOT NULL,
	seen BOOLEAN NOT NULL DEFAULT false,
	
	CONSTRAINT alarm_pk PRIMARY KEY (id),
	CONSTRAINT fence_id_fk_alarm FOREIGN KEY (fence_id) REFERENCES fences(id),
	CONSTRAINT location_id_fk_alarm FOREIGN KEY (location_id) REFERENCES locations(id)
);