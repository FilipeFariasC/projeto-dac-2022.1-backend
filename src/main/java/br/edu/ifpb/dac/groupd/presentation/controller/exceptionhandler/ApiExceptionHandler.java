package br.edu.ifpb.dac.groupd.presentation.controller.exceptionhandler;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import br.edu.ifpb.dac.groupd.business.exception.AbstractException;
import br.edu.ifpb.dac.groupd.presentation.controller.exceptionhandler.errors.AttributeErrorData;
import br.edu.ifpb.dac.groupd.presentation.controller.exceptionhandler.errors.AttributeValueErrorData;
import br.edu.ifpb.dac.groupd.presentation.controller.exceptionhandler.errors.ErrorData;
import br.edu.ifpb.dac.groupd.presentation.controller.exceptionhandler.errors.ErrorResponse;
import io.jsonwebtoken.MalformedJwtException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Autowired
	private MessageSource messageSource;
	
	private static final String INVALID_MESSAGE = "message.invalid";
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		//define a mensagem que será enviada em caso de erro de conversao de entidade
		String messageUser = messageSource.getMessage(INVALID_MESSAGE, null, LocaleContextHolder.getLocale());
		String messageDeveloper = ex.getCause().toString();
		
		Throwable cause = ex.getCause();
		
		ErrorData error = null;
		if(cause instanceof UnrecognizedPropertyException upe) {
			messageUser = messageSource.getMessage("attribute.Unrecognized", new String[]{upe.getPropertyName()}, INVALID_MESSAGE, LocaleContextHolder.getLocale());
			messageDeveloper = upe.getOriginalMessage();
			error = new AttributeErrorData(messageUser, messageDeveloper, upe.getPropertyName());

		} else {
			error = new ErrorData(messageUser, messageDeveloper);
		}
		
		List<ErrorData> errors = Arrays.asList(error);
		
		ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), getRequestUri(request), errors);
		
		return handleExceptionInternal(ex, response, headers, HttpStatus.BAD_REQUEST, request);
	}
	@ExceptionHandler(value = {
			AbstractException.class
		})
	public ResponseEntity<?> handleCustomException(AbstractException exception, HttpServletRequest request){
		ErrorData error = new ErrorData(exception.getMessage(), "");
		
		List<ErrorData> errors = Arrays.asList(error);
		
		ErrorResponse response = new ErrorResponse(exception.getStatus().value(), getRequestUri(request), errors);
	
		return ResponseEntity.status(exception.getStatus()).body(response);
	}
	@ExceptionHandler(value = MalformedJwtException.class)
	public ResponseEntity<?> handleMalformedJwtException(MalformedJwtException exception, HttpServletRequest request){
		ErrorData error = new ErrorData(exception.getMessage(), exception.getMessage());
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		List<ErrorData> errors = Arrays.asList(error);
		
		ErrorResponse response = new ErrorResponse(status.value(), getRequestUri(request), errors);
	
		return ResponseEntity.status(status).body(response);
	}
	
	@ExceptionHandler(value = {InvalidDataAccessApiUsageException.class})
	public ResponseEntity<?> handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException exception, HttpServletRequest request){
		HttpHeaders headers = new HttpHeaders();
		
		String messageUser = exception.getMostSpecificCause().getMessage();
		if(messageUser.startsWith("could not resolve property")) {
			 messageUser = messageSource.getMessage("error.noProperty", getPropertyAndEntity(messageUser), INVALID_MESSAGE, LocaleContextHolder.getLocale());
		}
		ErrorData error = new ErrorData(messageUser, exception.getMostSpecificCause().getMessage());
		
		List<ErrorData> errors = Arrays.asList(error);
		ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), getRequestUri(request), errors);
		
		return handleExceptionInternal(exception, response, headers, HttpStatus.BAD_REQUEST, null);
	}

	@Override
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ResponseBody
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		List<ErrorData> globalErrors = createGlobalErrorList(ex.getBindingResult().getGlobalErrors());
		List<ErrorData> fieldErrors = createFieldErrorList(ex.getBindingResult().getFieldErrors());
		
		List<ErrorData> errors = Stream.of(globalErrors, fieldErrors)
			.flatMap(Collection::stream)
			.toList();
		
		ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), getRequestUri(request), errors);
		
		return handleExceptionInternal(ex, response, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	private List<ErrorData> createGlobalErrorList(List<ObjectError> globalErrors){
		
		return globalErrors
			.stream()
			.map(objectError->{
				String messageUser = objectError.getDefaultMessage();
				String messageDeveloper = objectError.toString();
				
				return new ErrorData(messageUser, messageDeveloper);
			}).toList();
	}
	
	private List<ErrorData> createFieldErrorList(List<FieldError> fieldErrors){
		
		return fieldErrors
			.stream()
			.map(fieldError->{
				String messageUser = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
				String messageDeveloper = fieldError.toString();
				
				String field = fieldError.getField();
				String rejectedValue = null;
				
				if(fieldError.getRejectedValue() != null) {
					rejectedValue = fieldError.getRejectedValue().toString();
				}
				
				return new AttributeValueErrorData(messageUser, messageDeveloper, field, rejectedValue);
			}).collect(Collectors.toList());
	}
	
	
	private String getRequestUri(HttpServletRequest request) {
		return request.getRequestURI();
	}
	private String getRequestUri(WebRequest request) {
		return ((ServletWebRequest)request).getRequest().getRequestURI();
	}
	
	private String[] splitMessage(String message) {
		return message.split(":");
	}
	
	private String[] getPropertyAndEntity(String message) {
		String[] splitted = splitMessage(message);
		String[] packages = splitted[2].split("\\.");
		String property = splitted[1].split(" ")[1];
		String entity = packages[packages.length-1];
		
		return new String[] {property,entity};
	}
	
	private String getProperty(String message) {
		String[] splitted = splitMessage(message);
		
		return splitted[1].split(" ")[1];
	}
	private String getEntity(String message) {
		String[] splitted = splitMessage(message);
		
		String[] packages = splitted[2].split("\\.");
		
		return packages[packages.length-1];
	}
	
	
}
