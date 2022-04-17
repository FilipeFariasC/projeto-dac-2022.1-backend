package br.edu.ifpb.dac.groupd.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="bracelet")
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
	@Size(max=50)
	@Column(name="name")
	private String name;
	
	//Lista com tipo String provisorio, entquanto as entidades não são criadas
	@ManyToMany
	@JoinTable(name = "bracelet_fence",
		joinColumns = @JoinColumn(name = "bracelet_id"),
		inverseJoinColumns = @JoinColumn(name = "fence_id"))
	private Set<@Valid Fence> fences;
	
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "bracelet_location",
		joinColumns = @JoinColumn(name = "bracelet_id"),
		inverseJoinColumns = @JoinColumn(name = "location_id"))
	private Set<@Valid Location> locations;

	
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

	public Set<Fence> getFences() {
		return fences;
	}

	public void setFences(Set<Fence> fences) {
		this.fences = fences;
	}

	public Set<Location> getLocations() {
		return locations;
	}

	public void setLocations(Set<Location> locations) {
		this.locations = locations;
	}
	
	

}
