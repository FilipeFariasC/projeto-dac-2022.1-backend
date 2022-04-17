package br.edu.ifpb.dac.groupd.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.dto.post.BraceletPostDto;
import br.edu.ifpb.dac.groupd.exception.BraceletNotFoundException;
import br.edu.ifpb.dac.groupd.model.Bracelet;
import br.edu.ifpb.dac.groupd.repository.BraceletRepository;

@Service
public class BraceletService {
	
	@Autowired
	private BraceletRepository braceletRepository;
	
	public Bracelet save(BraceletPostDto dto){
		Bracelet bracelet = mapFromDto(dto);
		
		return braceletRepository.save(bracelet);
	}
	
	public List<Bracelet> getAll(){
		return braceletRepository.findAll();
	}
	
	public Bracelet findById(Long idBracelet) throws BraceletNotFoundException {
		Optional<Bracelet> bracelet = braceletRepository.findById(idBracelet);
		if(bracelet.isEmpty()) 
			throw new BraceletNotFoundException(idBracelet);
	
		return braceletRepository.getById(idBracelet);
	}
	
	public Bracelet findByName(String name) throws BraceletNotFoundException {
		Optional<Bracelet> bracelet = braceletRepository.findByName(name);
		if(bracelet.isEmpty()) 
			throw new BraceletNotFoundException(name);
		
		return bracelet.get();
		
	}
	
	public Bracelet update(Long idBracelet, BraceletPostDto dto) throws BraceletNotFoundException {
		if(!braceletRepository.existsById(idBracelet)) {
			throw new BraceletNotFoundException("Bracelet Not Exist");
		}
		Bracelet bracelet = mapFromDto(dto);
		bracelet.setId(idBracelet);
		return braceletRepository.save(bracelet);
	}
	
	public void delete(Long idBracelet) throws BraceletNotFoundException  {
		if(!braceletRepository.existsById(idBracelet)) 
			throw new BraceletNotFoundException(idBracelet);
		
		braceletRepository.deleteById(idBracelet);
	}
	
	public List<Bracelet> findFilter(Bracelet filter){
		Example example = Example.of(filter, ExampleMatcher.matching()
				.withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING));
		return braceletRepository.findAll(example);
	}
	
	private Bracelet mapFromDto(BraceletPostDto dto){
		Bracelet bracelet = new Bracelet();
		bracelet.setName(dto.getName());
		return bracelet;
	}
}
