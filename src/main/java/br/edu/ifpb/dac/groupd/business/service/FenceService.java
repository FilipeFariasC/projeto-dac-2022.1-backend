package br.edu.ifpb.dac.groupd.business.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.business.exception.FenceEmptyException;
import br.edu.ifpb.dac.groupd.business.exception.FenceNotFoundException;
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
	
	
	public Fence createFence(Long id, FenceRequest dto) throws UserNotFoundException {
		Optional<User> register = userRepo.findById(id);
		
		if (register.isEmpty())
			throw new UserNotFoundException(id);
		
		User user = register.get();
		
		Fence mapped = converter.requestToFence(dto);
		
		Fence fence = fenceRepo.save(mapped);
		user.addFence(fence);
		
		userRepo.save(user);
		fence.setUser(user);
		
		return fence;
	}
	public Page<Fence> getAllFences(Long id, Pageable pageable) throws UserNotFoundException {
		Optional<User> register = userRepo.findById(id);
		
		if (register.isEmpty())
			throw new UserNotFoundException(id);
		
		return fenceRepo.findAllFencesByUser(id, pageable);
	}
	public Page<Fence> searchFencesByName(Long id, String name, Pageable pageable) throws UserNotFoundException {
		boolean register = userRepo.existsById(id);
		
		if (!register)
			throw new UserNotFoundException(id);
		
		return fenceRepo.searchUserFenceByName(id, name, pageable);
	}
	
	public Fence findFenceById(Long id, Long fenceId) throws UserNotFoundException, FenceNotFoundException {
		Optional<User> register = userRepo.findById(id);
		
		if (register.isEmpty())
			throw new UserNotFoundException(id);
		
		User user = register.get();
		
		Optional<Fence> registerFence = user.getFences().stream().filter(fence->fence.getId().equals(fenceId)).findFirst();
		
		if(registerFence.isEmpty()) {
			throw new FenceNotFoundException(fenceId);
		}
		
		return registerFence.get();
	}

	public Fence updateFence(Long id, Long fenceId, FenceRequest dto) throws UserNotFoundException, FenceNotFoundException {
		Optional<User> register = userRepo.findById(id);
		
		if (register.isEmpty())
			throw new UserNotFoundException(id);
		
		User user = register.get();
		
		boolean existe = user.getFences()
				.stream()
				.mapToLong(Fence::getId)
				.anyMatch(fenceIdRegistered -> fenceIdRegistered == fenceId);
		if(!existe) {
			throw new FenceNotFoundException(fenceId);
		}
		
		Fence mapped = converter.requestToFence(dto);
		mapped.setId(fenceId);
		mapped.setUser(user);
		
		return fenceRepo.save(mapped);
	}
	public Fence setActive(Long id, Long fenceId, Boolean status) throws FenceEmptyException, FenceNotFoundException, UserNotFoundException, NoBraceletAvailableException {
		
		Optional<User> register = userRepo.findById(id);
		if(register.isEmpty()) {
			throw new UserNotFoundException(id);
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
	
	public void deleteFence(Long id, Long fenceId) throws UserNotFoundException, FenceNotFoundException {
		Optional<User> register = userRepo.findById(id);
		
		if (register.isEmpty())
			throw new UserNotFoundException(id);
		
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
