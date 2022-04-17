package br.edu.ifpb.dac.groupd.exception;

public class BraceletNotFoundException extends Exception {
	private static final long serialVersionUID = -6472366214235131010L;

	public BraceletNotFoundException(Long id) {
		super(String.format("A pulseira com identificador %d não foi encontrada.", id));
	}
	
	public BraceletNotFoundException(String name) {
		super(String.format("A pulseira com o nome %s não foi encontrada.", name));
	}
}
