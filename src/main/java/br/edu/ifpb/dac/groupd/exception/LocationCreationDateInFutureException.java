package br.edu.ifpb.dac.groupd.exception;

public class LocationCreationDateInFutureException extends Exception {
	private static final long serialVersionUID = 4486573287675907621L;

	public LocationCreationDateInFutureException(String criacao, String agora) {
		super(String.format(
		"""
		A data de criação é inválida, o tempo representado está no futuro.
		atual: %s
		passada: %s		
		""", agora, criacao));
		
	}
}
