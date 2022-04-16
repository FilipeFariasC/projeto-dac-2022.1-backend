package br.edu.ifpb.dac.groupd.dto.post;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserPostDto {

	@NotNull
	@NotEmpty
	@NotBlank
	private String name;
	
	@NotNull
	@NotEmpty
	@NotBlank
	@Email
	private String email;
	
	@NotNull
	@NotEmpty
	@NotBlank
	private String password;

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
