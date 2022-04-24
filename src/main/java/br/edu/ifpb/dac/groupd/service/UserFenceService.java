package br.edu.ifpb.dac.groupd.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.dto.post.FencePostDto;
import br.edu.ifpb.dac.groupd.exception.FenceNotFoundException;
import br.edu.ifpb.dac.groupd.exception.FenceNotRegisteredException;
import br.edu.ifpb.dac.groupd.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.model.Fence;
import br.edu.ifpb.dac.groupd.model.User;
import br.edu.ifpb.dac.groupd.repository.UserRepository;

@Service
public class UserFenceService {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private FenceService fenceService;
	
	
	public Fence createFence(Long userId, FencePostDto dto) throws UserNotFoundException {
		Optional<User> register = userRepo.findById(userId);
		
		if (register.isEmpty())
			throw new UserNotFoundException(userId);
		
		User user = register.get();
		
		Fence fence = fenceService.create(dto);

		user.getFences().add(fence);
		userRepo.save(user);
		return fence;
	}
	public List<Fence> getAllFences(Long userId) throws UserNotFoundException {
		Optional<User> register = userRepo.findById(userId);
		
		if (register.isEmpty())
			throw new UserNotFoundException(userId);
		
		User user = register.get();
		
		return user.getFences().stream().toList();
	}
	public Fence findFenceById(Long userId, Long fenceId) throws UserNotFoundException, FenceNotRegisteredException {
		Optional<User> register = userRepo.findById(userId);
		
		if (register.isEmpty())
			throw new UserNotFoundException(userId);
		
		User user = register.get();
		
		for(Fence fence : user.getFences()) {
			if(fence.getId().equals(fenceId)) {
				return fence;
			}
		}
		throw new FenceNotRegisteredException();
	}

	public Fence updateFence(Long userId, Long fenceId, FencePostDto dto) throws UserNotFoundException, FenceNotFoundException, FenceNotRegisteredException {
		Optional<User> register = userRepo.findById(userId);
		
		if (register.isEmpty())
			throw new UserNotFoundException(userId);
		
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
	public void deleteFence(Long userId, Long fenceId) throws UserNotFoundException, FenceNotRegisteredException, FenceNotFoundException {
		Optional<User> register = userRepo.findById(userId);
		
		if (register.isEmpty())
			throw new UserNotFoundException(userId);
		
		User user = register.get();
		Set<Fence> fences = user.getFences();

		Optional<Fence> fenceRegister = fences.stream().filter(fence->fence.getId().equals(fenceId)).findFirst();
		
		if(fenceRegister.isEmpty()) {
			throw new FenceNotRegisteredException();
		}
		
		Fence fence = fenceRegister.get();

		fences.remove(fence);
		userRepo.save(user);
		fenceService.delete(fenceId);
	}
}
