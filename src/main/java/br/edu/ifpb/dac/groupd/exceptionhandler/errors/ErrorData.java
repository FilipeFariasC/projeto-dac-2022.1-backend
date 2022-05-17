package br.edu.ifpb.dac.groupd.exceptionhandler.errors;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

@JsonSubTypes({ 
	  @Type(value = AttributeErrorData.class, name = "attributeErrorData"), 
	  @Type(value = AttributeValueErrorData.class, name = "truck") 
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
