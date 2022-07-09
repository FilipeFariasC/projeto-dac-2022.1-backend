package br.edu.ifpb.dac.groupd.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class FenceNotRegisteredException extends AbstractException {
	
	private static final long serialVersionUID = 1L;
	private static final HttpStatus status = HttpStatus.NOT_FOUND;

	public FenceNotRegisteredException() {
		super("A cerca não foi cadastrada");
	}

	@Override
	public HttpStatus getStatus() {
		return status;
	}
}
