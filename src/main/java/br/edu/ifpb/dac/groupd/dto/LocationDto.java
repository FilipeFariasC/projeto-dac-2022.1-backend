package br.edu.ifpb.dac.groupd.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.edu.ifpb.dac.groupd.model.Coordinate;

public class LocationDto {

	private Long braceletId;
	private Coordinate coordinate;
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime creationDate;
	
	private Long alarmId;
	
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
