package br.edu.ifpb.dac.groupd.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.edu.ifpb.dac.groupd.interfaces.Timer;
import br.edu.ifpb.dac.groupd.validation.contraints.ValidTimer;

public class ValidTimerValidator implements ConstraintValidator<ValidTimer, Timer> {

	@Override
	public boolean isValid(Timer value, ConstraintValidatorContext context) {
		if(value == null) {
			return false;
		}
		
		if(value.getStartTime() != null && value.getFinishTime() != null) {
			return value.getStartTime().isBefore(value.getFinishTime());
		}
		return true;
	}

}
