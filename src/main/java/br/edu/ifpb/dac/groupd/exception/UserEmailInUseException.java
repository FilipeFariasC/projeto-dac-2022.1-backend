package br.edu.ifpb.dac.groupd.exception;

public class UserEmailInUseException extends Exception {
	private static final long serialVersionUID = -171137723759054572L;

	public UserEmailInUseException(String email) {
		super(String.format("O email %s já está cadastrado", email));
	}
}
