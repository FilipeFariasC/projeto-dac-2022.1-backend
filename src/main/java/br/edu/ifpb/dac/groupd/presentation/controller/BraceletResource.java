package br.edu.ifpb.dac.groupd.presentation.controller;

import java.net.URI;
import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.edu.ifpb.dac.groupd.business.exception.BraceletNotFoundException;
import br.edu.ifpb.dac.groupd.business.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.business.service.BraceletService;
import br.edu.ifpb.dac.groupd.business.service.converter.BraceletConverterService;
import br.edu.ifpb.dac.groupd.model.entities.Bracelet;
import br.edu.ifpb.dac.groupd.presentation.dto.BraceletRequest;
import br.edu.ifpb.dac.groupd.presentation.dto.BraceletResponse;

@RestController
@RequestMapping({"/bracelets", "/users/bracelets"})
public class BraceletResource {
	// User Bracelet
	@Autowired
	private BraceletService braceletService;
	
	@Autowired
	private BraceletConverterService converter;
	
	@PostMapping
	public ResponseEntity<?> createBracelet(
			Principal principal,
			@Valid
			@RequestBody
			BraceletRequest postDto){
		try {
			Bracelet bracelet = braceletService.createBracelet(principal.getName(), postDto);
			
			BraceletResponse dto = converter.braceletToResponse(bracelet);
			
			return ResponseEntity.created(getUri(bracelet)).body(dto);
		} catch (UserNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	@GetMapping
	public ResponseEntity<?> getAllBracelets(Principal principal, Pageable pageable){
		try {
			Page<Bracelet> pageBracelets = braceletService.getAllBracelets(principal.getName(), pageable);
			Page<BraceletResponse> pageDtos = pageBracelets
					.map(converter::braceletToResponse);
			
			
			return ResponseEntity.ok(pageDtos);
		} catch (UserNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	
	@GetMapping("/{braceletId}")
	public ResponseEntity<?> getAllBracelets(
			Principal principal,
			@PathVariable("braceletId") Long braceletId){
		try {
			Bracelet bracelet = braceletService.findByBraceletId(principal.getName(), braceletId);
			
			BraceletResponse dto = converter.braceletToResponse(bracelet);
			
			return ResponseEntity.ok(dto);
		} catch (UserNotFoundException | BraceletNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	@GetMapping("/search")
	public ResponseEntity<?> searchBraceletByName(
			Principal principal,
			@RequestParam("name") String name,
			Pageable pageable){
		try {
			Page<Bracelet> bracelets = braceletService.searchBraceletByName(principal.getName(), name, pageable);
			
			Page<BraceletResponse> dto = bracelets.map(converter::braceletToResponse);
			
			return ResponseEntity.ok(dto);
		} catch (UserNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	
	@PutMapping("/{braceletId}")
	public ResponseEntity<?> updateBracelet(
			Principal principal,
			@PathVariable("braceletId") Long braceletId,
			@Valid
			@RequestBody
			BraceletRequest postDto){
		try {
			Bracelet bracelet = braceletService.updateBracelet(principal.getName(), braceletId, postDto);
			
			BraceletResponse dto = converter.braceletToResponse(bracelet);
			
			return ResponseEntity.status(HttpStatus.OK).location(getUri(bracelet)).body(dto);
		} catch (UserNotFoundException| BraceletNotFoundException  exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		} 
	}
	@DeleteMapping("/{braceletId}")
	public ResponseEntity<?> deleteUserBracelet(
			Principal principal,
			@PathVariable("braceletId") Long braceletId){
		try {
			braceletService.deleteBracelet(principal.getName(), braceletId);
			
			return ResponseEntity.noContent().build();
		} catch (UserNotFoundException | BraceletNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	
	private URI getUri(Bracelet bracelet) {
		return ServletUriComponentsBuilder
				.fromCurrentRequestUri()
				.path("/")
				.buildAndExpand(bracelet.getId())
				.toUri();
	}
}
