package br.edu.ifpb.groupd.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.model.Bracelet;
import br.edu.ifpb.groupd.repository.BraceletRepository;

@Service
public class BraceletService {
	
	@Autowired
	private BraceletRepository braceletyRepositori;
	
	public Bracelet SaveBracelet(Bracelet bracelet) throws Exception{
		Bracelet brc = braceletyRepositori.findByname(bracelet.getName()); 
		if(brc == null) {
			return braceletyRepositori.save(bracelet);
		}
		throw new Exception("Bracelet exist");
	}
	

}
