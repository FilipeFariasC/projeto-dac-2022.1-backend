package br.edu.ifpb.dac.groupd.business.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.edu.ifpb.dac.groupd.business.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.business.service.interfaces.UserService;
import br.edu.ifpb.dac.groupd.business.service.security.TokenServiceImpl;
import br.edu.ifpb.dac.groupd.model.entities.User;

@Component
public class TokenRequestFilter extends OncePerRequestFilter {
	@Autowired
	private TokenServiceImpl tokenService;
	
	@Autowired
	private UserService userService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = tokenService.get(request);
		boolean valid = tokenService.isValid(token);
		
		if(valid) {
			try {
				authenticate(token);
			} catch (UserNotFoundException e) {
				
			}
		}
		
		filterChain.doFilter(request, response);
	}
	
	private void authenticate(String token) throws UserNotFoundException {
		Long userId = tokenService.getUserId(token);
		
		User user = userService.findById(userId);
		UsernamePasswordAuthenticationToken authentication = 
			new UsernamePasswordAuthenticationToken(userId, null, user.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
	}
}