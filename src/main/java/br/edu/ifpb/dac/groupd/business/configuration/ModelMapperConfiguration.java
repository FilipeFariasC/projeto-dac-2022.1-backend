package br.edu.ifpb.dac.groupd.business.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.edu.ifpb.dac.groupd.model.entities.Bracelet;
import br.edu.ifpb.dac.groupd.presentation.dto.BraceletRequest;

@Configuration
public class ModelMapperConfiguration {
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	private void setUpRequestToBracelet(ModelMapper mapper) {
		TypeMap<BraceletRequest, Bracelet> requestToBracelet = mapper.createTypeMap(BraceletRequest.class, Bracelet.class);
	}
}
