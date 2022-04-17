package br.edu.ifpb.dac.groupd.dto.post;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class BraceletPostDto {

	@NotNull
	@NotEmpty
	@NotBlank
	@Size(max=50)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
