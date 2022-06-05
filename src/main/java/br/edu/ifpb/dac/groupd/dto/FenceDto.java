package br.edu.ifpb.dac.groupd.dto;

import java.time.LocalTime;

import br.edu.ifpb.dac.groupd.model.Coordinate;

public class FenceDto {
	
	private Long id;
	private String name;
	private Coordinate coordinate;
	private LocalTime startTime;
	private LocalTime finishTime;
	private boolean active;
	private Double radius;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Coordinate getCoordinate() {
		return coordinate;
	}
	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}
	public LocalTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}
	public LocalTime getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(LocalTime finishTime) {
		this.finishTime = finishTime;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public Double getRadius() {
		return radius;
	}
	public void setRadius(Double radius) {
		this.radius = radius;
	}
	
	
}
