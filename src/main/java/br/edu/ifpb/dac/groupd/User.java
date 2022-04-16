package br.edu.ifpb.dac.groupd;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name="t_user")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6820520515993917458L;
	
	
	@Id
	@Column(name="user_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@NotEmpty
	@NotBlank
<<<<<<< HEAD:src/main/java/br/edu/ifpb/dac/groupd/User.java
=======
	@Size(min=3, max=50)
	@Column(name="name")
	private String name;
	
	@NotNull
	@NotEmpty
	@NotBlank
>>>>>>> f98d2847d886b541601741572a18361f26e4638d:src/main/java/br/edu/ifpb/dac/groupd/model/User.java
	@Email
	private String email;
	
	@NotNull
	@NotEmpty
	@NotBlank
<<<<<<< HEAD:src/main/java/br/edu/ifpb/dac/groupd/User.java
=======
	@Column(name="password", nullable=false)
	@Size(min=8, max=30)
	@Pattern(regexp="^[^\\s]+$")
>>>>>>> f98d2847d886b541601741572a18361f26e4638d:src/main/java/br/edu/ifpb/dac/groupd/model/User.java
	private String password;
	
	

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

<<<<<<< HEAD:src/main/java/br/edu/ifpb/dac/groupd/User.java
	public String getPassword() {
		return password;
=======
	public void setName(String name) {
		this.name = name.trim();
>>>>>>> f98d2847d886b541601741572a18361f26e4638d:src/main/java/br/edu/ifpb/dac/groupd/model/User.java
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
