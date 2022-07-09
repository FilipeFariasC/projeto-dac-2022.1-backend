package br.edu.ifpb.dac.groupd.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class BraceletNotFoundException extends AbstractException {
	
	private static final long serialVersionUID = -3086461171400701680L;
	
	private static final HttpStatus status = HttpStatus.NOT_FOUND;

	public BraceletNotFoundException(Long id) {
		super(String.format("A pulseira com identificador %d não foi encontrada.", id));
	}
	
	public BraceletNotFoundException(String name) {
		super(String.format("A pulseira com o nome %s não foi encontrada.", name));
	}

	@Override
	public HttpStatus getStatus() {
		return status;
	}
}
