package br.edu.ifpb.dac.groupd.business.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.model.entities.Bracelet;
import br.edu.ifpb.dac.groupd.presentation.dto.BraceletResponse;

@Service
public class BraceletServiceConvert {
	
	public Bracelet dtoToBracelet(BraceletResponse dto) {
		Bracelet bracelet = new Bracelet();
		bracelet.setName(dto.getName());
		bracelet.setId(dto.getId());
		return bracelet;
	}
	
	public BraceletResponse braceletToDTO(Bracelet bracelet) {
		BraceletResponse dto = new BraceletResponse();
		dto.setName(bracelet.getName());
		dto.setId(bracelet.getId());
		return dto;
	}
	
	public List<BraceletResponse> braceletToDTO(List<Bracelet> bracelets){
		
		List<BraceletResponse> dtos = new ArrayList<>();
		
		for(Bracelet bracelet : bracelets) {
			BraceletResponse braceletDTO = braceletToDTO(bracelet);
			dtos.add(braceletDTO);
		}
		
		return dtos;
	}
	

}
