package br.edu.ifpb.dac.groupd.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class AuthenticationFailedException extends AbstractException {
	
	private static final long serialVersionUID = -57495805479409742L;
	
	private static final HttpStatus status = HttpStatus.UNAUTHORIZED;

	public AuthenticationFailedException(String msg, Throwable cause) {
		super(msg, cause);
	}
	public AuthenticationFailedException(String msg) {
		super(String.format("A autenticação do username %s falhou.", msg));
	}
	@Override
	public HttpStatus getStatus() {
		return status;
	}

}