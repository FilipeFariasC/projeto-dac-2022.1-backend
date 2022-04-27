package br.edu.ifpb.dac.groupd.exception;

public class AlarmNotFoundException extends Exception {
	
	private static final long serialVersionUID = -8934624530056633980L;

	public AlarmNotFoundException(Long id) {
		super(String.format("O alarme com identificador %d não foi encontrado.", id));
	}
	
}