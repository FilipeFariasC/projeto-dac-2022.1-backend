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
import br.edu.ifpb.dac.groupd.exception.NoBraceletAvailableException;
import br.edu.ifpb.dac.groupd.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.model.Fence;
import br.edu.ifpb.dac.groupd.model.User;
import br.edu.ifpb.dac.groupd.repository.FenceRepository;
import br.edu.ifpb.dac.groupd.repository.UserRepository;

@Service
public class FenceService {
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private FenceRepository fenceRepo;
	
	
	public Fence createFence(String username, FencePostDto dto) throws UserNotFoundException {
		Optional<User> register = userRepo.findByEmail(username);
		
		if (register.isEmpty())
			throw new UserNotFoundException(username);
		
		User user = register.get();
		
		Fence mapped = mapFromDto(dto);
		
		Fence fence = fenceRepo.save(mapped);

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
		
		Optional<Fence> registerFence = user.getFences().stream().filter(fence->fence.getId().equals(fenceId)).findFirst();
		
		if(registerFence.isEmpty()) {
			throw new FenceNotRegisteredException();
		}
		
		return registerFence.get();
	}

	public Fence updateFence(String username, Long fenceId, FencePostDto dto) throws UserNotFoundException, FenceNotFoundException {
		Optional<User> register = userRepo.findByEmail(username);
		
		if (register.isEmpty())
			throw new UserNotFoundException(username);
		
		User user = register.get();
		
		boolean existe = user.getFences()
				.stream()
				.mapToLong(Fence::getId)
				.anyMatch(id -> id == fenceId);
		if(!existe) {
			throw new FenceNotFoundException(fenceId);
		}
		
		Fence mapped = mapFromDto(dto);
		
		return fenceRepo.save(mapped);
	}
	public Fence setActive(String username, Long fenceId, Boolean status) throws FenceEmptyException, FenceNotFoundException, UserNotFoundException, NoBraceletAvailableException {
		
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
	
	public void deleteFence(String username, Long fenceId) throws UserNotFoundException, FenceNotFoundException {
		Optional<User> register = userRepo.findByEmail(username);
		
		if (register.isEmpty())
			throw new UserNotFoundException(username);
		
		User user = register.get();
		Set<Fence> fences = user.getFences();

		Optional<Fence> fenceRegister = fences.stream().filter(fence->fence.getId().equals(fenceId)).findFirst();
		
		if(fenceRegister.isEmpty()) {
			throw new FenceNotFoundException(fenceId);
		}
		
		Fence fence = fenceRegister.get();

		user.removeFence(fence);
		userRepo.save(user);
		fenceRepo.deleteById(fenceId);
	}
	
	private Fence mapFromDto(FencePostDto dto) {
		Fence fence = new Fence();
		
		fence.setCoordinate(dto.getCoordinate());
		fence.setRadius(dto.getRadius());
		fence.setStartTime(dto.getStartTime());
		fence.setFinishTime(dto.getFinishTime());
		
		return fence;
	}
}
