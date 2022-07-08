package br.edu.ifpb.dac.groupd.presentation.dto.security;

public class AuthenticationResponse {
	private String response;

	public AuthenticationResponse() {}
	
	public AuthenticationResponse(String response) {
		super();
		this.response = response;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
	
}
