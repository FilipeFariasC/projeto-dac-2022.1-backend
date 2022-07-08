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
	public ResponseEntity<?> getAllBracelets(Principal principal,
			@RequestParam(name = "page", required=false) int page,
			@RequestParam(name = "size", required=false) int size,
			@RequestParam(name = "sort", required=false) String sort
			){
		try {
			Pageable pageable = getPageable(page, size, sort);
			Page<Bracelet> pageBracelets = braceletService.getAllBracelets(principal.getName(), pageable);
			Page<BraceletResponse> pageDtos = pageBracelets
					.map(converter::braceletToResponse);
			
			
			return ResponseEntity.ok(pageDtos);
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
		return Sort.by("bracelet_id");
//		return Sort.by("id");
	}
	private Sort getSort(String sort) {
		if(sort == null || sort.isEmpty() || sort.isBlank()) {
//			return Sort.by("bracelet_id").ascending();
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

	@GetMapping("/search")
	public ResponseEntity<?> searchBraceleByName(
			Principal principal,
			@RequestParam(name="name", required=true) String name){
		try {
			List<Bracelet> bracelets = braceletService.searchBraceletByName(principal.getName(), name);
			
			List<BraceletResponse> dtos = bracelets
					.stream()
					.map(converter::braceletToResponse)
					.toList();
			return ResponseEntity.ok(dtos);
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
