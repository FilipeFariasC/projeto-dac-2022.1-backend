package br.edu.ifpb.dac.groupd.business.validation.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.edu.ifpb.dac.groupd.business.validation.constraints.ValidEmail;

public class EmailValidator 
implements ConstraintValidator<ValidEmail, String> {
  
  private Pattern pattern;
  private Matcher matcher;
  private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";
  private static final String REGEX_EMAIL = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
  private static final String PATTERN_EMAIL = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
  
  @Override
  public void initialize(ValidEmail constraintAnnotation) {
  }
  @Override
  public boolean isValid(String email, ConstraintValidatorContext context){
	  if(email == null) {
		  return false;
	  }
      return (validateEmail(email));
  } 
  private boolean validateEmail(String email) {
      pattern = Pattern.compile(PATTERN_EMAIL);
      matcher = pattern.matcher(email);
      
      return matcher.matches();
  }
}