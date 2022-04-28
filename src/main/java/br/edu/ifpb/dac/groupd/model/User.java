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

import org.springframework.validation.annotation.Validated;

import static br.edu.ifpb.dac.groupd.validation.validator.ModelValidator.*;

@Entity
@Table(name="t_user")
@Validated
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6820520515993917458L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="user_id")
	private Long id;
	
	@NotEmpty
	@Size(min=3, max=50)
	@Column(name="name", nullable=false)
	private String name;
	
	@NotEmpty
	@Email
	@Column(name="email", unique=true, nullable=false)
	@Size(max=255)
	private String email;
	
	@NotNull
	@Column(name="password", nullable=false)
	@Size(min=8, max=30)
	@Pattern(regexp="^[^\\s]+$", message="{password.Pattern}")
	private String password;
	
	
	@OneToMany
	@JoinTable(name="user_bracelet",
			joinColumns = @JoinColumn(name="user_id"),
			inverseJoinColumns = @JoinColumn(name="bracelet_id"))
	private Set<Bracelet> bracelets = new HashSet<>();
	
	@OneToMany
	@JoinTable(name="user_fence",
		joinColumns = @JoinColumn(name="user_id"),
		inverseJoinColumns = @JoinColumn(name="fence_id"))
	private Set<Fence> fences = new HashSet<>();

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
		if(name != null)
			this.name = name.trim();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if(email != null)
			this.email = email.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		if(password != null)
			this.password = password.trim();
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
	
	public boolean addFence(Fence fence) {
		if(validFence(fence)) {
			this.fences.add(fence);
			return true;
		}
		return false;
	}
	public boolean removeFence(Fence fence) {
		return this.fences.remove(fence);
	}
	
	public boolean addBracelet(Bracelet bracelet) {
		if(validBracelet(bracelet)) {
			this.bracelets.add(bracelet);
			return true;
		}
		return false;
	}
	public boolean removeBracelet(Bracelet bracelet) {
		return this.bracelets.remove(bracelet);
	}
	
}
