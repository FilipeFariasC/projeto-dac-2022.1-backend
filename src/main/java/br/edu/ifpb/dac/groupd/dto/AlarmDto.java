package br.edu.ifpb.dac.groupd.dto;

import java.time.LocalDateTime;

import br.edu.ifpb.dac.groupd.model.Fence;
import br.edu.ifpb.dac.groupd.model.Location;

public class AlarmDto {
	
	private Long id;
	private LocalDateTime registerDate;
	private Boolean seen;
	private Long idFence;
	private Long idLocation;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDateTime getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(LocalDateTime registerDate) {
		this.registerDate = registerDate;
	}
	public Boolean getSeen() {
		return seen;
	}
	public void setSeen(Boolean seen) {
		this.seen = seen;
	}
	public Long getFence() {
		return idFence;
	}
	public void setFence(Long idFence) {
		this.idFence = idFence;
	}
	public Long getLocation() {
		return idLocation;
	}
	public void setLocation(Long idLocation) {
		this.idLocation = idLocation;
	}
	
	

}
