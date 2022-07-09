package br.edu.ifpb.dac.groupd.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class FenceEmptyException extends AbstractException {
	
	private static final long serialVersionUID = -1055410811240259200L;
	private static final HttpStatus status = HttpStatus.NOT_FOUND;

	public FenceEmptyException(Long id) {
		super(String.format("A cerca com identificador %d não possui pulseiras cadastradas", id));
	}

	@Override
	public HttpStatus getStatus() {
		return status;
	}
}
