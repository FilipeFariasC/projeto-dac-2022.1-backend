package br.edu.ifpb.dac.groupd.business.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.edu.ifpb.dac.groupd.business.validation.constraints.HashedPassword;

public class HashedPasswordValidator implements ConstraintValidator<HashedPassword, String>{

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value == null) {
			return true;
		}
		return value.matches("^\\$2[ayb]\\$.{56}$");
	}

}
