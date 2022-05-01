package br.edu.ifpb.dac.groupd.dto;

public class AlarmDto {
	
	private Long id;
	private Boolean seen;
	private Long idFence;
	private Long idLocation;
	
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
