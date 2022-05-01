package br.edu.ifpb.dac.groupd.exception;

public class BraceletNotInFenceException extends Exception {
	private static final long serialVersionUID = 8269545488371196299L;

	public BraceletNotInFenceException(Long fenceId, Long braceletId) {
		super(String.format("A cerca de identificador %d não possui a pulseira de identificador %d cadastrada.", fenceId, braceletId));
	}
}
