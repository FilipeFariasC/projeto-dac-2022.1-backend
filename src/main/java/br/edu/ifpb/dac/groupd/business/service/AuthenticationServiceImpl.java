package br.edu.ifpb.dac.groupd.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.business.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.business.service.interfaces.AuthenticationService;
import br.edu.ifpb.dac.groupd.business.service.interfaces.TokenService;
import br.edu.ifpb.dac.groupd.business.service.interfaces.UserService;
import br.edu.ifpb.dac.groupd.model.entities.User;



@Service
public class AuthenticationServiceImpl implements AuthenticationService{
	@Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;
	
	public String login(String username, String password) throws UserNotFoundException {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		
		User user = userService.findByUsername(username);

		return tokenService.generate(user);
	}

	public User getLoggedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (User) authentication.getPrincipal();
	}
}