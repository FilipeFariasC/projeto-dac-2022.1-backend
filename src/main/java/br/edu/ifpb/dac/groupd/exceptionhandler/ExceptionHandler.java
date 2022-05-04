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
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import br.edu.ifpb.dac.groupd.exceptionhandler.errors.FieldArgumentError;
import br.edu.ifpb.dac.groupd.exceptionhandler.errors.FieldValueError;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Autowired
	private MessageSource messageSource;
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		//define a mensagem que será enviada em caso de erro de conversao de entidade
		String messageUser = messageSource.getMessage("message.invalid", null, LocaleContextHolder.getLocale());;
		String messageDeveloper = ex.getCause().toString();
		
		Throwable cause = ex.getCause();
		
		FieldValueError error = null;
		if(cause instanceof UnrecognizedPropertyException upe) {
			messageUser = messageSource.getMessage("attribute.Unrecognized", new String[]{upe.getPropertyName()}, LocaleContextHolder.getLocale());
			messageDeveloper = upe.getOriginalMessage();
			error = new FieldValueError(upe.getPropertyName(), messageUser, messageDeveloper);

		} else {
			error = new FieldValueError(null, messageUser, messageDeveloper);
		}
		
		List<FieldValueError> errors = Arrays.asList(error);
		
		return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		List<FieldValueError> errors = createErrorList(ex.getBindingResult());
		
		return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	private List<FieldValueError> createErrorList(BindingResult bindingResult){
		List<FieldValueError> errors = new ArrayList<>();
		
		String messageUser = "";
		String messageDeveloper = "";
		
		for(FieldError field : bindingResult.getFieldErrors()) {
			messageUser = messageSource.getMessage(field, LocaleContextHolder.getLocale());
			messageDeveloper = field.toString();
			errors.add(new FieldArgumentError(field.getField(),field.getRejectedValue().toString(),messageUser, messageDeveloper));
		}

		return errors;
	}
	
	
}
