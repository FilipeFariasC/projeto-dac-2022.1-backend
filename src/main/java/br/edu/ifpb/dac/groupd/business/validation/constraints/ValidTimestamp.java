package br.edu.ifpb.dac.groupd.business.validation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import br.edu.ifpb.dac.groupd.business.validation.validator.ValidTimestampValidator;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidTimestampValidator.class)
@Documented
public @interface ValidTimestamp {
	String message() default "{br.edu.ifpb.dac.groupd.validation.contraints.ValidTimestamp.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
