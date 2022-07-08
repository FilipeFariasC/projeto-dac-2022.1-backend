package br.edu.ifpb.dac.groupd.presentation.dto;

import java.time.LocalTime;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.edu.ifpb.dac.groupd.business.validation.constraints.ValidTimer;
import br.edu.ifpb.dac.groupd.model.entities.Coordinate;
import br.edu.ifpb.dac.groupd.model.interfaces.Timer;


@ValidTimer
public class FenceRequest implements Timer{
	
	@NotNull
	@NotEmpty
	@NotBlank
	@Size(min=1,max=50)
	private String name;
	
	@NotNull
	@Valid
	private Coordinate coordinate;
	
	@JsonFormat(pattern = "HH:mm")
	private LocalTime startTime;
	
	@JsonFormat(pattern = "HH:mm")
	private LocalTime finishTime;
	
	@NotNull
	@Min(1)
	private Double radius;
	
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

	public Double getRadius() {
		return radius;
	}

	public void setRadius(Double radius) {
		this.radius = radius;
	}
	
	
	
}
