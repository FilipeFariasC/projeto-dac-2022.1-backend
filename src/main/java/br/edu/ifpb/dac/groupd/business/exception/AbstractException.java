package br.edu.ifpb.dac.groupd.business.exception;

import org.springframework.http.HttpStatus;

public abstract class AbstractException extends Exception{
	private static final long serialVersionUID = -8934624530056633980L;
	
	protected AbstractException() {}
	
	protected AbstractException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}


	protected AbstractException(String message, Throwable cause) {
		super(message, cause);
	}


	protected AbstractException(String message) {
		super(message);
	}


	protected AbstractException(Throwable cause) {
		super(cause);
	}


	public abstract HttpStatus getStatus();
}
