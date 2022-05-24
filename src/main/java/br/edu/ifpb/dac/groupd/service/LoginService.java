package br.edu.ifpb.dac.groupd.service;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.dto.post.security.AuthenticationResponse;
import br.edu.ifpb.dac.groupd.dto.post.security.UserDetailsDto;
import br.edu.ifpb.dac.groupd.exception.AuthenticationFailedException;
import br.edu.ifpb.dac.groupd.model.Role;
import br.edu.ifpb.dac.groupd.security.JwtUtils;
import br.edu.ifpb.dac.groupd.security.UserDetailsService;
import io.jsonwebtoken.lang.Collections;



@Service
public class LoginService {
	@Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;
    
    public AuthenticationResponse login(UserDetailsDto userDetails) throws AuthenticationException {
        String username = userDetails.getUsername();
        String password = userDetails.getPassword();
        
    	try {
        	authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        }catch (AuthenticationException exception) {
			throw new AuthenticationFailedException(username);
		}
        	
    	UserDetails register = userDetailsService.loadUserByUsername(username);
        
        String token = jwtUtils.generateToken(register);

        return new AuthenticationResponse(token);
        
    }
}