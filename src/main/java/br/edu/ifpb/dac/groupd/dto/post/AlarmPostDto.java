package br.edu.ifpb.dac.groupd.dto.post;

import javax.validation.constraints.NotNull;

public class AlarmPostDto {
	
	@NotNull
	private Long fenceId;
	
	@NotNull
	private Long locationId;
	
	public Long getFenceId() {
		return fenceId;
	}
	public void setFenceId(Long fenceId) {
		this.fenceId = fenceId;
	}
	public Long getLocationId() {
		return locationId;
	}
	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}
	
	
	
}
