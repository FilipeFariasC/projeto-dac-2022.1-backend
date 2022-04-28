package br.edu.ifpb.dac.groupd.validation.validator;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import br.edu.ifpb.dac.groupd.model.Bracelet;
import br.edu.ifpb.dac.groupd.model.Fence;

public class ModelValidator {
	
	private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
	public static boolean validBracelet(Bracelet bracelet) {
		Set<ConstraintViolation<Bracelet>> violations = validator.validate(bracelet);
		
		return violations.size() == 0;
	}
	public static boolean validFence(Fence fence) {
		Set<ConstraintViolation<Fence>> violations = validator.validate(fence);
		
		return violations.size() == 0;
	}
}
