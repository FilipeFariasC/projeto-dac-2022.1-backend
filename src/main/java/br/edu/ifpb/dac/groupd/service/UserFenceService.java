package br.edu.ifpb.dac.groupd.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.dto.post.FencePostDto;
import br.edu.ifpb.dac.groupd.exception.FenceEmptyException;
import br.edu.ifpb.dac.groupd.exception.FenceNotFoundException;
import br.edu.ifpb.dac.groupd.exception.FenceNotRegisteredException;
import br.edu.ifpb.dac.groupd.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.model.Fence;
import br.edu.ifpb.dac.groupd.model.User;
import br.edu.ifpb.dac.groupd.repository.FenceRepository;
import br.edu.ifpb.dac.groupd.repository.UserRepository;

@Service
public class UserFenceService {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private FenceService fenceService;
	
	@Autowired
	private FenceRepository fenceRepo;
	
	
	public Fence createFence(String username, FencePostDto dto) throws UserNotFoundException {
		Optional<User> register = userRepo.findByEmail(username);
		
		if (register.isEmpty())
			throw new UserNotFoundException(username);
		
		User user = register.get();
		
		Fence fence = fenceService.create(dto);

		user.addFence(fence);
		userRepo.save(user);
		return fence;
	}
	public List<Fence> getAllFences(String username) throws UserNotFoundException {
		Optional<User> register = userRepo.findByEmail(username);
		
		if (register.isEmpty())
			throw new UserNotFoundException(username);
		
		User user = register.get();
		
		return user.getFences().stream().toList();
	}
	public Fence findFenceById(String username, Long fenceId) throws UserNotFoundException, FenceNotRegisteredException {
		Optional<User> register = userRepo.findByEmail(username);
		
		if (register.isEmpty())
			throw new UserNotFoundException(username);
		
		User user = register.get();
		
		for(Fence fence : user.getFences()) {
			if(fence.getId().equals(fenceId)) {
				return fence;
			}
		}
		throw new FenceNotRegisteredException();
	}

	public Fence updateFence(String username, Long fenceId, FencePostDto dto) throws UserNotFoundException, FenceNotFoundException, FenceNotRegisteredException {
		Optional<User> register = userRepo.findByEmail(username);
		
		if (register.isEmpty())
			throw new UserNotFoundException(username);
		
		User user = register.get();
		
		boolean existe = user.getFences()
				.stream()
				.mapToLong(Fence::getId)
				.anyMatch(id -> id == fenceId);
		if(!existe) {
			throw new FenceNotRegisteredException();
		}
		
		return fenceService.update(fenceId, dto);
	}
	public Fence setActive(String username, Long fenceId, Boolean status) throws FenceEmptyException, FenceNotFoundException, UserNotFoundException {
		
		Optional<User> register = userRepo.findByEmail(username);
		if(register.isEmpty()) {
			throw new UserNotFoundException(username);
		}
		User user = register.get();
		
		Optional<Fence> fenceRegister = user.getFences().stream().filter(f->f.getId().equals(fenceId))
		.findFirst();
		if(fenceRegister.isEmpty())
			throw new FenceNotFoundException(fenceId);
		
		Fence fence = fenceRegister.get();
		fence.setActive(status);
		
		return fenceRepo.save(fence);
	}
	
	public void deleteFence(String username, Long fenceId) throws UserNotFoundException, FenceNotRegisteredException, FenceNotFoundException {
		Optional<User> register = userRepo.findByEmail(username);
		
		if (register.isEmpty())
			throw new UserNotFoundException(username);
		
		User user = register.get();
		Set<Fence> fences = user.getFences();

		Optional<Fence> fenceRegister = fences.stream().filter(fence->fence.getId().equals(fenceId)).findFirst();
		
		if(fenceRegister.isEmpty()) {
			throw new FenceNotRegisteredException();
		}
		
		Fence fence = fenceRegister.get();

		user.removeFence(fence);
		userRepo.save(user);
		fenceService.delete(fenceId);
	}
}
