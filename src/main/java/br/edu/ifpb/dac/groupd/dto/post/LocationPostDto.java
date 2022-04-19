package br.edu.ifpb.dac.groupd.dto.post;

import java.time.LocalDateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.edu.ifpb.dac.groupd.model.Coordinate;
import br.edu.ifpb.dac.groupd.validation.contraints.ValidTimestamp;

public class LocationPostDto {
	
	@NotNull
	private Long braceletId;
	
	@NotNull
	@Valid
	private Coordinate coordinate;
	
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@ValidTimestamp
	private LocalDateTime creationDate;

	
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

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}


	
}
