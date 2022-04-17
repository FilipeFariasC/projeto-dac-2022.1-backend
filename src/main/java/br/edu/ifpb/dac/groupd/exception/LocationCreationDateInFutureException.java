package br.edu.ifpb.dac.groupd.exception;

import java.time.LocalDateTime;

public class LocationCreationDateInFutureException extends Exception {
	public LocationCreationDateInFutureException(String criacao, String agora) {
		super(String.format(
		"""
		A data de criação é inválida, o tempo representado está no futuro.
		atual: %s
		passada: %s		
		""", agora, criacao));
		
	}
}
