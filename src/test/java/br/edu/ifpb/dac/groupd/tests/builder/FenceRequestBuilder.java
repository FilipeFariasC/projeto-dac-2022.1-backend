package br.edu.ifpb.dac.groupd.tests.builder;

import java.time.LocalTime;

import br.edu.ifpb.dac.groupd.model.entities.Coordinate;
import br.edu.ifpb.dac.groupd.presentation.dto.FenceRequest;

public class FenceRequestBuilder {
	
	private String name;
	private Coordinate coordinate;
	private LocalTime startTime;
	private LocalTime finishTime;
	private Double radius;
	
	public FenceRequestBuilder withName(String name) {
		this.name = name;
		return this;
	}
	
	public FenceRequestBuilder withCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
		return this;
	}
	
	public FenceRequestBuilder withStartTime(LocalTime startTime) {
		this.startTime = startTime;
		return this;
	}
	
	public FenceRequestBuilder withFinishTime(LocalTime finishTime) {
		this.finishTime = finishTime;
		return this;
	}
	
	public FenceRequestBuilder withRadius(Double radius) {
		this.radius = radius;
		
		return this;
	}
	
	public FenceRequest build() {
		FenceRequest request = new FenceRequest();

		request.setName(name);
		request.setCoordinate(coordinate);
		request.setStartTime(startTime);
		request.setFinishTime(finishTime);
		request.setRadius(radius);
		
		return request;
	}
	
}
