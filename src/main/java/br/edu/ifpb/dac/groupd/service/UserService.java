package br.edu.ifpb.dac.groupd.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.dto.post.UserPostDto;
import br.edu.ifpb.dac.groupd.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.model.User;
import br.edu.ifpb.dac.groupd.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	public User create(UserPostDto userPostDto) {
		User user = mapper.map(userPostDto, User.class);
		
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
	
	public User update(Long id, UserPostDto userPostDto) throws UserNotFoundException {
		
		if(!userRepo.existsById(id))
			throw new UserNotFoundException(id);
		
		User updated = mapper.map(userPostDto, User.class);
		updated.setId(id);
		
		return userRepo.save(updated);
	}
	public void deleteById(Long id) throws UserNotFoundException{
		if(!userRepo.existsById(id))
			throw new UserNotFoundException(id);
		
		userRepo.deleteById(id);
	}
	
}
