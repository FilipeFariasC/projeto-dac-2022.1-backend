package br.edu.ifpb.dac.groupd.validation.contraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import br.edu.ifpb.dac.groupd.validation.validator.ValidTimerValidator;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidTimerValidator.class)
@Documented
public @interface ValidTimer {
	String message() default "{br.edu.ifpb.dac.groupd.validation.contraints.ValidTimer.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
