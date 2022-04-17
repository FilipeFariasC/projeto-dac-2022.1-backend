package br.edu.ifpb.dac.groupd.exception;

public class LocationNotFoundException extends Exception {
	public LocationNotFoundException(Long id) {
		super(String.format("A localização com identificador %s não foi encontrada", id));
	}
}
