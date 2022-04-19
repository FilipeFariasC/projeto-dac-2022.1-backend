package br.edu.ifpb.dac.groupd.exception;

public class UserEmailInUseException extends Exception {
	public UserEmailInUseException(String email) {
		super(String.format("O email %s já está cadastrado", email));
	}
}
