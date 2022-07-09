package br.edu.ifpb.dac.groupd.presentation.controller.exceptionhandler.errors;

import java.util.List;

public class ErrorResponse {
	private Integer status;
	private String path;
	private List<ErrorData> errors;
	
	public ErrorResponse() {}// construtor default
	
	public ErrorResponse(Integer status, String path, List<ErrorData> errors) {
		super();
		this.status = status;
		this.path = path;
		this.errors = errors;
	}
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public List<? extends ErrorData> getErrors() {
		return errors;
	}
	public void setErrors(List<ErrorData> errors) {
		this.errors = errors;
	}
	
	
	
}
