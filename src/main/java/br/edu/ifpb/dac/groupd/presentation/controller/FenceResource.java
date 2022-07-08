package br.edu.ifpb.dac.groupd.presentation.controller;


import java.net.URI;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.edu.ifpb.dac.groupd.business.exception.FenceEmptyException;
import br.edu.ifpb.dac.groupd.business.exception.FenceNotFoundException;
import br.edu.ifpb.dac.groupd.business.exception.FenceNotRegisteredException;
import br.edu.ifpb.dac.groupd.business.exception.NoBraceletAvailableException;
import br.edu.ifpb.dac.groupd.business.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.business.service.FenceService;
import br.edu.ifpb.dac.groupd.model.entities.Fence;
import br.edu.ifpb.dac.groupd.presentation.dto.FenceRequest;
import br.edu.ifpb.dac.groupd.presentation.dto.FenceResponse;

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
			FenceRequest postDto){
		try {
			Fence fence = fenceService.createFence(principal.getName(), postDto);
			
			FenceResponse dto = mapToFenceDto(fence);
			
			return ResponseEntity.status(HttpStatus.CREATED).location(toUri(fence)).body(dto);
		} catch (UserNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	@GetMapping
	public ResponseEntity<?> getAllFences(
			Principal principal,
			@RequestHeader(name = "page", required=false) int page,
			@RequestHeader(name = "size", required=false) int size,
			@RequestHeader(name = "sort", required=false) String sort
			){
		try {
			Pageable pageable = getPageable(page, size, sort);
			
			Page<Fence> pageFences = fenceService.getAllFences(principal.getName(), pageable);
			Page<FenceResponse> dtos = pageFences
					.map(this::mapToFenceDto);
			
			return ResponseEntity.ok(dtos);
		} catch (UserNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	private Pageable getPageable(int page, int size, String sort){
		if(size == 0) {
			size = 5;
		}
		Sort sorted = getSort(sort);
		return PageRequest.of(page, size, sorted);
	}
	
	private Sort byId() {
		return Sort.by("fence_id");
//		return Sort.by("id");
	}
	
	private Sort getSort(String sort) {
		if(sort == null || sort.isEmpty() || sort.isBlank()) {
			return byId().ascending();
		}
		
		
		String[] arguments = sort.split(",");
		arguments = Arrays.stream(arguments).map(String::trim).toArray(String[]::new);
		
		String[] fields = Arrays.stream(arguments).filter(n ->{
			return !(n.equalsIgnoreCase("ASC") || n.equalsIgnoreCase("DESC"));
		}).toArray(String[]::new);

		String[] order = Arrays.stream(arguments).filter(n ->{
			return (n.equalsIgnoreCase("ASC") || n.equalsIgnoreCase("DESC"));
		}).toArray(String[]::new);

		Sort sortedFields = sortFields(fields);
		
		return sortOrder(sortedFields, order);
	}
	Sort sortFields(String[] arguments) {
		boolean first = true;
		Sort sorted = null;
		
		
		for(int i = 0; i < arguments.length; i++) {
			String argument = arguments[i].toUpperCase();
			if(first) {
				sorted = switch(argument){
					case "ID" ->{
						first = false;
						yield byId();
					}
					case "NAME" ->{
						first = false;
						yield Sort.by("name");
					}
					case "RADIUS" ->{
						first = false;
						yield Sort.by("radius");
					}
					default -> byId();
				};
			} else {
				switch(argument){
					case "ID" -> sorted.and(byId());
					case "NAME" -> sorted.and(Sort.by("name"));
				}
			}
				
		}
		return sorted;
	}
	Sort sortOrder(Sort sorted, String[] arguments) {
		for(int i = 0; i < arguments.length; i++) {
			String argument = arguments[i];
			if(argument.toUpperCase().equals("ASC")){
				return sorted.ascending();
			} else if(argument.toUpperCase().equals("DESC")) {
				return sorted.descending();
			};
		}
		return sorted;
	}
	
	@GetMapping("/{fenceId}")
	public ResponseEntity<?> getAllFences(
			Principal principal,
			@PathVariable("fenceId") Long fenceId){
		try {
			Fence bracelet = fenceService.findFenceById(principal.getName(), fenceId);
			
			FenceResponse dto = mapToFenceDto(bracelet);
			
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
			FenceRequest postDto){
		try {
			Fence fence = fenceService.updateFence(principal.getName(), fenceId, postDto);
			
			FenceResponse dto = mapToFenceDto(fence);
			
			return ResponseEntity.ok(dto);
		} catch (UserNotFoundException| FenceNotFoundException   exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		} 
	}
	@PatchMapping("/{fenceId}/setStatus")
	public ResponseEntity<?> setStatus(Principal principal,
			@PathVariable("fenceId") Long fenceId,
			@RequestParam(name="active", required=true) boolean active){
		try {
			Fence fence = fenceService.setActive(principal.getName(), fenceId, active);
			
			FenceResponse dto = mapToFenceDto(fence);
			
			return ResponseEntity.ok(dto);
		} catch ( FenceNotFoundException | UserNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		} catch (FenceEmptyException | NoBraceletAvailableException exception) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
		}
	}
	
	@DeleteMapping("/{fenceId}")
	public ResponseEntity<?> deleteFence(
			Principal principal,
			@PathVariable("fenceId") Long fenceId){
		try {
			fenceService.deleteFence(principal.getName(), fenceId);
			
			return ResponseEntity.noContent().build();
		} catch (UserNotFoundException | FenceNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	
	private FenceResponse mapToFenceDto(Fence fence) {
		FenceResponse dto = new FenceResponse();
		
		dto.setId(fence.getId());
		dto.setName(fence.getName());
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
