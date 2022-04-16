package br.edu.ifpb.dac.groupd.exception;

public class BraceletNotFoundException extends Exception {
	public BraceletNotFoundException(Long id) {
		super(String.format("A pulseira com identificador %d não foi encontrada.", id));
	}
	
	public BraceletNotFoundException(String name) {
		super(String.format("A pulseira com o nome %s não foi encontrada.", name));
	}
}
