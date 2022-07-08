package br.edu.ifpb.dac.groupd.business.service.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.model.entities.Bracelet;
import br.edu.ifpb.dac.groupd.presentation.dto.BraceletRequest;
import br.edu.ifpb.dac.groupd.presentation.dto.BraceletResponse;
import br.edu.ifpb.dac.groupd.presentation.dto.BraceletResponseMin;

@Service
public class BraceletConverterService {
	
	@Autowired
	private ModelMapper mapper;
	
	
	public Bracelet requestToBracelet(BraceletRequest dto ) {
		Bracelet bracelet = mapper.map(dto, Bracelet.class); 
		
		return bracelet;
	}
	public BraceletResponse braceletToResponse(Bracelet bracelet) {
		BraceletResponse response = mapper.map(bracelet, BraceletResponse.class); 
		
		return response;
	}
	public BraceletResponseMin braceletToResponseMin(Bracelet bracelet) {
		BraceletResponseMin responseMin = mapper.map(bracelet, BraceletResponseMin.class); 
		
		return responseMin;
	}

}
