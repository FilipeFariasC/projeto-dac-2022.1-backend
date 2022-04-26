package br.edu.ifpb.dac.groupd.exception;

public class FenceNotRegisteredException extends Exception {
	public FenceNotRegisteredException() {
		super("A cerca não foi cadastrada");
	}
}
