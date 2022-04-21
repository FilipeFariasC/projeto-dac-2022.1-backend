CREATE TABLE IF NOT EXISTS alarm (
	alarm_id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	fence_id BIGINT(20) NOT NULL,
	register_date TIMESTAMP NOT NULL,
	seen BOOLEAN NOT NULL,
	location_id BIGINT(20) NOT NULL,
	
	CONSTRAINT fence_id_fk_alarm FOREIGN KEY (fence_id) REFERENCES fence(fence_id),
	CONSTRAINT location_id_fk_alarm FOREIGN KEY (location_id) REFERENCES location(location_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;