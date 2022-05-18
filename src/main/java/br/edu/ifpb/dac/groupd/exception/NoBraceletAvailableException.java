package br.edu.ifpb.dac.groupd.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class NoBraceletAvailableException extends Exception {
	@Autowired
	private MessageSource messageSource;
	public NoBraceletAvailableException() {
		// TODO Auto-generated constructor stub
	}
	public NoBraceletAvailableException(Long id) {
		super(String.format("A pulseira de id %d não possui pulseiras disponíveis para monitoramento.", id));
	}
}
