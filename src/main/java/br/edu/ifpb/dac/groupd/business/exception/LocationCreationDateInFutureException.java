package br.edu.ifpb.dac.groupd.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class LocationCreationDateInFutureException extends AbstractException {
	
	private static final long serialVersionUID = 4515323381330680041L;
	private static final HttpStatus status = HttpStatus.BAD_REQUEST;

	public LocationCreationDateInFutureException(String criacao, String agora) {
		super(String.format(
		"""
		A data de criação é inválida, o tempo representado está no futuro.
		atual: %s
		passada: %s		
		""", agora, criacao));
		
	}

	@Override
	public HttpStatus getStatus() {
		return status;
	}
}
