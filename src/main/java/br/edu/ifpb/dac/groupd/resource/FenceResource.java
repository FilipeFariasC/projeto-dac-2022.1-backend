package br.edu.ifpb.dac.groupd.resource;


import java.net.URI;
import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

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
import br.edu.ifpb.dac.groupd.exception.NoBraceletAvailableException;
import br.edu.ifpb.dac.groupd.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.model.Fence;
import br.edu.ifpb.dac.groupd.service.FenceService;

@RestController
@RequestMapping({"/fences","/users/fences"})
public class FenceResource {
	// User Fence
	@Autowired
	private FenceService fenceService;
	
	
	@PostMapping
	public ResponseEntity<?> createFence(
			Principal principal,
			@Valid
			@RequestBody
			FencePostDto postDto){
		try {
			Fence fence = fenceService.createFence(principal.getName(), postDto);
			
			FenceDto dto = mapToFenceDto(fence);
			
			return ResponseEntity.status(HttpStatus.CREATED).location(toUri(fence)).body(dto);
		} catch (UserNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	@GetMapping
	public ResponseEntity<?> getAllFences(
			Principal principal){
		try {
			List<FenceDto> dtos = fenceService.getAllFences(principal.getName())
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
			Principal principal,
			@PathVariable("fenceId") Long fenceId){
		try {
			Fence bracelet = fenceService.findFenceById(principal.getName(), fenceId);
			
			FenceDto dto = mapToFenceDto(bracelet);
			
			return ResponseEntity.ok(dto);
		} catch (UserNotFoundException | FenceNotRegisteredException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	
	@PutMapping("/{fenceId}")
	public ResponseEntity<?> updateFence(
			Principal principal,
			@PathVariable("fenceId") Long fenceId,
			@Valid
			@RequestBody
			FencePostDto postDto){
		try {
			Fence fence = fenceService.updateFence(principal.getName(), fenceId, postDto);
			
			FenceDto dto = mapToFenceDto(fence);
			
			return ResponseEntity.ok(dto);
		} catch (UserNotFoundException| FenceNotFoundException | FenceNotRegisteredException  exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		} 
	}
	@PatchMapping("/{fenceId}/setStatus")
	public ResponseEntity<?> setStatus(Principal principal,
			@PathVariable("fenceId") Long fenceId,
			@RequestParam(name="active", required=true) boolean active){
		try {
			Fence fence = fenceService.setActive(principal.getName(), fenceId, active);
			
			FenceDto dto = mapToFenceDto(fence);
			
			return ResponseEntity.ok(dto);
		} catch ( FenceNotFoundException | UserNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		} catch (FenceEmptyException | NoBraceletAvailableException exception) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
		}
	}
	
	@DeleteMapping("/{fenceId}")
	public ResponseEntity<?> deleteUserFence(
			Principal principal,
			@PathVariable("fenceId") Long fenceId){
		try {
			fenceService.deleteFence(principal.getName(), fenceId);
			
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
		dto.setActive(fence.isActive());
		
		return dto;
	}
	private URI toUri (Fence fence) {
		return ServletUriComponentsBuilder
				.fromCurrentRequestUri()
				.path("/{id}")
				.buildAndExpand(fence.getId())
				.toUri();
	}
}
