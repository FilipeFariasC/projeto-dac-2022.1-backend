package br.edu.ifpb.dac.groupd.model;

import java.io.Serializable;
import java.util.HashSet;
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
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="t_bracelet")
public class Bracelet implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8321201080965139583L;

	@Id
	@Column(name="bracelet_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@NotEmpty
	@Size(min=1,max=50)
	@Column(name="name")
	private String name;
	
	@Valid
	@ManyToMany(fetch = FetchType.EAGER, mappedBy="bracelets")
	private Set<@Valid Fence> fences = new HashSet<>();
	
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "bracelet_location",
		joinColumns = @JoinColumn(name = "bracelet_id"),
		inverseJoinColumns = @JoinColumn(name = "location_id"))
	@Valid
	private Set<@Valid Location> locations = new HashSet<>();


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
		if(name == null)
			return ;
		this.name = name.trim();
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
	
	public void addLocation(Location location) {
		if(location != null)
			locations.add(location);
	}

}
