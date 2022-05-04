package br.edu.ifpb.dac.groupd.exceptionhandler.errors;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"fieldName", "path","statusCode", "messageUser", "messageDeveloper" })
public class FieldValueError {
	
	private Integer statusCode;
	private String fieldName;
	private String messageUser;
	private String messageDeveloper;
	private String path;
	
	public FieldValueError (String fieldName, String messageUser, String messageDeveloper, String path, Integer statusCode){
		setFieldName(fieldName);
		setMessageDeveloper(messageDeveloper);
		setMessageUser(messageUser);
		setPath(path);
		setStatusCode(statusCode);
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

	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Integer getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
}
