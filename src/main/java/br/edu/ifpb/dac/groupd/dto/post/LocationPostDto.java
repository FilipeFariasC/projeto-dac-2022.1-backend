package br.edu.ifpb.dac.groupd.dto.post;

import java.time.LocalDateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import br.edu.ifpb.dac.groupd.model.Coordinate;

public class LocationPostDto {
	
	@NotNull
	private Long braceletId;
	
	@NotNull
	@Valid
	private Coordinate coordinate;
	
	@NotNull
	private LocalDateTime timestamp;

	
	
	public Long getBraceletId() {
		return braceletId;
	}

	public void setBraceletId(Long braceletId) {
		this.braceletId = braceletId;
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
