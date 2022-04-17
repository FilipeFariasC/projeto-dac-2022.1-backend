package br.edu.ifpb.dac.groupd.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.dto.BraceletDto;
import br.edu.ifpb.dac.groupd.model.Bracelet;

@Service
public class BraceletServiceConvert {
	
	public Bracelet dtoToBracelet(BraceletDto dto) {
		Bracelet bracelet = new Bracelet();
		bracelet.setName(dto.getName());
		bracelet.setId(dto.getIdBracelet());
		return bracelet;
	}
	
	public BraceletDto braceletToDTO(Bracelet bracelet) {
		BraceletDto dto = new BraceletDto();
		dto.setName(bracelet.getName());
		dto.setIdBracelet(bracelet.getId());
		return dto;
	}
	
	public List<BraceletDto> braceletToDTO(List<Bracelet> bracelets){
		
		List<BraceletDto> dtos = new ArrayList<>();
		
		for(Bracelet bracelet : bracelets) {
			BraceletDto braceletDTO = braceletToDTO(bracelet);
			dtos.add(braceletDTO);
		}
		
		return dtos;
	}
	

}
