package br.edu.ifpb.dac.groupd.exceptionhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import br.edu.ifpb.dac.groupd.exceptionhandler.errors.AttributeErrorData;
import br.edu.ifpb.dac.groupd.exceptionhandler.errors.AttributeValueErrorData;
import br.edu.ifpb.dac.groupd.exceptionhandler.errors.ErrorData;
import br.edu.ifpb.dac.groupd.exceptionhandler.errors.ErrorResponse;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Autowired
	private MessageSource messageSource;
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		//define a mensagem que será enviada em caso de erro de conversao de entidade
		String messageUser = messageSource.getMessage("message.invalid", null, LocaleContextHolder.getLocale());;
		String messageDeveloper = ex.getCause().toString();
		
		Throwable cause = ex.getCause();
		
		ErrorData error = null;
		if(cause instanceof UnrecognizedPropertyException upe) {
			messageUser = messageSource.getMessage("attribute.Unrecognized", new String[]{upe.getPropertyName()}, LocaleContextHolder.getLocale());
			messageDeveloper = upe.getOriginalMessage();
			error = new AttributeErrorData(messageUser, messageDeveloper, upe.getPropertyName());

		} else {
			error = new ErrorData(messageUser, messageDeveloper);
		}
		
		List<ErrorData> errors = Arrays.asList(error);
		
		ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), getRequestUri(request), errors);
		
		return handleExceptionInternal(ex, response, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@Override
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ResponseBody
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		List<ErrorData> errors = createErrorList(ex.getBindingResult());
		
		ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), getRequestUri(request), errors);
		
		return handleExceptionInternal(ex, response, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	private List<ErrorData> createErrorList(BindingResult bindingResult){
		List<ErrorData> errors = new ArrayList<>();
		
		String messageUser = "";
		String messageDeveloper = "";
		
		for(FieldError fieldError : bindingResult.getFieldErrors()) {
			messageUser = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
			messageDeveloper = fieldError.toString();
			
			String field = fieldError.getField();
			String rejectedValue = null;
			
			if(fieldError.getRejectedValue() != null) {
				rejectedValue = fieldError.getRejectedValue().toString();
			}
			
			errors.add(new AttributeValueErrorData(messageUser, messageDeveloper, field, rejectedValue));
		}

		return errors;
	}
	
	private String getRequestUri(WebRequest request) {
		return ((ServletWebRequest)request).getRequest().getRequestURI();
	}
}
