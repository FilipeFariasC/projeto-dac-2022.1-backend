package br.edu.ifpb.dac.groupd.model.repository.dataloader;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import br.edu.ifpb.dac.groupd.model.entities.Role;
import br.edu.ifpb.dac.groupd.model.repository.RoleRepository;

@Component
public class RoleDataLoader implements ApplicationRunner {
	
    private RoleRepository roleRepository;

    @Autowired
    public RoleDataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void run(ApplicationArguments args) {
    	List<String> roles = Arrays.asList("ADMIN", "USER");
    	
    	roles.forEach(role -> {
    		if(roleRepository.findByAuthority(role).isEmpty()) {
        		roleRepository.save(new Role(role));
        	}
    	});
    }
}