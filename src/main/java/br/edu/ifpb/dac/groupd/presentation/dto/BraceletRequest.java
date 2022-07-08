package br.edu.ifpb.dac.groupd.presentation.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class BraceletRequest {

	@NotNull
	@NotEmpty
	@NotBlank
	@Size(min=1, max=50)
	private String name;
	
	// Default
	public BraceletRequest() {}

	public BraceletRequest(@NotNull @NotEmpty @NotBlank @Size(max = 50) String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "BraceletPostDto [name=" + name + "]";
	}
	
	
	
}
