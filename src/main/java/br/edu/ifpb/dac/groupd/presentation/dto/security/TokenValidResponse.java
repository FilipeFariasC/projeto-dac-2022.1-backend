package br.edu.ifpb.dac.groupd.presentation.dto.security;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TokenValidResponse {
	
	private String token;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime expirationTime;
	private boolean isValid;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public LocalDateTime getExpirationTime() {
		return expirationTime;
	}
	public void setExpirationTime(LocalDateTime expirationTime) {
		this.expirationTime = expirationTime;
	}
	public boolean isValid() {
		return isValid;
	}
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	
	
}
