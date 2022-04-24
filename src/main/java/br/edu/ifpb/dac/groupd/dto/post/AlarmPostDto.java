package br.edu.ifpb.dac.groupd.dto.post;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.edu.ifpb.dac.groupd.model.Fence;
import br.edu.ifpb.dac.groupd.model.Location;

public class AlarmPostDto {
		
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime registerDate;
	
	@NotNull
	private Long fenceId;
	
	@NotNull
	private Long locationId;
	
	public LocalDateTime getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(LocalDateTime registerDate) {
		this.registerDate = registerDate;
	}
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
