package br.edu.ifpb.dac.groupd.business.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.business.exception.FenceEmptyException;
import br.edu.ifpb.dac.groupd.business.exception.FenceNotFoundException;
import br.edu.ifpb.dac.groupd.business.exception.FenceNotRegisteredException;
import br.edu.ifpb.dac.groupd.business.exception.NoBraceletAvailableException;
import br.edu.ifpb.dac.groupd.business.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.business.service.converter.FenceConverterService;
import br.edu.ifpb.dac.groupd.model.entities.Fence;
import br.edu.ifpb.dac.groupd.model.entities.User;
import br.edu.ifpb.dac.groupd.model.repository.FenceRepository;
import br.edu.ifpb.dac.groupd.model.repository.UserRepository;
import br.edu.ifpb.dac.groupd.presentation.dto.FenceRequest;

@Service
public class FenceService {
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private FenceRepository fenceRepo;
	
	@Autowired
	private FenceConverterService converter;
	
	
	public Fence createFence(String username, FenceRequest dto) throws UserNotFoundException {
		Optional<User> register = userRepo.findByEmail(username);
		
		if (register.isEmpty())
			throw new UserNotFoundException(username);
		
		User user = register.get();
		
		Fence mapped = converter.requestToFence(dto);
		
		Fence fence = fenceRepo.save(mapped);
		user.addFence(fence);
		
		userRepo.save(user);
		fence.setUser(user);
		
		return fence;
	}
	public Page<Fence> getAllFences(String username, Pageable pageable) throws UserNotFoundException {
		Optional<User> register = userRepo.findByEmail(username);
		
		if (register.isEmpty())
			throw new UserNotFoundException(username);
		
		return fenceRepo.findByUserEmail(username, pageable);
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

	public Fence updateFence(String username, Long fenceId, FenceRequest dto) throws UserNotFoundException, FenceNotFoundException {
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
		
		Fence mapped = converter.requestToFence(dto);
		mapped.setId(fenceId);
		mapped.setUser(user);
		
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
	
}
