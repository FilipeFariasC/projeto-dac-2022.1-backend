package br.edu.ifpb.dac.groupd.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="t-bracelet")
public class Bracelet implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="bracelet_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long idBracelet;
	
	@NotNull
	@NotEmpty
	@NotBlank
	private String name;
	
	//Lista com tipo String provisorio, entquanto as entidades não são criadas
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<String> fences;
	
	//Lista com tipo String provisorio, entquanto as entidades não são criadas
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<String> locations;

	
	public Long getIdBracelet() {
		return idBracelet;
	}

	public void setIdBracelet(Long idBracelet) {
		this.idBracelet = idBracelet;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getFences() {
		return fences;
	}

	public void setFences(List<String> fences) {
		this.fences = fences;
	}

	public List<String> getLocations() {
		return locations;
	}

	public void setLocations(List<String> locations) {
		this.locations = locations;
	}
	
	

}
