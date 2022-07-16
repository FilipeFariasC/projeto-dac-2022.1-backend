package br.edu.ifpb.dac.groupd.business.service.converter;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.model.entities.User;
import br.edu.ifpb.dac.groupd.presentation.dto.UserRequest;
import br.edu.ifpb.dac.groupd.presentation.dto.UserResponse;

@Service
public class UserConverterService {
	
	@Autowired
	private ModelMapper mapper;
	
	
	public User requestToUser(UserRequest dto ) {
		User user = mapper.map(dto, User.class); 
		
		return user;
	}
	public UserResponse userToResponse(User user) {
		UserResponse response = mapper.map(user, UserResponse.class); 
		
		return response;
	}
	
	public List<UserResponse> usersToResponses(List<User> users) {
		return users.stream()
				.map(this::userToResponse)
				.toList();
	}
	

}
