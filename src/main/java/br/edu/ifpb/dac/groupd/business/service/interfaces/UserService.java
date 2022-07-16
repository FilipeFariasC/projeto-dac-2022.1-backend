package br.edu.ifpb.dac.groupd.business.service.interfaces;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import br.edu.ifpb.dac.groupd.business.exception.UserEmailInUseException;
import br.edu.ifpb.dac.groupd.business.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.model.entities.User;

public interface UserService extends UserDetailsService {
	
	User save(User user) throws UserEmailInUseException;
	User update(User user) throws UserNotFoundException, UserEmailInUseException;
	void delete(Long id) throws UserNotFoundException;
	void delete(User user) throws UserNotFoundException;
	User findById(Long id) throws UserNotFoundException;
	User findByEmail(String email) throws UserNotFoundException;
	User findByUsername(String username) throws UserNotFoundException;
	List<User> findAll();
	List<User> find(User filter);
	
}
