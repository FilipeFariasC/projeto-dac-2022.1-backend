package br.edu.ifpb.dac.groupd.presentation.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.edu.ifpb.dac.groupd.model.entities.Coordinate;

public class LocationResponse {
	
	private Long id;
	private Long braceletId;
	private Coordinate coordinate;
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime creationDate;
	
	private Long alarmId;
	
	
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
	public Long getBraceletId() {
		return braceletId;
	}
	public void setBraceletId(Long braceletId) {
		this.braceletId = braceletId;
	}
	public Long getAlarmId() {
		return alarmId;
	}
	public void setAlarmId(Long alarmId) {
		this.alarmId = alarmId;
	}
	
	
}
