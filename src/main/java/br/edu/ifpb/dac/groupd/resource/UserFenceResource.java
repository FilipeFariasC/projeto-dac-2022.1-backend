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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.edu.ifpb.dac.groupd.dto.FenceDto;
import br.edu.ifpb.dac.groupd.dto.post.FencePostDto;
import br.edu.ifpb.dac.groupd.exception.FenceEmptyException;
import br.edu.ifpb.dac.groupd.exception.FenceNotFoundException;
import br.edu.ifpb.dac.groupd.exception.FenceNotRegisteredException;
import br.edu.ifpb.dac.groupd.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.model.Fence;
import br.edu.ifpb.dac.groupd.service.UserFenceService;

@RestController
@RequestMapping("/users/{userId}/fences")
public class UserFenceResource {
	// User Fence
	@Autowired
	private UserFenceService userFenceService;
	
	@Autowired
	private ModelMapper mapper;
	
	@PostMapping
	public ResponseEntity<?> createFence(
			@PathVariable("userId") Long userId,
			@Valid
			@RequestBody
			FencePostDto postDto){
		try {
			Fence fence = userFenceService.createFence(userId, postDto);
			
			FenceDto dto = mapToFenceDto(fence);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(dto);
		} catch (UserNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	@GetMapping
	public ResponseEntity<?> getAllFences(
			@PathVariable("iuserId") Long id){
		try {
			List<FenceDto> dtos = userFenceService.getAllFences(id)
					.stream()
					.map(this::mapToFenceDto)
					.toList();
			
			return ResponseEntity.ok(dtos);
		} catch (UserNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	
	@GetMapping("/{fenceId}")
	public ResponseEntity<?> getAllFences(
			@PathVariable("userId") Long id,
			@PathVariable("fenceId") Long fenceId){
		try {
			Fence bracelet = userFenceService.findFenceById(id, fenceId);
			
			FenceDto dto = mapToFenceDto(bracelet);
			
			return ResponseEntity.ok(dto);
		} catch (UserNotFoundException | FenceNotRegisteredException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	
	@PutMapping("/{fenceId}")
	public ResponseEntity<?> updateFence(
			@PathVariable("id") Long id,
			@PathVariable("fenceId") Long fenceId,
			@Valid
			@RequestBody
			FencePostDto postDto){
		try {
			Fence fence = userFenceService.updateFence(id, fenceId, postDto);
			
			FenceDto dto = mapToFenceDto(fence);
			
			return ResponseEntity.ok(dto);
		} catch (UserNotFoundException| FenceNotFoundException | FenceNotRegisteredException  exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		} 
	}
	@PatchMapping("/{fenceId}/setStatus")
	public ResponseEntity<?> active(
			@PathVariable("userId") Long fenceId,
			@RequestParam(name="status", required=true) boolean status){
		try {
			Fence fence = userFenceService.setActive(fenceId, status);
			
			URI uri = toUri(fence);
			
			return ResponseEntity.created(uri).body(toDto(fence));
		} catch (FenceNotFoundException | FenceEmptyException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	
	@DeleteMapping("/{fenceId}")
	public ResponseEntity<?> deleteUserFence(
			@PathVariable("userId") Long userId,
			@PathVariable("fenceId") Long fenceId){
		try {
			userFenceService.deleteFence(userId, fenceId);
			
			return ResponseEntity.noContent().build();
		} catch (UserNotFoundException | FenceNotFoundException | FenceNotRegisteredException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	
	private FenceDto mapToFenceDto(Fence fence) {
		FenceDto dto = new FenceDto();
		
		dto.setId(fence.getId());
		dto.setCoordinate(fence.getCoordinate());
		dto.setStartTime(fence.getStartTime());
		dto.setFinishTime(fence.getFinishTime());
		dto.setRadius(fence.getRadius());
		dto.isActive(fence.isActive());
		
		return dto;
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
