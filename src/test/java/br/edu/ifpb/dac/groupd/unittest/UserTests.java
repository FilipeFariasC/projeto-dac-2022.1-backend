package br.edu.ifpb.dac.groupd.unittest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.beans.factory.annotation.Autowired;

import br.edu.ifpb.dac.groupd.model.User;

@Testable
@DisplayName("Teste unitário da entidade usuário")
class UserTests {
	
	private User user;
	
	
	@Autowired
	private static Validator validator;
	
	@BeforeAll
	public static void setUpBeforeAll() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
	}
	@BeforeEach
	public void setUpBeforeEach() {
		user = new User();

	}

	@DisplayName("Email inválido")
	@ParameterizedTest(name="Teste inválido {index}")
	@ValueSource(strings= {"", "    ", " \t "})
	void testeEmailInvalidoSemCaracteres(String email) {
		user.setEmail(email);
		
		Set<ConstraintViolation<User>> violations = validator.validateProperty(user, "email");
		assertTrue((violations.size() > 0), () -> "Email válido" );
	}
	
	
	@DisplayName("Email fora do padrão")
	@ParameterizedTest(name="Teste inválido {index} -> {0} ")
	@ValueSource(strings= {"filipe farias@email.com", "@gmail.com", "1filipe@"})
	void testeEmailInvalidoComCaracteres(String email) {
		user.setEmail(email);
		
		Set<ConstraintViolation<User>> violations = validator.validateProperty(user, "email");
		assertTrue((violations.size() > 0), () -> "Email válido" );
	}
	
	@DisplayName("Email dentro do padrão")
	@ParameterizedTest(name="Teste inválido {index} -> {0} ")
	@ValueSource(strings= {"filipe.farias@email.com", "f@f.f", "filipe@gmail"})
	void testEmailValido(String email) {
		user.setEmail(email);
		
		Set<ConstraintViolation<User>> violations = validator.validateProperty(user, "email");
		assertTrue((violations.size() == 0), () -> "Email Inválido");
	}
	
}
