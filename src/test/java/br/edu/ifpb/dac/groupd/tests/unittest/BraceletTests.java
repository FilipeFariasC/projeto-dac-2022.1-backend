package br.edu.ifpb.dac.groupd.tests.unittest;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.beans.factory.annotation.Autowired;

import br.edu.ifpb.dac.groupd.model.Bracelet;

@Testable
@DisplayName("Bracelet")
public class BraceletTests {
	
	private Bracelet bracelet;
	
	private Set<ConstraintViolation<Bracelet>> violations;
	
	@Autowired
	private static Validator validator;
	
	@BeforeAll
	public static void setUpBeforeAll() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
	}
	@BeforeEach
	public void setUpBeforeEach() {
		bracelet = new Bracelet();
	}
	
	@Test
	@DisplayName("Name is null")
	void testNameNull() {
		bracelet.setName("");
		
		violations = validator.validateProperty(bracelet, "name");
		assertNotEquals(0, violations.size(), () -> "Valid name" );
	}
	@DisplayName("Name is empty")
	@ParameterizedTest(name="Invalid Teste {index}")
	@ValueSource(strings= {"", "\r", "\r\r"})
	void testNameEmpty(String name) {
		bracelet.setName(name);
		violations = validator.validateProperty(bracelet, "name");
		assertNotEquals(0, violations.size(), () -> "Valid name" );
	}
	@DisplayName("Name is blank")
	@ParameterizedTest(name="Invalid Test {index}")
	@ValueSource(strings= {"      ", "\t", "\n", " \t "})
	void testNameBlank(String name) {
		bracelet.setName(name);
		violations = validator.validateProperty(bracelet, "name");
		assertNotEquals(0, violations.size(), () -> "Valid name" );
	}
	@DisplayName("Name has more than 50 characters")
	@ParameterizedTest(name="Invalid Test {index}")
	@ValueSource(strings= {"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "\t", "\n", " \t "})
	void testNameMoreThanFiftyCharacters(String name) {
		bracelet.setName(name);
		violations = validator.validateProperty(bracelet, "name");
		assertNotEquals(0, violations.size(), () -> "Valid name" );
	}
	
}
