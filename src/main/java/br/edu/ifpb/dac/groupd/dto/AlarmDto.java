package br.edu.ifpb.dac.groupd.dto;

import java.time.LocalDateTime;

import br.edu.ifpb.dac.groupd.model.Fence;
import br.edu.ifpb.dac.groupd.model.Location;

public class AlarmDto {
	
	private Long id;
	private LocalDateTime registerDate;
	private Boolean seen;
	private Fence fence;
	private Location location;
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
	public Fence getFence() {
		return fence;
	}
	public void setFence(Fence fence) {
		this.fence = fence;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	
	

}
