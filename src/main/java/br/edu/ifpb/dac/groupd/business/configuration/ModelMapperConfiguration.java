package br.edu.ifpb.dac.groupd.business.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.edu.ifpb.dac.groupd.model.entities.Location;
import br.edu.ifpb.dac.groupd.presentation.dto.LocationResponseMin;

@Configuration
public class ModelMapperConfiguration {
	
	private ModelMapper modelMapper = new ModelMapper();
	public ModelMapperConfiguration() {
		setupLocationMapping();
	}
	
	@Bean
	public ModelMapper modelMapper() {
		return modelMapper;
	}
	
	private void setupLocationMapping() {
		modelMapper.createTypeMap(Location.class, LocationResponseMin.class)
			.addMapping(Location::getId, LocationResponseMin::setId)
			.addMapping(Location::getCoordinate, LocationResponseMin::setCoordinate)
			.addMapping(Location::getCreationDate, LocationResponseMin::setCreationDate)
			.addMapping(Location::getBracelet, LocationResponseMin::setBracelet);
	}
}
