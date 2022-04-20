package br.edu.ifpb.dac.groupd.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
@Entity
@Table(name="user")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6820520515993917458L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="user_id")
	private Long id;
	
	@NotNull
	@NotEmpty
	@Size(min=3, max=50)
	@Column(name="name", nullable=false)
	private String name;
	
	@NotNull
	@NotEmpty
	@Email
	@Column(name="email", unique=true, nullable=false)
	@Size(max=255)
	private String email;
	
	@NotNull
	@Column(name="password", nullable=false)
	@Size(min=8, max=30)
	@Pattern(regexp="^[^\\s]+$", message="{0} nao pode conter espaços")
	private String password;
	
	
	@OneToMany
	@JoinTable(name="user_bracelet",
			joinColumns = @JoinColumn(name="user_id"),
			inverseJoinColumns = @JoinColumn(name="bracelet_id"))
	@Valid
	private Set<@Valid Bracelet> bracelets = new HashSet<>();
	
	@OneToMany
	@JoinTable(name="user_fence",
		joinColumns = @JoinColumn(name="user_id"),
		inverseJoinColumns = @JoinColumn(name="fence_id"))
	@Valid
	private Set<@Valid Fence> fences = new HashSet<>();

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
		this.name = name.trim();
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

	public Set<Bracelet> getBracelets() {
		return bracelets;
	}

	public void setBracelets(Set<Bracelet> bracelets) {
		this.bracelets = bracelets;
	}

	public Set<Fence> getFences() {
		return fences;
	}

	public void setFences(Set<Fence> fences) {
		this.fences = fences;
	}
	
	
}
