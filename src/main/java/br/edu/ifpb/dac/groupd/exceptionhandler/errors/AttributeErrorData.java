package br.edu.ifpb.dac.groupd.exceptionhandler.errors;

public class AttributeErrorData extends ErrorData {
	private String fieldName;

	public AttributeErrorData() {}
	
	public AttributeErrorData(String messageUser, String messageDeveloper, String fieldName) {
		super(messageUser, messageDeveloper);
		this.fieldName = fieldName;
	}
	


	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
}
