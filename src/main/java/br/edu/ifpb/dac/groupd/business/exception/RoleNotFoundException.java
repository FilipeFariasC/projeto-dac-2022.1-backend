package br.edu.ifpb.dac.groupd.business.exception;

import org.springframework.http.HttpStatus;

public class RoleNotFoundException extends AbstractException {

	private static final long serialVersionUID = -5001284292218799994L;
	
	private static final HttpStatus status = HttpStatus.NOT_FOUND;
	
	
	public RoleNotFoundException() { }
	
	public RoleNotFoundException(String message) {
		super(String.format("Não foi encontrada a role com nome %s.", message));
	}



	@Override
	public HttpStatus getStatus() {
		return status;
	}

}
