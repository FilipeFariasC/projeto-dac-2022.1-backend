package br.edu.ifpb.dac.groupd.business.exception;

public class FenceNotFoundException extends Exception {
	
	private static final long serialVersionUID = -758004545279784158L;

	public FenceNotFoundException(Long id) {
		super(String.format("A pulseira com identificador %d não foi encontrada.", id));
	}
}
