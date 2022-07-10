package br.edu.ifpb.dac.groupd.presentation.controller;

import java.net.URI;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.edu.ifpb.dac.groupd.business.exception.UserEmailInUseException;
import br.edu.ifpb.dac.groupd.business.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.business.service.UserService;
import br.edu.ifpb.dac.groupd.model.entities.User;
import br.edu.ifpb.dac.groupd.presentation.dto.UserRequest;
import br.edu.ifpb.dac.groupd.presentation.dto.UserResponse;

@RestController
@RequestMapping("/users")
public class UserResource {
	@Autowired
	private UserService userService;
	
	@Autowired
	private ModelMapper mapper;
	
	// User Only
	@PostMapping
	@ResponseStatus(code=HttpStatus.CREATED)
	public ResponseEntity<?> create(
			@Valid
			@RequestBody
			UserRequest userPostDto,
			HttpServletRequest request){
		
		User created;
		try {
			created = userService.create(userPostDto);
		} catch (UserEmailInUseException exception) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
		}
		
		UserResponse dto = mapToUserDto(created);
		
		URI uri = getUri(created);
		
		return ResponseEntity.created(uri).body(dto);
	}
	@GetMapping("/user")
	public ResponseEntity<?> findById(Principal principal){
		try {
			User user = userService.findByEmail(principal.getName());
			
			UserResponse dto = mapToUserDto(user);
			
			return ResponseEntity.ok(dto);
		} catch (UserNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	@PutMapping
	public ResponseEntity<?> updateById(Principal principal, 
			@Valid
			@RequestBody
			UserRequest userPostDto, HttpSession session){
		
		try {
			
			User user = userService.findByEmail(principal.getName());
			
			User updated = userService.update(principal.getName(), userPostDto);
			
			UserResponse dto = mapToUserDto(updated);
			
			URI uri = getUri(updated);
			
			
			
			if(!principal.getName().equals(userPostDto.getEmail()) || userPostDto.getPassword().equals(user.getPassword())) {
				session.invalidate();
			}

			return ResponseEntity.status(HttpStatus.OK).location(uri).body(dto);
		} catch (UserNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		} catch(UserEmailInUseException exception) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
		}
	}
	
	@PatchMapping("/user")
	public ResponseEntity<?> updateUserName(Principal principal, 
			@RequestBody @Valid UserRequest userPostDto){
		try {
			userService.updateUserName(principal.getName(), userPostDto.getName());
			
			return ResponseEntity.ok(String.format("Nome atualizado!", principal.getName()));
		} catch (UserNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	
	@DeleteMapping
	public ResponseEntity<?> deleteById(Principal principal, HttpSession session){
		try {
			userService.deleteByUsername(principal.getName());
			session.invalidate();
			return ResponseEntity.ok(String.format("Usuário de email: %s deletado!", principal.getName()));
		} catch (UserNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	
	private UserResponse mapToUserDto(User user) {
		return mapper.map(user, UserResponse.class);
	}
	
	private URI getUri(User user) {
		return ServletUriComponentsBuilder
				.fromCurrentRequestUri()
				.path("/")
				.build()
				.toUri();
	}
	
}