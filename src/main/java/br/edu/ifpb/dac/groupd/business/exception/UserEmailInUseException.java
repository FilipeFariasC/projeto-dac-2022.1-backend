package br.edu.ifpb.dac.groupd.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class UserEmailInUseException extends AbstractException {
	
	private static final long serialVersionUID = -3535661252742132431L;
	private static final HttpStatus status = HttpStatus.CONFLICT;

	public UserEmailInUseException(String email) {
		super(String.format("O email %s já está cadastrado", email));
	}

	@Override
	public HttpStatus getStatus() {
		return status;
	}
}
