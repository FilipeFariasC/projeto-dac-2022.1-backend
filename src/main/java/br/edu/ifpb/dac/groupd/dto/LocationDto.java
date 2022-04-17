package br.edu.ifpb.dac.groupd.dto;

import java.time.LocalDateTime;

import br.edu.ifpb.dac.groupd.model.Coordinate;

public class LocationDto {

	private Long braceletId;
	private Coordinate coordinate;
	private LocalDateTime creationDate;
	
	public Coordinate getCoordinate() {
		return coordinate;
	}
	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}
	public Long getBraceletId() {
		return braceletId;
	}
	public void setBraceletId(Long braceletId) {
		this.braceletId = braceletId;
	}
	
	
}
