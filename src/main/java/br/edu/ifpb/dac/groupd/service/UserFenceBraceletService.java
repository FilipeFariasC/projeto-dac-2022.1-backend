package br.edu.ifpb.dac.groupd.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.exception.BraceletNotRegisteredException;
import br.edu.ifpb.dac.groupd.exception.FenceNotRegisteredException;
import br.edu.ifpb.dac.groupd.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.model.Bracelet;
import br.edu.ifpb.dac.groupd.model.Fence;
import br.edu.ifpb.dac.groupd.model.User;
import br.edu.ifpb.dac.groupd.repository.UserRepository;

@Service
public class UserFenceBraceletService {
	
	@Autowired
	private UserRepository userRepo;
	
	
	public void addBraceletFence(String username, Long fenceId, Long braceletId) throws UserNotFoundException, FenceNotRegisteredException, BraceletNotRegisteredException {
		Optional<User> register = userRepo.findByEmail(username);
		
		if (register.isEmpty())
			throw new UserNotFoundException(username);
		
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
			throw new FenceNotRegisteredException();
		}
		
		if(braceletRegister.isEmpty()) {
			throw new BraceletNotRegisteredException();
		}
		
		Fence fence = fenceRegister.get();
		Bracelet bracelet = braceletRegister.get();
		
		fence.addBracelet(bracelet);
		
		userRepo.save(user);
		
	}
	public void removeBraceletFence(String username, Long fenceId, Long braceletId) throws UserNotFoundException, FenceNotRegisteredException, BraceletNotRegisteredException {
		Optional<User> register = userRepo.findByEmail(username);
		
		if (register.isEmpty())
			throw new UserNotFoundException(username);
		
		User user = register.get();
		
		Optional<Fence> fenceRegister = 
				user.getFences()
				.stream()
				.filter(fence -> fence.getId().equals(fenceId))
				.findFirst();
		
		if(fenceRegister.isEmpty()) {
			throw new FenceNotRegisteredException();
		}
		Fence fence = fenceRegister.get();
		
		Optional<Bracelet> braceletRegister = 
				fence.getBracelets()
				.stream()
				.filter(bracelet -> bracelet.getId().equals(braceletId))
				.findFirst();
		
		if(braceletRegister.isEmpty()) {
			throw new BraceletNotRegisteredException();
		}
		
		Bracelet bracelet = braceletRegister.get();
		
		fence.removeBracelet(bracelet);
		
		userRepo.save(user);
		
	}
}
