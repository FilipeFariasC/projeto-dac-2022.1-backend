package br.edu.ifpb.dac.groupd.business.exception;

public class BraceletNotRegisteredException extends Exception {
	private static final long serialVersionUID = 6636770537977573104L;

	public BraceletNotRegisteredException() {
		super("A pulseira não foi cadastrada");
	}
}
