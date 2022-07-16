package br.edu.ifpb.dac.groupd.business.service.interfaces;

import javax.servlet.http.HttpServletRequest;

import br.edu.ifpb.dac.groupd.model.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

public interface TokenService {
	
	String generate(User user);
	
	Claims getClaims(String token) throws ExpiredJwtException;
	
	boolean isValid(String token);
	
	String getUsername(String token);
	
	Long getUserId(String token);
	
	String get(HttpServletRequest request);
	
}
