package br.edu.ifpb.dac.groupd.exception;

public class BraceletNotInFenceException extends Exception {
	public BraceletNotInFenceException(Long fenceId, Long braceletId) {
		super(String.format("A cerca de identificador %d não possui a pulseira de identificador %d cadastrada.", fenceId, braceletId));
	}
}
