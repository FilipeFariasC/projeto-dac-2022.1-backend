package br.edu.ifpb.dac.groupd.presentation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class AlarmResponse {
	
	private Long id;
	private Boolean seen;
	@JsonIgnoreProperties(value="bracelets")
	private FenceResponse fence;
	private Double distance;
	private Double exceeded;
	@JsonIgnoreProperties(value="alarm")
	private LocationResponseMin location;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getSeen() {
		return seen;
	}
	public void setSeen(Boolean seen) {
		this.seen = seen;
	}
	public FenceResponse getFence() {
		return fence;
	}
	public void setFence(FenceResponse fence) {
		this.fence = fence;
	}
	public LocationResponseMin getLocation() {
		return location;
	}
	public void setLocation(LocationResponseMin location) {
		this.location = location;
	}
	public Double getDistance() {
		return distance;
	}
	public void setDistance(Double distance) {
		this.distance = distance;
	}
	public Double getExceeded() {
		return exceeded;
	}
	public void setExceeded(Double exceeded) {
		this.exceeded = exceeded;
	}
	
	
}
