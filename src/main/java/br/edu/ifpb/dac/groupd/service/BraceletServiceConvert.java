package br.edu.ifpb.dac.groupd.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.dto.BraceletDTO;
import br.edu.ifpb.dac.groupd.model.Bracelet;

@Service
public class BraceletServiceConvert {
	
	public Bracelet dtoToBracelet(BraceletDTO dto) {
		Bracelet bracelet = new Bracelet();
		bracelet.setName(dto.getName());
		bracelet.setIdBracelet(dto.getIdBracelet());
		return bracelet;
	}
	
	public BraceletDTO braceletToDTO(Bracelet bracelet) {
		BraceletDTO dto = new BraceletDTO();
		dto.setName(bracelet.getName());
		dto.setIdBracelet(bracelet.getIdBracelet());
		return dto;
	}
	
	public List<BraceletDTO> braceletToDTO(List<Bracelet> bracelets){
		
		List<BraceletDTO> dtos = new ArrayList<>();
		
		for(Bracelet bracelet : bracelets) {
			BraceletDTO braceletDTO = braceletToDTO(bracelet);
			dtos.add(braceletDTO);
		}
		
		return dtos;
	}
	

}
