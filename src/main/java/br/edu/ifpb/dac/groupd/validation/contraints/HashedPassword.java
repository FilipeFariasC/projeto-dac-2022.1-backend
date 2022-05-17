package br.edu.ifpb.dac.groupd.validation.contraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import br.edu.ifpb.dac.groupd.validation.validator.HashedPasswordValidator;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HashedPasswordValidator.class)
@Documented
public @interface HashedPassword {
	String message() default "{br.edu.ifpb.dac.groupd.validation.contraints.HashedPassword}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
