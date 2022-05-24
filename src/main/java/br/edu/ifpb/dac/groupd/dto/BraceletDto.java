package br.edu.ifpb.dac.groupd.dto;

import java.util.Objects;

public class BraceletDto {
	
	private Long id;
	private String name;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BraceletDto other = (BraceletDto) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}
	
	

}
