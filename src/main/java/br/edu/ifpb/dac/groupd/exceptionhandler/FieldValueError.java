package br.edu.ifpb.dac.groupd.exceptionhandler;

public class FieldValueError {
	private String messageUser;
	private String messageDeveloper;
	
	protected FieldValueError (String messageUser, String messageDeveloper){
		setMessageDeveloper(messageDeveloper);
		setMessageUser(messageUser);
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
