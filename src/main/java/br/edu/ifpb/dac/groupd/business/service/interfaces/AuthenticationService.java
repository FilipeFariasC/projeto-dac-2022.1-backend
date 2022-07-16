package br.edu.ifpb.dac.groupd.business.service.interfaces;

import br.edu.ifpb.dac.groupd.business.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.model.entities.User;

public interface AuthenticationService {
	
	String login(String email, String password) throws UserNotFoundException;
	User getLoggedUser();
}
