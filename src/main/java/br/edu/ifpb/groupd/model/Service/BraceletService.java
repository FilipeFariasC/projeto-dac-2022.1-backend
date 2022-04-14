package br.edu.ifpb.groupd.model.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.model.Bracelet;
import br.edu.ifpb.groupd.model.repository.BraceletRepository;

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
	
	public List<Bracelet> getBracelets(){
		return braceletyRepositori.findAll();
	}
	
	public Bracelet getBraceletID(Long idBracelet) throws Exception {
		Bracelet brac = braceletyRepositori.getById(idBracelet);
		if(brac == null) {
			throw new Exception("Bracelet Not Exist");
		}else {
			return braceletyRepositori.getById(idBracelet);
		}
	}
	
	public Bracelet getBraceletNAME(String name) throws Exception {
		Bracelet brac = braceletyRepositori.findByname(name);
		if(brac == null) {
			throw new Exception("Bracelet Not Exist");
		}else {
			return braceletyRepositori.findByname(name);
		}
	}
	
	public Bracelet updateBracelet(Long idBracelet,Bracelet bracelet) throws Exception{
		Bracelet brac = braceletyRepositori.getById(idBracelet);
		if(brac == null) {
			throw new Exception("Bracelet Not Exist");
		}else {
			brac.setName(bracelet.getName());
			brac.setFences(bracelet.getFences());
			brac.setLocations(bracelet.getLocations());
			deleteBracelet(bracelet.getIdBracelet());
			return braceletyRepositori.save(brac);
		}
	}
	
	public void deleteBracelet(Long idBracelet) throws Exception {
		Bracelet brac = braceletyRepositori.getById(idBracelet);
		if(brac == null) {
			throw new Exception("Bracelet Not Exist");
		}else {
			braceletyRepositori.deleteById(idBracelet);
		}
	}
	

}
