package br.edu.ifpb.dac.groupd.presentation.controller;

import java.net.URI;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
import br.edu.ifpb.dac.groupd.business.service.UserServiceImpl;
import br.edu.ifpb.dac.groupd.business.service.converter.UserConverterService;
import br.edu.ifpb.dac.groupd.model.entities.User;
import br.edu.ifpb.dac.groupd.presentation.dto.UserRequest;
import br.edu.ifpb.dac.groupd.presentation.dto.UserResponse;

@RestController
@RequestMapping("/users")
public class UserResource {
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private UserConverterService converter;
	
	// User Only
	@PostMapping
	@ResponseStatus(code=HttpStatus.CREATED)
	public ResponseEntity<?> create(
			@Valid
			@RequestBody
			UserRequest userPostDto,
			HttpServletRequest request) throws UserEmailInUseException{
		
		User created;
		created = userService.create(userPostDto);
		
		UserResponse dto = converter.userToResponse(created);
		
		URI uri = getUri(created);
		
		return ResponseEntity.created(uri).body(dto);
	}
	@GetMapping("/user")
	public ResponseEntity<?> findById(Principal principal){
		try {
			User user = userService.findById(Long.parseLong(principal.getName()));
			
			UserResponse dto = converter.userToResponse(user);
			
			return ResponseEntity.ok(dto);
		} catch (UserNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	@PutMapping
	public ResponseEntity<?> updateById(Principal principal, 
			@Valid
			@RequestBody
			UserRequest userPostDto, HttpSession session) throws UserNotFoundException, UserEmailInUseException{
		
			Long id = getPrincipalId(principal);
			User user = converter.requestToUser(userPostDto);
			user.setId(id);
			
			
			User updated = userService.update(user);
			
			UserResponse dto = converter.userToResponse(updated);
			
			URI uri = getUri(updated);

			return ResponseEntity.status(HttpStatus.OK).location(uri).body(dto);
	}
	
	@PatchMapping("/user")
	public ResponseEntity<?> updateUserName(Principal principal, 
			@RequestBody @Valid UserRequest userPostDto) throws UserNotFoundException{

		userService.updateUserName(principal.getName(), userPostDto.getName());
		
		return ResponseEntity.ok(String.format("Nome atualizado!", principal.getName()));

	}
	
	@DeleteMapping("/user")
	public ResponseEntity<?> deleteById(Principal principal, HttpSession session) throws UserNotFoundException{
		userService.delete(getPrincipalId(principal));
		session.invalidate();
		return ResponseEntity.ok("Usuário deletado!");
	}
	
	private URI getUri(User user) {
		return ServletUriComponentsBuilder
				.fromCurrentRequestUri()
				.path("/")
				.build()
				.toUri();
	}
	
	private Long getPrincipalId(Principal principal) {
		return Long.parseLong(principal.getName());
	}
	
}
