package br.edu.ifpb.dac.groupd.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class AlarmNotFoundException extends AbstractException {
	
	private static final long serialVersionUID = -9089772634690986898L;
	
	private static final HttpStatus status = HttpStatus.NOT_FOUND;

	public AlarmNotFoundException(Long id) {
		super(String.format("O alarme com identificador %d não foi encontrado.", id));
	}
	public AlarmNotFoundException(String message) {
		super(message);
	}
	
	@Override
	public HttpStatus getStatus() {
		return status;
	}
}
