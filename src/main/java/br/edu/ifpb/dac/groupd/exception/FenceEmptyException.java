package br.edu.ifpb.dac.groupd.exception;

public class FenceEmptyException extends Exception {
	private static final long serialVersionUID = -8309686378836761276L;

	public FenceEmptyException(Long id) {
		super(String.format("A cerca com identificador %d não possui pulseiras cadastradas", id));
	}
}
