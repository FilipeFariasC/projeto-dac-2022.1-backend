package br.edu.ifpb.dac.groupd.presentation.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.edu.ifpb.dac.groupd.model.entities.Coordinate;

public class LocationResponseMin {
	
	private Long id;
	@JsonIgnoreProperties(value="fences")
	private BraceletResponse bracelet;
	private Coordinate coordinate;
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime creationDate;
	
	
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

	public LocalDateTime getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}
	public BraceletResponse getBracelet() {
		return bracelet;
	}
	public void setBracelet(BraceletResponse bracelet) {
		this.bracelet = bracelet;
	}
	
}
