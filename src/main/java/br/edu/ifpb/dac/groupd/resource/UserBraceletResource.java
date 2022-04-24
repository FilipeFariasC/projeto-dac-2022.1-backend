package br.edu.ifpb.dac.groupd.resource;

import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
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

import br.edu.ifpb.dac.groupd.dto.BraceletDto;
import br.edu.ifpb.dac.groupd.dto.post.BraceletPostDto;
import br.edu.ifpb.dac.groupd.exception.BraceletNotFoundException;
import br.edu.ifpb.dac.groupd.exception.BraceletNotRegisteredException;
import br.edu.ifpb.dac.groupd.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.model.Bracelet;
import br.edu.ifpb.dac.groupd.service.UserBraceletService;

@RestController
@RequestMapping("/users/{id}/bracelets")
public class UserBraceletResource {
	// User Bracelet
	@Autowired
	private UserBraceletService userBraceletService;
	
	@Autowired
	private ModelMapper mapper;
	
	@PostMapping
	public ResponseEntity<?> createBracelet(
			@PathVariable("id") Long userId,
			@Valid
			@RequestBody
			BraceletPostDto postDto){
		try {
			Bracelet bracelet = userBraceletService.createBracelet(userId, postDto);
			
			BraceletDto dto = mapToBraceletDto(bracelet);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(dto);
		} catch (UserNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	@GetMapping
	public ResponseEntity<?> getAllBracelets(
			@PathVariable("id") Long id){
		try {
			List<BraceletDto> dtos = userBraceletService.getAllBracelets(id)
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
			@PathVariable("id") Long userId,
			@RequestParam(name="name", required=true) String name){
		try {
			List<Bracelet> bracelets = userBraceletService.searchBraceletByName(userId, name);
			
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
			@PathVariable("id") Long id,
			@PathVariable("braceletId") Long braceletId){
		try {
			Bracelet bracelet = userBraceletService.findByBraceletId(id, braceletId);
			
			BraceletDto dto = mapToBraceletDto(bracelet);
			
			return ResponseEntity.ok(dto);
		} catch (UserNotFoundException | BraceletNotRegisteredException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	
	@PutMapping("/{braceletId}")
	public ResponseEntity<?> updateBracelet(
			@PathVariable("id") Long id,
			@PathVariable("braceletId") Long braceletId,
			@Valid
			@RequestBody
			BraceletPostDto postDto){
		try {
			Bracelet bracelet = userBraceletService.updateBracelet(id, braceletId, postDto);
			
			BraceletDto dto = mapToBraceletDto(bracelet);
			
			return ResponseEntity.ok(dto);
		} catch (UserNotFoundException| BraceletNotFoundException | BraceletNotRegisteredException  exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		} 
	}
	@DeleteMapping("/{braceletId}")
	public ResponseEntity<?> deleteUserBracelet(
			@PathVariable("id") Long userId,
			@PathVariable("braceletId") Long braceletId){
		try {
			userBraceletService.deleteBracelet(userId, braceletId);
			
			return ResponseEntity.noContent().build();
		} catch (UserNotFoundException | BraceletNotFoundException | BraceletNotRegisteredException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	
	private BraceletDto mapToBraceletDto(Bracelet bracelet) {
		BraceletDto dto = new BraceletDto();
		
		dto.setIdBracelet(bracelet.getId());
		dto.setName(bracelet.getName());
		
		return dto;
	}
}
