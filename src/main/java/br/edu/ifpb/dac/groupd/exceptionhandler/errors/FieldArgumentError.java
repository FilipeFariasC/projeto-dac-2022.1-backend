package br.edu.ifpb.dac.groupd.exceptionhandler.errors;

public class FieldArgumentError extends FieldValueError{
	
	private String value;
	
	protected FieldArgumentError(String fieldName, String messageUser, String messageDeveloper) {
		super(fieldName, messageUser, messageDeveloper);
	}
	
	public FieldArgumentError(String fieldName, String value, String messageUser, String messageDeveloper) {
		super(fieldName, messageUser, messageDeveloper);
		setValue(value);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
