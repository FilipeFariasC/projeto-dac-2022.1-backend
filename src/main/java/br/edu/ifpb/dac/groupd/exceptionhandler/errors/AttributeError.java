package br.edu.ifpb.dac.groupd.exceptionhandler.errors;

public class AttributeError {
	private String fieldName;
	private String messageUser;
	private String messageDeveloper;
	
	
	public AttributeError(String fieldName, String messageUser, String messageDeveloper) {
		super();
		this.fieldName = fieldName;
		this.messageUser = messageUser;
		this.messageDeveloper = messageDeveloper;
	}
	
	public AttributeError() {
		super();
	}


	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getMessageUser() {
		return messageUser;
	}
	public void setMessageUser(String messageUser) {
		this.messageUser = messageUser;
	}
	public String getMessageDeveloper() {
		return messageDeveloper;
	}
	public void setMessageDeveloper(String messageDeveloper) {
		this.messageDeveloper = messageDeveloper;
	}
	
	
}
