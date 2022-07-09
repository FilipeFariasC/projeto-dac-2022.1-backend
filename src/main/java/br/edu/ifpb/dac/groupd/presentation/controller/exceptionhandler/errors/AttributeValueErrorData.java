package br.edu.ifpb.dac.groupd.presentation.controller.exceptionhandler.errors;

public class AttributeValueErrorData extends AttributeErrorData {
	private String value;
	
	public AttributeValueErrorData() {} // construtor default
	
	public AttributeValueErrorData(String messageUser, String messageDeveloper, String fieldName, String value) {
		super(messageUser, messageDeveloper, fieldName);
		this.value = value;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
