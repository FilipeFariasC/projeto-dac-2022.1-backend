package br.edu.ifpb.dac.groupd.presentation.dto.security;

import br.edu.ifpb.dac.groupd.presentation.dto.UserResponse;

public class AuthenticationResponse {
	
	private String token;
	private UserResponse user;
	
	
	public AuthenticationResponse() {}

	public AuthenticationResponse(String token, UserResponse user) {
		super();
		this.token = token;
		this.user = user;
	}
	

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserResponse getUser() {
		return user;
	}

	public void setUser(UserResponse user) {
		this.user = user;
	}
	
}
