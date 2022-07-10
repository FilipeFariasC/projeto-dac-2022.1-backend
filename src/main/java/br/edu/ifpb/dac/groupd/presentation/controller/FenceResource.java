package br.edu.ifpb.dac.groupd.presentation.controller;


import java.net.URI;
import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import br.edu.ifpb.dac.groupd.business.exception.FenceEmptyException;
import br.edu.ifpb.dac.groupd.business.exception.FenceNotFoundException;
import br.edu.ifpb.dac.groupd.business.exception.FenceNotRegisteredException;
import br.edu.ifpb.dac.groupd.business.exception.NoBraceletAvailableException;
import br.edu.ifpb.dac.groupd.business.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.business.service.FenceService;
import br.edu.ifpb.dac.groupd.business.service.converter.FenceConverterService;
import br.edu.ifpb.dac.groupd.model.entities.Fence;
import br.edu.ifpb.dac.groupd.presentation.dto.FenceRequest;
import br.edu.ifpb.dac.groupd.presentation.dto.FenceResponse;

@RestController
@RequestMapping({"/fences","/users/fences"})
public class FenceResource {
	// User Fence
	@Autowired
	private FenceService fenceService;
	
	@Autowired
	private FenceConverterService converter;
	
	
	@PostMapping
	public ResponseEntity<?> createFence(
			Principal principal,
			@Valid
			@RequestBody
			FenceRequest postDto){
		try {
			Fence fence = fenceService.createFence(principal.getName(), postDto);
			
			FenceResponse dto = converter.fenceToResponse(fence);
			
			return ResponseEntity.status(HttpStatus.CREATED).location(toUri(fence)).body(dto);
		} catch (UserNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	@GetMapping
	public ResponseEntity<?> getAllFences(
			Principal principal, Pageable pageable
			){
		try {
			
			Page<Fence> pageFences = fenceService.getAllFences(principal.getName(), pageable);
			Page<FenceResponse> dtos = pageFences
					.map(converter::fenceToResponse);
			
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
			
			FenceResponse dto = converter.fenceToResponse(bracelet);
			
			return ResponseEntity.ok(dto);
		} catch (UserNotFoundException | FenceNotRegisteredException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	@GetMapping("/search")
	public ResponseEntity<?> searchFenceByName(
			Principal principal,
			@RequestParam("name") String name,
			Pageable pageable){
		try {
			Page<Fence> bracelets = fenceService.searchFencesByName(principal.getName(), name, pageable);
			
			Page<FenceResponse> dto = bracelets.map(converter::fenceToResponse);
			
			return ResponseEntity.ok(dto);
		} catch (UserNotFoundException exception) {
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
			
			FenceResponse dto = converter.fenceToResponse(fence);
			
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
			
			FenceResponse dto = converter.fenceToResponse(fence);
			
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
	private URI toUri (Fence fence) {
		return ServletUriComponentsBuilder
				.fromCurrentRequestUri()
				.path("/{id}")
				.buildAndExpand(fence.getId())
				.toUri();
	}
}
