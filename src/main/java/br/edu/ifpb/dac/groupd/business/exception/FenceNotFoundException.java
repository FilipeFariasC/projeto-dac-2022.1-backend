package br.edu.ifpb.dac.groupd.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class FenceNotFoundException extends AbstractException {
	
	private static final long serialVersionUID = -5204216697805538236L;
	private static final HttpStatus status = HttpStatus.NOT_FOUND;

	public FenceNotFoundException(Long id) {
		super(String.format("A pulseira com identificador %d não foi encontrada.", id));
	}

	@Override
	public HttpStatus getStatus() {
		return status;
	}
}
