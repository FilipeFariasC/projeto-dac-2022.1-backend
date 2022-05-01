package br.edu.ifpb.dac.groupd.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authorizationHeader = request.getHeader("Authorization");
		
		String username = null;
		String jwtToken = null;
		
		if(authorizationHeader != null && 
				authorizationHeader.startsWith("Bearer ")) {
			jwtToken = authorizationHeader.substring(7);
			username = jwtUtils.extractUsername(jwtToken);
		}
		if( username != null && 
				SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails currentUser = userDetailsService.loadUserByUsername(username);
			Boolean tokenValidation = jwtUtils.validateToken(jwtToken, currentUser);
			if(tokenValidation) {
				UsernamePasswordAuthenticationToken uPAT = new UsernamePasswordAuthenticationToken(currentUser, tokenValidation, currentUser.getAuthorities());
				uPAT.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(uPAT);
			}
			
		}
		filterChain.doFilter(request, response);
	}

}