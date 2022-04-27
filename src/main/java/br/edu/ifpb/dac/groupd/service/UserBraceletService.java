package br.edu.ifpb.dac.groupd.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.dto.post.BraceletPostDto;
import br.edu.ifpb.dac.groupd.exception.BraceletNotFoundException;
import br.edu.ifpb.dac.groupd.exception.BraceletNotRegisteredException;
import br.edu.ifpb.dac.groupd.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.model.Bracelet;
import br.edu.ifpb.dac.groupd.model.User;
import br.edu.ifpb.dac.groupd.repository.UserRepository;

@Service
public class UserBraceletService {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private BraceletService braceletService;
	
	// User bracelet
	public Bracelet createBracelet(Long userId, BraceletPostDto dto) throws UserNotFoundException {
		Optional<User> register = userRepo.findById(userId);
		
		if (register.isEmpty())
			throw new UserNotFoundException(userId);
		
		User user = register.get();
		
		Bracelet bracelet = braceletService.save(dto);

		user.addBracelet(bracelet);
		userRepo.save(user);
		return bracelet;
	}
	public List<Bracelet> getAllBracelets(Long userId) throws UserNotFoundException {
		Optional<User> register = userRepo.findById(userId);
		
		if (register.isEmpty())
			throw new UserNotFoundException(userId);
		
		User user = register.get();
		
		return user.getBracelets().stream().toList();
	}
	public Bracelet findByBraceletId(Long userId, Long braceletId) throws UserNotFoundException, BraceletNotRegisteredException {
		Optional<User> register = userRepo.findById(userId);
		
		if (register.isEmpty())
			throw new UserNotFoundException(userId);
		
		User user = register.get();
		
		for(Bracelet bracelet : user.getBracelets()) {
			if(bracelet.getId().equals(braceletId)) {
				return bracelet;
			}
		}
		throw new BraceletNotRegisteredException();
	}
	public List<Bracelet> searchBraceletByName(Long userId, String name) throws UserNotFoundException {
		Optional<User> register = userRepo.findById(userId);
		
		if(register.isEmpty())
			throw new UserNotFoundException(userId);
		
		User user = register.get();
				
		return user.getBracelets()
				.stream()
				.filter(
					bracelet->bracelet.getName().toLowerCase().contains(name.toLowerCase())
					)
				.toList();
	}
	
	public Bracelet updateBracelet(Long userId, Long braceletId, BraceletPostDto dto) throws UserNotFoundException, BraceletNotFoundException, BraceletNotRegisteredException {
		Optional<User> register = userRepo.findById(userId);
		
		if (register.isEmpty())
			throw new UserNotFoundException(userId);
		
		User user = register.get();
		
		boolean entrou = false;
		for(Bracelet braceletRegister : user.getBracelets()) {
			entrou = braceletRegister.getId().equals(braceletId);
		}
		if(!entrou) {
			throw new BraceletNotRegisteredException();
		}
		
		return braceletService.update(braceletId, dto);
	}
	public void deleteBracelet(Long userId, Long braceletId) throws UserNotFoundException, BraceletNotFoundException, BraceletNotRegisteredException {
		Optional<User> register = userRepo.findById(userId);
		
		if (register.isEmpty())
			throw new UserNotFoundException(userId);
		
		User user = register.get();
		Set<Bracelet> bracelets = user.getBracelets();
		boolean entrou = false;
		
		Bracelet bracelet = null;
		for(Bracelet braceletRegister : bracelets) {
			if(braceletRegister.getId().equals(braceletId)) {
				bracelet = braceletRegister;
				entrou = true;
			}
		}
		if(entrou) {
			throw new BraceletNotRegisteredException();
		}
		user.removeBracelet(bracelet);
		userRepo.save(user);
		braceletService.delete(braceletId);
	}
}
