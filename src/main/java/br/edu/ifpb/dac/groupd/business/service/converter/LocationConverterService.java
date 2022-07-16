package br.edu.ifpb.dac.groupd.business.service.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.model.entities.Location;
import br.edu.ifpb.dac.groupd.presentation.dto.LocationRequest;
import br.edu.ifpb.dac.groupd.presentation.dto.LocationResponse;
import br.edu.ifpb.dac.groupd.presentation.dto.LocationResponseMin;

@Service
public class LocationConverterService {
	
	@Autowired
	private ModelMapper mapper;
	
	
	public Location requestToLocation(LocationRequest dto ) {
		Location location = mapper.map(dto, Location.class); 
		
		return location;
	}
	public LocationResponse locationToResponse(Location location) {
		LocationResponse response = mapper.map(location, LocationResponse.class); 
		
		return response;
	}
	public LocationResponseMin locationToResponseMin(Location location) {
		LocationResponseMin response = mapper.map(location, LocationResponseMin.class); 
		
		return response;
	}

}
