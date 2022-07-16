package br.edu.ifpb.dac.groupd.business.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.business.exception.BraceletNotFoundException;
import br.edu.ifpb.dac.groupd.business.exception.FenceNotFoundException;
import br.edu.ifpb.dac.groupd.business.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.model.entities.Bracelet;
import br.edu.ifpb.dac.groupd.model.entities.Fence;
import br.edu.ifpb.dac.groupd.model.entities.User;
import br.edu.ifpb.dac.groupd.model.repository.UserRepository;

@Service
public class FenceBraceletService {
	
	@Autowired
	private UserRepository userRepo;
	
	
	public void addBraceletFence(Long id, Long fenceId, Long braceletId) throws UserNotFoundException, FenceNotFoundException, BraceletNotFoundException {
		Optional<User> register = userRepo.findById(id);
		
		if (register.isEmpty())
			throw new UserNotFoundException(id);
		
		User user = register.get();
		
		Optional<Fence> fenceRegister = 
				user.getFences()
				.stream()
				.filter(fence -> fence.getId().equals(fenceId))
				.findFirst();
		Optional<Bracelet> braceletRegister = 
				user.getBracelets()
				.stream()
				.filter(bracelet -> bracelet.getId().equals(braceletId))
				.findFirst();
		
		if(fenceRegister.isEmpty()) {
			throw new FenceNotFoundException(fenceId);
		}
		
		if(braceletRegister.isEmpty()) {
			throw new BraceletNotFoundException(fenceId);
		}
		
		Fence fence = fenceRegister.get();
		Bracelet bracelet = braceletRegister.get();
		
		fence.addBracelet(bracelet);
		
		userRepo.save(user);
		
	}
	public void removeBraceletFence(Long id, Long fenceId, Long braceletId) throws UserNotFoundException, FenceNotFoundException, BraceletNotFoundException {
		Optional<User> register = userRepo.findById(id);
		
		if (register.isEmpty())
			throw new UserNotFoundException(id);
		
		User user = register.get();
		
		Optional<Fence> fenceRegister = 
				user.getFences()
				.stream()
				.filter(fence -> fence.getId().equals(fenceId))
				.findFirst();
		
		if(fenceRegister.isEmpty()) {
			throw new FenceNotFoundException(fenceId);
		}
		Fence fence = fenceRegister.get();
		
		Optional<Bracelet> braceletRegister = 
				fence.getBracelets()
				.stream()
				.filter(bracelet -> bracelet.getId().equals(braceletId))
				.findFirst();
		
		if(braceletRegister.isEmpty()) {
			throw new BraceletNotFoundException(braceletId);
		}
		
		Bracelet bracelet = braceletRegister.get();
		
		fence.removeBracelet(bracelet);
		
		userRepo.save(user);
		
	}
}
