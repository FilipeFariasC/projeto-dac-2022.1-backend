package br.edu.ifpb.dac.groupd.exceptionhandler.errors;

public class FieldValueError {
	
	private String fieldName;
	private String messageUser;
	private String messageDeveloper;
	
	public FieldValueError (String fieldName, String messageUser, String messageDeveloper){
		setFieldName(fieldName);
		setMessageDeveloper(messageDeveloper);
		setMessageUser(messageUser);
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
