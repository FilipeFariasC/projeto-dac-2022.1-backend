package br.edu.ifpb.dac.groupd.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.dto.post.FencePostDto;
import br.edu.ifpb.dac.groupd.exception.FenceEmptyException;
import br.edu.ifpb.dac.groupd.exception.FenceNotFoundException;
import br.edu.ifpb.dac.groupd.model.Fence;
import br.edu.ifpb.dac.groupd.repository.FenceRepository;

@Service
public class FenceService {
	@Autowired
	private FenceRepository fenceRepo;
	
	public Fence create(@Valid FencePostDto postDto) {
		Fence fence = fromDto(postDto);
		
		
		return fenceRepo.save(fence);
	}
	public List<Fence> getAll(){
		
		return fenceRepo.findAll();
	}
	public Fence findById(Long id) throws FenceNotFoundException{
		Optional<Fence> register = fenceRepo.findById(id);
		
		if(register.isEmpty())
			throw new FenceNotFoundException(id);
		
		return register.get();
	}
	public Fence update(Long id, @Valid FencePostDto postDto) throws FenceNotFoundException {
		Optional<Fence> register = fenceRepo.findById(id);
		
		if(register.isEmpty())
			throw new FenceNotFoundException(id);
		
		Fence fence = register.get();
		
		Fence updated = fromDto(postDto);
		updated.setId(id);
		updated.setBracelets(fence.getBracelets());
		
		return fenceRepo.save(updated);
	}
	
	public Fence setActive(Long fenceId, Boolean status) throws FenceEmptyException, FenceNotFoundException {
		Optional<Fence> register = fenceRepo.findById(fenceId);
		if(register.isEmpty()) {
			throw new FenceNotFoundException(fenceId);
		}
		
		Fence fence = register.get();
		fence.setActive(status);
		
		return fenceRepo.save(fence);
	}
	
	
	public void delete(Long id) throws FenceNotFoundException {
		if(!fenceRepo.existsById(id))
			throw new FenceNotFoundException(id);
		
		fenceRepo.deleteById(id);
	}
	
	private Fence fromDto(FencePostDto dto) {
		Fence fence = new Fence();
		
		fence.setCoordinate(dto.getCoordinate());
		fence.setRadius(dto.getRadius());
		fence.setStartTime(dto.getStartTime());
		fence.setFinishTime(dto.getFinishTime());
		
		return fence;
	}
}
