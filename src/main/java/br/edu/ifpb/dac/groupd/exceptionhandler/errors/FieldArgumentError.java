package br.edu.ifpb.dac.groupd.exceptionhandler.errors;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"fieldName", "value","path","statusCode", "messageUser", "messageDeveloper" })
public class FieldArgumentError extends FieldValueError{
	
	private String value;
	
	protected FieldArgumentError(String fieldName, String messageUser, String messageDeveloper, String path, Integer statusCode) {
		super(fieldName, messageUser, messageDeveloper, path, statusCode);
	}
	
	public FieldArgumentError(String fieldName, String value, String messageUser, String messageDeveloper, String path, Integer statusCode) {
		super(fieldName, messageUser, messageDeveloper, path, statusCode);
		setValue(value);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
