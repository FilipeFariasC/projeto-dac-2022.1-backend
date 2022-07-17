package br.edu.ifpb.dac.groupd.presentation.controller;

import java.time.LocalDateTime;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import br.edu.ifpb.dac.groupd.business.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.business.service.DateUtilsService;
import br.edu.ifpb.dac.groupd.business.service.converter.UserConverterService;
import br.edu.ifpb.dac.groupd.business.service.interfaces.AuthenticationService;
import br.edu.ifpb.dac.groupd.business.service.interfaces.TokenService;
import br.edu.ifpb.dac.groupd.business.service.interfaces.UserService;
import br.edu.ifpb.dac.groupd.model.entities.User;
import br.edu.ifpb.dac.groupd.presentation.dto.UserResponse;
import br.edu.ifpb.dac.groupd.presentation.dto.security.AuthenticationResponse;
import br.edu.ifpb.dac.groupd.presentation.dto.security.TokenValidResponse;
import br.edu.ifpb.dac.groupd.presentation.dto.security.UserDetailsRequest;

@RestController
@RequestMapping
@Scope(value=WebApplicationContext.SCOPE_SESSION)
public class AuthenticationController {
	@Autowired
	private AuthenticationService loginService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserConverterService converter;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private DateUtilsService dateService;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(
			@Valid
			@RequestBody
			UserDetailsRequest userDetailsDto,
			HttpServletRequest request) throws UserNotFoundException{
		
		String token = loginService.login(userDetailsDto.getUsername(), userDetailsDto.getPassword());
		User user = userService.findByUsername(userDetailsDto.getUsername());
		
		UserResponse userResponse = converter.userToResponse(user);
		AuthenticationResponse response = new AuthenticationResponse(token, userResponse);
		
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/isValidToken")
	public ResponseEntity<?> isTokenValid(@RequestBody AuthenticationResponse dto ){
		
		boolean isValidToken = tokenService.isValid(dto.getToken());
		
		try {
			Date exp = tokenService.getClaims(dto.getToken()).getExpiration();
			
			TokenValidResponse response = new TokenValidResponse();
			LocalDateTime dateTimeExpiration = dateService.convertDateToLocalDateTime(exp);
			response.setExpirationTime(dateTimeExpiration);
			response.setToken(dto.getToken());
			response.setValid(isValidToken);
			
			return ResponseEntity.ok(response);
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	
	@PostMapping("/logout")
	public ResponseEntity<?> logout(
			HttpSession session){
		try {
			session.invalidate();
			
			return ResponseEntity.ok("Logout concluído!");
		} catch  (IllegalStateException exception) {
			return ResponseEntity.badRequest().body("Você já está deslogado!");
		}
	}
}
