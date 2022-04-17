package br.edu.ifpb.dac.groupd.exception;

public class BraceletNotRegisteredException extends Exception {
	public BraceletNotRegisteredException() {
		super(String.format("A pulseira não foi cadastrada"));
	}
}
