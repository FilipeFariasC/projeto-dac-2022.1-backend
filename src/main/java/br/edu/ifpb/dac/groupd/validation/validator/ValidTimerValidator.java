package br.edu.ifpb.dac.groupd.validation.validator;

import java.time.LocalTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.edu.ifpb.dac.groupd.dto.post.FencePostDto;
import br.edu.ifpb.dac.groupd.validation.contraints.ValidTimer;

public class ValidTimerValidator implements ConstraintValidator<ValidTimer, FencePostDto> {

	@Override
	public boolean isValid(FencePostDto value, ConstraintValidatorContext context) {
		if(value == null) {
			return false;
		}
		
		if(value.getStartTime() != null && value.getFinishTime() != null) {
			return value.getStartTime().isBefore(value.getFinishTime());
		}
		return true;
	}

}
