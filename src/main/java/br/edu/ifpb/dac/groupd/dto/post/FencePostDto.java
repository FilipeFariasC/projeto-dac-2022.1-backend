package br.edu.ifpb.dac.groupd.dto.post;

import java.time.LocalTime;

import javax.persistence.Embedded;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.edu.ifpb.dac.groupd.model.Coordinate;

public class FencePostDto {

	@NotNull
	private Coordinate coordinate;
	
	@JsonFormat(pattern = "hh:mm")
	private LocalTime startTime;
	
	@JsonFormat(pattern = "hh:mm")
	private LocalTime finishTime;
	
	@NotNull
	private Boolean status;
	
	@NotNull
	@Min(1)
	private Double radius;

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

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Double getRadius() {
		return radius;
	}

	public void setRadius(Double radius) {
		this.radius = radius;
	}
	
	
	
}
