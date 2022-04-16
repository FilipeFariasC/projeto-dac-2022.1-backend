package br.edu.ifpb.dac.groupd.dto.post;

import java.time.LocalDateTime;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class LocationPostDto {
	
	@NotNull
	private Long braceletId;
	
	@NotNull
	@Min(-90)
	@Max(90)
	private Double latitude;
	
	@NotNull
	@Min(-180)
	@Max(180)
	private Double longitude;
	
	@NotNull
	private LocalDateTime timestamp;

	
	
	public Long getBraceletId() {
		return braceletId;
	}

	public void setBraceletId(Long braceletId) {
		this.braceletId = braceletId;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	
	
}
