package br.edu.ifpb.dac.groupd.business.exception;

public class FenceNotRegisteredException extends Exception {
	private static final long serialVersionUID = 969968680683029834L;

	public FenceNotRegisteredException() {
		super("A cerca não foi cadastrada");
	}
}
