package br.edu.ifpb.dac.groupd.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends AbstractException {
	
	private static final long serialVersionUID = -7599471152347606283L;
	private static final HttpStatus status = HttpStatus.NOT_FOUND;
	
	public UserNotFoundException(Long id) {
		super(String.format("O usuário com o identificador %d não foi encontrado.", id));
	}
	public UserNotFoundException(String email) {
		super(String.format("O usuário com o email %s não foi encontrado.", email));
	}
	@Override
	public HttpStatus getStatus() {
		return status;
	}
}
