package br.edu.ifpb.dac.groupd.dto;

import java.time.LocalDateTime;

import br.edu.ifpb.dac.groupd.model.Coordinate;

public class LocationDto {

	private Long id;
	private Coordinate coordinate;
	private LocalDateTime timestamp;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Coordinate getCoordinate() {
		return coordinate;
	}
	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	
	
}
