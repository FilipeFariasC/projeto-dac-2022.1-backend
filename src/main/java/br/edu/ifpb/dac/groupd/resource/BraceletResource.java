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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.edu.ifpb.dac.groupd.dto.BraceletDto;
import br.edu.ifpb.dac.groupd.dto.post.BraceletPostDto;
import br.edu.ifpb.dac.groupd.exception.BraceletNotFoundException;
import br.edu.ifpb.dac.groupd.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.model.Bracelet;
import br.edu.ifpb.dac.groupd.service.BraceletService;

@RestController
@RequestMapping({"/bracelets", "/users/bracelets"})
public class BraceletResource {
	// User Bracelet
	@Autowired
	private BraceletService braceletService;
	
	@PostMapping
	public ResponseEntity<?> createBracelet(
			Principal principal,
			@Valid
			@RequestBody
			BraceletPostDto postDto){
		try {
			Bracelet bracelet = braceletService.createBracelet(principal.getName(), postDto);
			
			BraceletDto dto = mapToBraceletDto(bracelet);
			
			return ResponseEntity.created(getUri(bracelet)).body(dto);
		} catch (UserNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	@GetMapping
	public ResponseEntity<?> getAllBracelets(Principal principal){
		try {
			List<BraceletDto> dtos = braceletService.getAllBracelets(principal.getName())
					.stream()
					.map(this::mapToBraceletDto)
					.toList();
			
			return ResponseEntity.ok(dtos);
		} catch (UserNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	@GetMapping("/search")
	public ResponseEntity<?> searchBraceleByName(
			Principal principal,
			@RequestParam(name="name", required=true) String name){
		try {
			List<Bracelet> bracelets = braceletService.searchBraceletByName(principal.getName(), name);
			
			List<BraceletDto> dtos = bracelets
					.stream()
					.map(this::mapToBraceletDto)
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
			
			BraceletDto dto = mapToBraceletDto(bracelet);
			
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
			BraceletPostDto postDto){
		try {
			Bracelet bracelet = braceletService.updateBracelet(principal.getName(), braceletId, postDto);
			
			BraceletDto dto = mapToBraceletDto(bracelet);
			
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
	
	private BraceletDto mapToBraceletDto(Bracelet bracelet) {
		BraceletDto dto = new BraceletDto();
		
		dto.setId(bracelet.getId());
		dto.setName(bracelet.getName());
		
		return dto;
	}
	private URI getUri(Bracelet bracelet) {
		return ServletUriComponentsBuilder
				.fromCurrentRequestUri()
				.path("/")
				.buildAndExpand(bracelet.getId())
				.toUri();
	}
}
