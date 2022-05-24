package br.edu.ifpb.dac.groupd.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.edu.ifpb.dac.groupd.validation.contraints.HashedPassword;

public class HashedPasswordValidator implements ConstraintValidator<HashedPassword, String>{

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value == null) {
			return true;
		}
		return value.matches("^\\$2[ayb]\\$.{56}$");
	}

}
