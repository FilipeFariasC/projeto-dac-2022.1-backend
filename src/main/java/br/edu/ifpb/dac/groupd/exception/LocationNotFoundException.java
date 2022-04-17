package br.edu.ifpb.dac.groupd.exception;

public class LocationNotFoundException extends Exception {
	private static final long serialVersionUID = -893659169624679362L;

	public LocationNotFoundException(Long id) {
		super(String.format("A localização com identificador %s não foi encontrada", id));
	}
}
