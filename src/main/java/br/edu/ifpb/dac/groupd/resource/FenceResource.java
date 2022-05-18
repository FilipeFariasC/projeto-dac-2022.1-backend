package br.edu.ifpb.dac.groupd.resource;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.edu.ifpb.dac.groupd.dto.FenceDto;
import br.edu.ifpb.dac.groupd.dto.post.FencePostDto;
import br.edu.ifpb.dac.groupd.exception.FenceEmptyException;
import br.edu.ifpb.dac.groupd.exception.FenceNotFoundException;
import br.edu.ifpb.dac.groupd.exception.NoBraceletAvailableException;
import br.edu.ifpb.dac.groupd.model.Fence;
import br.edu.ifpb.dac.groupd.service.FenceService;

@RestController
@RequestMapping("/fences")
public class FenceResource {
	@Autowired
	private FenceService fenceService;
	
	@Autowired
	private ModelMapper mapper;
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<?> create(
			@Valid
			@RequestBody
			FencePostDto postDto){
		
		Fence fence = fenceService.create(postDto);
		
		URI uri = toUri(fence);
		
		return ResponseEntity.created(uri).body(toDto(fence));
	}
	@GetMapping
	public ResponseEntity<?> getAll(){
		List<FenceDto> dtos = fenceService.getAll()
				.stream()
				.map(this::toDto)
				.toList();
		
		return ResponseEntity.ok(dtos);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable("id") Long id){
		try {
			Fence fence =  fenceService.findById(id);
			
			return ResponseEntity.ok(toDto(fence));
		} catch (FenceNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	
	@PutMapping("/{id}")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<?> update(
			@PathVariable("id")
			Long id,
			@Valid
			@RequestBody
			FencePostDto postDto){
		try {
			Fence fence = fenceService.update(id, postDto);
			
			URI uri = toUri(fence);
			
			return ResponseEntity.created(uri).body(toDto(fence));
		} catch (FenceNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	@PatchMapping("/{id}/setStatus")
	public ResponseEntity<?> active(
			@PathVariable("id") Long fenceId,
			@RequestParam(name="status", required=true) boolean status){
		try {
			Fence fence = fenceService.setActive(fenceId, status);
			
			URI uri = toUri(fence);
			
			return ResponseEntity.created(uri).body(toDto(fence));
		} catch (FenceNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		} catch (FenceEmptyException | NoBraceletAvailableException exception) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
		}
	}
	
	@DeleteMapping("/{ìd}")
	public ResponseEntity<?> delete(
			@PathVariable("id")
			Long id){
		try {
			fenceService.delete(id);
			return ResponseEntity.ok(String.format("Remoção da cerca com identificador id %s bem sucedida!", id));
		} catch (FenceNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	
	private URI toUri (Fence fence) {
		return ServletUriComponentsBuilder
				.fromCurrentRequestUri()
				.path("/{id}")
				.buildAndExpand(fence.getId())
				.toUri();
	}
	
	private FenceDto toDto(Fence fence) {
		return mapper.map(fence, FenceDto.class);
	}
}
