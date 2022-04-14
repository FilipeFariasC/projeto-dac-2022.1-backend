package br.edu.ifpb.dac.groupd.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends Exception {

	private static final long serialVersionUID = -7810725053673273144L;
	
	public UserNotFoundException(Long id) {
		super(String.format("O usuário com o identificador %d não foi encontrado", id));
	}

}
