package br.edu.ifpb.dac.groupd.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class NoBraceletAvailableException extends AbstractException {
	
	private static final long serialVersionUID = -4352892525683864793L;
	private static final HttpStatus status = HttpStatus.CONFLICT;
	
	public NoBraceletAvailableException() {
	}
	public NoBraceletAvailableException(Long id) {
		super(String.format("A pulseira de id %d não possui pulseiras disponíveis para monitoramento.", id));
	}
	@Override
	public HttpStatus getStatus() {
		return status;
	}
}
