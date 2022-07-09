package br.edu.ifpb.dac.groupd.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.business.exception.AuthenticationFailedException;
import br.edu.ifpb.dac.groupd.business.security.JwtUtils;
import br.edu.ifpb.dac.groupd.business.security.UserDetailsService;
import br.edu.ifpb.dac.groupd.presentation.dto.security.AuthenticationResponse;
import br.edu.ifpb.dac.groupd.presentation.dto.security.UserDetailsRequest;



@Service
public class LoginService {
	@Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;
    
    public AuthenticationResponse login(UserDetailsRequest userDetails) throws AuthenticationFailedException {
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