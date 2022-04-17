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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.edu.ifpb.dac.groupd.dto.BraceletDto;
import br.edu.ifpb.dac.groupd.dto.UserDto;
import br.edu.ifpb.dac.groupd.dto.post.BraceletPostDto;
import br.edu.ifpb.dac.groupd.dto.post.UserPostDto;
import br.edu.ifpb.dac.groupd.exception.BraceletNotFoundException;
import br.edu.ifpb.dac.groupd.exception.BraceletNotRegisteredException;
import br.edu.ifpb.dac.groupd.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.model.Bracelet;
import br.edu.ifpb.dac.groupd.model.User;
import br.edu.ifpb.dac.groupd.service.UserService;

@RestController
@RequestMapping("/users")
public class UserResource {
	@Autowired
	private UserService userService;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private RestTemplate template;
	
	// User Only
	@PostMapping
	@ResponseStatus(code=HttpStatus.CREATED)
	public ResponseEntity<UserDto> create(
			@Valid
			@RequestBody
			UserPostDto userPostDto) {
		
		User created = userService.create(userPostDto);
		
		UserDto dto = mapToUserDto(created);
		
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequestUri()
				.path("/{id}")
				.buildAndExpand(created.getId())
				.toUri();
		
		return ResponseEntity.created(uri).body(dto);
	}
	@GetMapping
	public ResponseEntity<?> getAll() {
		List<UserDto> dtos = userService.getAll()
				.stream()
				.map(this::mapToUserDto)
				.toList();
		
		return ResponseEntity.ok(dtos);
	}
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable("id") Long id){
		try {
			User user = userService.findById(id);
			
			UserDto dto = mapToUserDto(user);
			
			return ResponseEntity.ok(dto);
		} catch (UserNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	@PutMapping("/{id}")
	public ResponseEntity<?> updateById(@PathVariable("id") Long id, 
			@Valid
			@RequestBody
			UserPostDto userPostDto){
		
		try {
			User user = userService.update(id, userPostDto);
			
			UserDto dto = mapToUserDto(user);
			
			URI uri = getUri(user);
			
			return ResponseEntity.status(HttpStatus.OK).location(uri).body(dto);
		} catch (UserNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable("id") Long id){
		try {
			userService.deleteById(id);
			return ResponseEntity.ok(String.format("Usuário de identificador %d deletado!", id));
		} catch (UserNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	
	private UserDto mapToUserDto(User user) {
		return mapper.map(user, UserDto.class);
	}
	
	private URI getUri(User user) {
		return ServletUriComponentsBuilder
				.fromCurrentRequestUri()
				.path("/{id}")
				.buildAndExpand(user.getId())
				.toUri();
	}
	
	// User Bracelet
	
	@PostMapping("/{id}/bracelets")
	public ResponseEntity<?> createBracelet(
			@PathVariable("id") Long id,
			@Valid
			@RequestBody
			BraceletPostDto postDto){
		try {
			Bracelet bracelet = userService.createBracelet(id, postDto);
			
			BraceletDto dto = mapToBraceletDto(bracelet);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(dto);
		} catch (UserNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	@GetMapping("/{id}/bracelets")
	public ResponseEntity<?> getAllBracelets(
			@PathVariable("id") Long id){
		try {
			List<BraceletDto> dtos = userService.getAllBracelets(id)
					.stream()
					.map(this::mapToBraceletDto)
					.toList();
			
			return ResponseEntity.ok(dtos);
		} catch (UserNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	@GetMapping("/{id}/bracelets/search")
	public ResponseEntity<?> searchBraceleByName(
			@PathVariable("id") Long userId,
			@RequestParam(name="name", required=true) String name){
		try {
			List<Bracelet> bracelets = userService.searchBraceletByName(userId, name);
			
			List<BraceletDto> dtos = bracelets
					.stream()
					.map(this::mapToBraceletDto)
					.toList();
			return ResponseEntity.ok(dtos);
		} catch (UserNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	
	@GetMapping("/{id}/bracelets/{braceletId}")
	public ResponseEntity<?> getAllBracelets(
			@PathVariable("id") Long id,
			@PathVariable("braceletId") Long braceletId){
		try {
			Bracelet bracelet = userService.findByBraceletId(id, braceletId);
			
			BraceletDto dto = mapToBraceletDto(bracelet);
			
			return ResponseEntity.ok(dto);
		} catch (UserNotFoundException | BraceletNotRegisteredException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	
	@PutMapping("/{id}/bracelets/{braceletId}")
	public ResponseEntity<?> updateBracelet(
			@PathVariable("id") Long id,
			@PathVariable("braceletId") Long braceletId,
			@Valid
			@RequestBody
			BraceletPostDto postDto){
		try {
			Bracelet bracelet = userService.updateBracelet(id, braceletId, postDto);
			
			BraceletDto dto = mapToBraceletDto(bracelet);
			
			return ResponseEntity.ok(dto);
		} catch (UserNotFoundException| BraceletNotFoundException | BraceletNotRegisteredException  exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		} 
	}
	@DeleteMapping("/{id}/bracelets/{braceletId}")
	public ResponseEntity<?> deleteUserBracelet(
			@PathVariable("id") Long userId,
			@PathVariable("braceletId") Long braceletId){
		try {
			userService.deleteBracelet(userId, braceletId);
			
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
