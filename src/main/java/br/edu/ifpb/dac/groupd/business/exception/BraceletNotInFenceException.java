package br.edu.ifpb.dac.groupd.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class BraceletNotInFenceException extends AbstractException {
	
	private static final long serialVersionUID = 6108650905214007758L;
	
	private static final HttpStatus status = HttpStatus.NOT_FOUND;

	public BraceletNotInFenceException(Long fenceId, Long braceletId) {
		super(String.format("A cerca de identificador %d não possui a pulseira de identificador %d cadastrada.", fenceId, braceletId));
	}

	@Override
	public HttpStatus getStatus() {
		return status;
	}
}
