package br.edu.ifpb.dac.groupd.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "A pulseira não foi cadastrada")
public class BraceletNotRegisteredException extends AbstractException {
	
	private static final long serialVersionUID = -458543862119502315L;
	private static final HttpStatus status = HttpStatus.NOT_FOUND;
	
	
	public BraceletNotRegisteredException() {
		super("A pulseira não foi cadastrada");
	}


	@Override
	public HttpStatus getStatus() {
		return status;
	}
	
}
