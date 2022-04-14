package br.edu.ifpb.groupd.model.Service;

import br.edu.ifpb.dac.groupd.model.Bracelet;
import br.edu.ifpb.groupd.model.dto.BraceletDTO;

public class BraceletSeviceConvet {
	
	public Bracelet dtoToLinha(BraceletDTO dto) {
		Bracelet bracelet = new Bracelet();
		bracelet.setName(dto.getName());
		bracelet.setIdBracelet(dto.getIdBracelet());
		return bracelet;
	}
	
	public BraceletDTO linhaToDTO(Bracelet bracelet) {
		BraceletDTO dto = new BraceletDTO();
		dto.setName(bracelet.getName());
		dto.setIdBracelet(bracelet.getIdBracelet());
		return dto;
	}
	

}
