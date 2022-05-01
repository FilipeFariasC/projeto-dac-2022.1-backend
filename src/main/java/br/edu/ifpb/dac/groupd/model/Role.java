package br.edu.ifpb.dac.groupd.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name="roles")
public class Role implements GrantedAuthority, Serializable {

	private static final long serialVersionUID = -1248257294220217113L;

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="role_id")
	private Long id;
	
	@NotNull
	@NotBlank
	@NotEmpty
	@Column(name="authority", unique=true)
	private String authority;
	
	public Role() {
		super();
	}


	public Role(@NotNull @NotBlank @NotEmpty String authority) {
		super();
		this.authority = authority;
	}


	@Override
	public String getAuthority() {
		return authority;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

}
