package br.edu.ifpb.dac.groupd.business.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.business.exception.UserEmailInUseException;
import br.edu.ifpb.dac.groupd.business.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.model.entities.User;
import br.edu.ifpb.dac.groupd.model.repository.RoleRepository;
import br.edu.ifpb.dac.groupd.model.repository.UserRepository;
import br.edu.ifpb.dac.groupd.presentation.dto.UserRequest;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private BCryptPasswordEncoder passEncoder;
	
	@Autowired
	private RoleRepository roleRepo;
	
	// User
	public User create(UserRequest userPostDto) throws UserEmailInUseException {
		User user = mapper.map(userPostDto, User.class);
		
		boolean register = userRepo.existsByEmail(userPostDto.getEmail());
		
		if(register)
			throw new UserEmailInUseException(userPostDto.getEmail());
		
		user.setPassword(passEncoder.encode(userPostDto.getPassword()));
		user.addAuthority(roleRepo.findByAuthority("USER").get());
		
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
	
}
