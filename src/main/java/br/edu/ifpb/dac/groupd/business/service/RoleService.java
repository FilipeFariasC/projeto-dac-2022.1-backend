package br.edu.ifpb.dac.groupd.business.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.business.exception.RoleNotFoundException;
import br.edu.ifpb.dac.groupd.model.entities.Role;
import br.edu.ifpb.dac.groupd.model.repository.RoleRepository;

@Service
public class RoleService {
	
	public enum AVAILABLE_ROLES {
		ADMIN("Admin"),
		USER("User");
		private String role;
		
		AVAILABLE_ROLES(String role) {
			this.role = role;
		}
		public String getRole() {
			return role;
		}
	}
	
	@Autowired
	private RoleRepository roleRepo;
	
	public Role findByName(String name) throws RoleNotFoundException {
		if(name == null) {
			throw new IllegalStateException("Name cannot be null");
		}
		
		Optional<Role> register = roleRepo.findByName(name);
		
		if(register.isEmpty()) {
			throw new RoleNotFoundException(name);
		}
		return register.get();
	}
	
	public Role findDefault() {
		try {
			return findByName(AVAILABLE_ROLES.USER.toString());
		} catch (RoleNotFoundException e) {
			return null;
		}
	}
}
