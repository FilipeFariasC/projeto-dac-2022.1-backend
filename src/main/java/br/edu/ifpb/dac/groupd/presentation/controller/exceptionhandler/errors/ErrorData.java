package br.edu.ifpb.dac.groupd.presentation.controller.exceptionhandler.errors;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property="tipo")
@JsonSubTypes({
  @Type(value=AttributeErrorData.class, name = "AttributeErrorData"),
  @Type(value=AttributeValueErrorData.class, name = "AttributeValueErrorData")
})
public class ErrorData {
	
	private String messageUser;
	private String messageDeveloper;
	
	public ErrorData() {}
	
	public ErrorData(String messageUser, String messageDeveloper) {
		super();
		this.messageUser = messageUser;
		this.messageDeveloper = messageDeveloper;
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
