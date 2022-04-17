package br.edu.ifpb.dac.groupd.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.dto.post.BraceletPostDto;
import br.edu.ifpb.dac.groupd.dto.post.UserPostDto;
import br.edu.ifpb.dac.groupd.exception.BraceletNotFoundException;
import br.edu.ifpb.dac.groupd.exception.BraceletNotRegisteredException;
import br.edu.ifpb.dac.groupd.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.model.Bracelet;
import br.edu.ifpb.dac.groupd.model.User;
import br.edu.ifpb.dac.groupd.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ModelMapper mapper;
	// User
	@Autowired
	private BraceletService braceletService;
	
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
	// User Bracelet
	public Bracelet createBracelet(Long userId, BraceletPostDto dto) throws UserNotFoundException {
		Optional<User> register = userRepo.findById(userId);
		
		if (register.isEmpty())
			throw new UserNotFoundException(userId);
		
		User user = register.get();
		
		Bracelet bracelet = braceletService.save(dto);

		user.getBracelets().add(bracelet);
		
		return bracelet;
	}
	public List<Bracelet> getAllBracelets(Long userId) throws UserNotFoundException {
		Optional<User> register = userRepo.findById(userId);
		
		if (register.isEmpty())
			throw new UserNotFoundException(userId);
		
		User user = register.get();
		
		return user.getBracelets().stream().toList();
	}
	public Bracelet findByBraceletId(Long userId, Long braceletId) throws UserNotFoundException, BraceletNotRegisteredException {
		Optional<User> register = userRepo.findById(userId);
		
		if (register.isEmpty())
			throw new UserNotFoundException(userId);
		
		User user = register.get();
		
		for(Bracelet bracelet : user.getBracelets()) {
			if(bracelet.getIdBracelet().equals(braceletId)) {
				return bracelet;
			}
		}
		throw new BraceletNotRegisteredException();
	}
	
	public Bracelet updateBracelet(Long userId, Long braceletId, BraceletPostDto dto) throws UserNotFoundException, BraceletNotFoundException, BraceletNotRegisteredException {
		Optional<User> register = userRepo.findById(userId);
		
		if (register.isEmpty())
			throw new UserNotFoundException(userId);
		
		User user = register.get();
		
		boolean entrou = false;
		for(Bracelet braceletRegister : user.getBracelets()) {
			entrou = braceletRegister.getIdBracelet().equals(braceletId);
		}
		if(entrou == false) {
			throw new BraceletNotRegisteredException();
		}
		
		Bracelet bracelet = braceletService.update(braceletId, dto);
		
		return bracelet;
	}
}
