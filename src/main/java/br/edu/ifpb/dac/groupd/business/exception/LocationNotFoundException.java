package br.edu.ifpb.dac.groupd.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class LocationNotFoundException extends AbstractException {
	
	private static final long serialVersionUID = 5398283760968399660L;
	private static final HttpStatus status = HttpStatus.NOT_FOUND;

	public LocationNotFoundException(Long id) {
		super(String.format("A localização com identificador %s não foi encontrada", id));
	}

	@Override
	public HttpStatus getStatus() {
		return status;
	}
}
