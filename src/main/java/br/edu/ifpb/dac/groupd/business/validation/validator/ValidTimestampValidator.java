package br.edu.ifpb.dac.groupd.business.validation.validator;

import java.time.LocalDateTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.edu.ifpb.dac.groupd.business.validation.constraints.ValidTimestamp;

public class ValidTimestampValidator implements ConstraintValidator<ValidTimestamp, LocalDateTime> {

	public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
		if(value == null)
			return true;
		
		return value.isBefore(LocalDateTime.now());
	}

}
