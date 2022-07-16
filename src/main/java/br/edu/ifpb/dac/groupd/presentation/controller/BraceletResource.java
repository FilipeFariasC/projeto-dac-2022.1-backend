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
			BraceletRequest postDto) throws UserNotFoundException{
		Bracelet bracelet = braceletService.createBracelet(getPrincipalId(principal), postDto);
		
		BraceletResponse dto = converter.braceletToResponse(bracelet);
		
		return ResponseEntity.created(getUri(bracelet)).body(dto);

	}
	@GetMapping
	public ResponseEntity<?> getAllBracelets(Principal principal, Pageable pageable) throws UserNotFoundException{
		Page<Bracelet> pageBracelets = braceletService.getAllBracelets(getPrincipalId(principal), pageable);
		Page<BraceletResponse> pageDtos = pageBracelets
				.map(converter::braceletToResponse);
		
		
		return ResponseEntity.ok(pageDtos);

	}
	
	@GetMapping("/{braceletId}")
	public ResponseEntity<?> getAllBracelets(
			Principal principal,
			@PathVariable("braceletId") Long braceletId) throws UserNotFoundException, BraceletNotFoundException{
		Bracelet bracelet = braceletService.findByBraceletId(getPrincipalId(principal), braceletId);
		
		BraceletResponse dto = converter.braceletToResponse(bracelet);
		
		return ResponseEntity.ok(dto);

	}
	@GetMapping("/search")
	public ResponseEntity<?> searchBraceletByName(
			Principal principal,
			@RequestParam("name") String name,
			Pageable pageable) throws UserNotFoundException{
		Page<Bracelet> bracelets = braceletService.searchBraceletByName(getPrincipalId(principal), name, pageable);
		
		Page<BraceletResponse> dto = bracelets.map(converter::braceletToResponse);
		
		return ResponseEntity.ok(dto);
	}
	
	@PutMapping("/{braceletId}")
	public ResponseEntity<?> updateBracelet(
			Principal principal,
			@PathVariable("braceletId") Long braceletId,
			@Valid
			@RequestBody
			BraceletRequest postDto) throws UserNotFoundException, BraceletNotFoundException{
		Bracelet bracelet = braceletService.updateBracelet(getPrincipalId(principal), braceletId, postDto);
		
		BraceletResponse dto = converter.braceletToResponse(bracelet);
		
		return ResponseEntity.status(HttpStatus.OK).location(getUri(bracelet)).body(dto);
	}
	@DeleteMapping("/{braceletId}")
	public ResponseEntity<?> deleteUserBracelet(
			Principal principal,
			@PathVariable("braceletId") Long braceletId) throws UserNotFoundException, BraceletNotFoundException{
		braceletService.deleteBracelet(getPrincipalId(principal), braceletId);
		
		return ResponseEntity.noContent().build();
	}
	
	private URI getUri(Bracelet bracelet) {
		return ServletUriComponentsBuilder
				.fromCurrentRequestUri()
				.path("/")
				.buildAndExpand(bracelet.getId())
				.toUri();
	}
	
	private Long getPrincipalId(Principal principal) {
		return Long.parseLong(principal.getName());
	}
}
