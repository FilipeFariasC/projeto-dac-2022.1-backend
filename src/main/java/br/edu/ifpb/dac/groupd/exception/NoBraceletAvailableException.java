package br.edu.ifpb.dac.groupd.exception;

public class NoBraceletAvailableException extends Exception {
	private static final long serialVersionUID = -941436713186971505L;
	
	public NoBraceletAvailableException() {
		// TODO Auto-generated constructor stub
	}
	public NoBraceletAvailableException(Long id) {
		super(String.format("A pulseira de id %d não possui pulseiras disponíveis para monitoramento.", id));
	}
}
