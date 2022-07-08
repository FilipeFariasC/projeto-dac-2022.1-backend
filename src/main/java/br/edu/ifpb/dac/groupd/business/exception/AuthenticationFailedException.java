package br.edu.ifpb.dac.groupd.business.exception;

import org.springframework.security.core.AuthenticationException;

public class AuthenticationFailedException extends AuthenticationException {

	private static final long serialVersionUID = -57495805479409742L;

	public AuthenticationFailedException(String msg, Throwable cause) {
		super(msg, cause);
	}
	public AuthenticationFailedException(String msg) {
		super(String.format("A autenticação do username %s falhou.", msg));
	}

}