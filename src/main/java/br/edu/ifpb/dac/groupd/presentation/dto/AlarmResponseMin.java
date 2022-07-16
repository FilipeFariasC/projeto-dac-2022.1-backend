package br.edu.ifpb.dac.groupd.presentation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class AlarmResponseMin {
	
	private Long id;
	private Boolean seen;
	private Double distance;
	@JsonIgnoreProperties(value="bracelets")
	private FenceResponse fence;
	
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
	public Double getDistance() {
		return distance;
	}
	public void setDistance(Double distance) {
		this.distance = distance;
	}
	

}
