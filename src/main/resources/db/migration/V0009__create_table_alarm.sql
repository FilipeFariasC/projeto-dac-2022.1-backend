CREATE TABLE IF NOT EXISTS t_alarm (
	alarm_id BIGSERIAL,
	fence_id BIGINT NOT NULL,
	seen BOOLEAN NOT NULL,
	location_id BIGINT NOT NULL,
	
	CONSTRAINT alarm_pk PRIMARY KEY (alarm_id),
	CONSTRAINT fence_id_fk_alarm FOREIGN KEY (fence_id) REFERENCES t_fence(fence_id),
	CONSTRAINT location_id_fk_alarm FOREIGN KEY (location_id) REFERENCES t_location(location_id)
);