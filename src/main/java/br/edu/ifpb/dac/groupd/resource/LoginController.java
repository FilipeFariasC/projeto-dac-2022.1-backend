package br.edu.ifpb.dac.groupd.resource;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.dac.groupd.dto.post.security.UserDetailsDto;
import br.edu.ifpb.dac.groupd.service.LoginService;

@RestController
@RequestMapping
public class LoginController {
	@Autowired
	private LoginService loginService;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(
			Principal principal,
			@Valid
			@RequestBody
			UserDetailsDto userDetailsDto,
			HttpServletRequest request){
		try {
			return ResponseEntity.ok(loginService.login(userDetailsDto));
		} catch(AuthenticationException exception) {
			return ResponseEntity.badRequest().body(exception.getMessage());
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
