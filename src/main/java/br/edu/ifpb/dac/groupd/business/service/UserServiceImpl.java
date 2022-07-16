package br.edu.ifpb.dac.groupd.business.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.business.exception.UserEmailInUseException;
import br.edu.ifpb.dac.groupd.business.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.business.service.interfaces.PasswordEncoderService;
import br.edu.ifpb.dac.groupd.business.service.interfaces.UserService;
import br.edu.ifpb.dac.groupd.model.entities.User;
import br.edu.ifpb.dac.groupd.model.repository.UserRepository;
import br.edu.ifpb.dac.groupd.presentation.dto.UserRequest;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private PasswordEncoderService passEncoder;
	
	@Autowired
	private RoleService roleService;
	
	// User
	public User create(UserRequest userPostDto) throws UserEmailInUseException {
		User user = mapper.map(userPostDto, User.class);
		
		boolean register = userRepo.existsByEmail(userPostDto.getEmail());
		
		if(register)
			throw new UserEmailInUseException(userPostDto.getEmail());
		
		user.setPassword(passEncoder.encode(userPostDto.getPassword()));
		user.addAuthority(roleService.findDefault());
		
		return userRepo.save(user);
	}
	public List<User> getAll(){
		return userRepo.findAll();
	}
	public User findById(Long id) throws UserNotFoundException{
		Optional<User> user = userRepo.findById(id);
		
		if(user.isEmpty())
			throw new UserNotFoundException(id);
		
		return user.get();
	}
	
	public User update(String username, UserRequest userPostDto) throws UserNotFoundException, UserEmailInUseException {
		Optional<User> user = userRepo.findByEmail(username);
		if(user.isEmpty())
			throw new UserNotFoundException(username);
		
		if(!username.equals(userPostDto.getEmail())){
			boolean register = userRepo.existsByEmail(userPostDto.getEmail());
			
			if(register)
				throw new UserEmailInUseException(userPostDto.getEmail());
		}
		User original = user.get();
		User updated = mapper.map(userPostDto, User.class);
		updated.setId(user.get().getId());
		updated.setPassword(passEncoder.encode(userPostDto.getPassword()));
		
		updated.setFences(original.getFences());
		updated.setBracelets(original.getBracelets());
		updated.setRoles(original.getAuthorities());
		
		return userRepo.save(updated);
	}
	
	public User updateUserName(String username, String name) throws UserNotFoundException {
		Optional<User> user = userRepo.findByEmail(username);
		if(user.isEmpty())
			throw new UserNotFoundException(username);
		
		User updated = user.get();
		updated.setName(name);
		
		return userRepo.save(updated);
	}
	
	public void deleteByUsername(String username) throws UserNotFoundException{
		Optional<User> register = userRepo.findByEmail(username);
		
		if(register.isEmpty())
			throw new UserNotFoundException(username);
		
		userRepo.deleteById(register.get().getId());
	}
	
	public User findByEmail(String username) throws UserNotFoundException {
		
		Optional<User> user = userRepo.findByEmail(username);
		
		if(user.isEmpty())
			throw new UserNotFoundException(username);
		
		return user.get();
	}

	public User save(User user) throws UserEmailInUseException {
		
		if(user.getId() != null) {
			throw new IllegalStateException("Usuário já está no banco, tente atualizá-lo.");
		}
		
		boolean register = userRepo.existsByEmail(user.getEmail());
		
		if(register)
			throw new UserEmailInUseException(user.getEmail());
		
		passEncoder.encryptPassword(user);
		user.addAuthority(roleService.findDefault());
		
		return userRepo.save(user);
	}

	public User update(User user) throws UserNotFoundException, UserEmailInUseException {
		Optional<User> userRegister = userRepo.findById(user.getId());
		if(userRegister.isEmpty())
			throw new UserNotFoundException(user.getUsername());
		
		User registered = userRegister.get();
		
		Optional<User> email = userRepo.findByEmail(user.getEmail());
		
		if(email.isPresent() && !registered.getId().equals(email.get().getId())) {
			throw new UserEmailInUseException(user.getEmail());
		}
		
		passEncoder.encryptPassword(user);
		
		user.setFences(registered.getFences());
		user.setBracelets(registered.getBracelets());
		user.setRoles(registered.getAuthorities());
		
		return userRepo.save(user);
	}

	public void delete(Long id) throws UserNotFoundException {
		boolean exists = userRepo.existsById(id);
		
		if(!exists)
			throw new UserNotFoundException(id);
		
		userRepo.deleteById(id);
	}

	public void delete(User user) throws UserNotFoundException {
		Optional<User> register = userRepo.findByEmail(user.getUsername());
		
		if(register.isEmpty())
			throw new UserNotFoundException(user.getUsername());
		
		userRepo.deleteById(register.get().getId());
		
	}

	public User findByUsername(String username) throws UserNotFoundException {
		return findByEmail(username);
	}

	public List<User> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<User> find(User filter) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			return findByUsername(username);
		} catch (UserNotFoundException e) {
			return null;
		}
	}
	
}
