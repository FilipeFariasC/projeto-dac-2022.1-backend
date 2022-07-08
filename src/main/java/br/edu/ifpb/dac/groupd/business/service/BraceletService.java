package br.edu.ifpb.dac.groupd.business.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.business.exception.BraceletNotFoundException;
import br.edu.ifpb.dac.groupd.business.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.model.entities.Bracelet;
import br.edu.ifpb.dac.groupd.model.entities.User;
import br.edu.ifpb.dac.groupd.model.repository.BraceletRepository;
import br.edu.ifpb.dac.groupd.model.repository.UserRepository;
import br.edu.ifpb.dac.groupd.presentation.dto.BraceletRequest;

@Service
public class BraceletService {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private BraceletRepository braceletRepo;
	
	// User bracelet
	public Bracelet createBracelet(String username, BraceletRequest dto) throws UserNotFoundException {
		
		Optional<User> register = userRepo.findByEmail(username);
		
		if (register.isEmpty())
			throw new UserNotFoundException(username);
		
		User user = register.get();
		
		Bracelet mapped = mapFromDto(dto);
		
		Bracelet bracelet = braceletRepo.save(mapped);
		

		user.addBracelet(bracelet);
		userRepo.save(user);
		
		return bracelet;
	}
	public Page<Bracelet> getAllBracelets(String username, Pageable pageable) throws UserNotFoundException {
		boolean register = userRepo.existsByEmail(username);
		
		if (!register)
			throw new UserNotFoundException(username);
		
		Page<Bracelet> bracelets = braceletRepo.findAllBraceletsByUsername(username, pageable);
		return bracelets;
	}
	public Bracelet findByBraceletId(String username, Long braceletId) throws UserNotFoundException, BraceletNotFoundException {
		Optional<User> register = userRepo.findByEmail(username);
		
		if (register.isEmpty())
			throw new UserNotFoundException(username);
		
		User user = register.get();
		
		for(Bracelet bracelet : user.getBracelets()) {
			if(bracelet.getId().equals(braceletId)) {
				return bracelet;
			}
		}
		throw new BraceletNotFoundException(braceletId);
	}
	public List<Bracelet> searchBraceletByName(String username, String name) throws UserNotFoundException {
		Optional<User> register = userRepo.findByEmail(username);
		
		if(register.isEmpty())
			throw new UserNotFoundException(username);
		
		User user = register.get();
				
		return user.getBracelets()
				.stream()
				.filter(
					bracelet->bracelet.getName().toLowerCase().contains(name.toLowerCase())
					)
				.toList();
	}
	
	public Bracelet updateBracelet(String username, Long braceletId, BraceletRequest dto) throws UserNotFoundException, BraceletNotFoundException {
		Optional<User> register = userRepo.findByEmail(username);
		
		if (register.isEmpty())
			throw new UserNotFoundException(username);
		
		User user = register.get();
		
		boolean entrou = false;
		for(Bracelet braceletRegister : user.getBracelets()) {
			entrou = braceletRegister.getId().equals(braceletId);
		}
		if(!entrou) {
			throw new BraceletNotFoundException(braceletId);
		}
		
		Bracelet mapped = mapFromDto(dto);
		mapped.setId(braceletId);
		
		return braceletRepo.save(mapped);
	}
	public void deleteBracelet(String username, Long braceletId) throws UserNotFoundException, BraceletNotFoundException{
		Optional<User> register = userRepo.findByEmail(username);
		
		if (register.isEmpty())
			throw new UserNotFoundException(username);
		
		User user = register.get();
		Set<Bracelet> bracelets = user.getBracelets();
		
		Optional<Bracelet> registerBracelet = bracelets.stream().filter((bracelet)->bracelet.getId().equals(braceletId)).findFirst();
		if(register.isEmpty()) {
			throw new BraceletNotFoundException(braceletId);
		}
		user.removeBracelet(registerBracelet.get());
		userRepo.save(user);
		braceletRepo.deleteById(braceletId);
	}
	private Bracelet mapFromDto(BraceletRequest dto){
		Bracelet bracelet = new Bracelet();
		bracelet.setName(dto.getName());
		return bracelet;
	}
}
