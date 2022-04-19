package br.edu.ifpb.dac.groupd.tests.unittest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.beans.factory.annotation.Autowired;

import br.edu.ifpb.dac.groupd.model.User;

@Testable
@DisplayName("User")
@TestClassOrder(org.junit.jupiter.api.ClassOrderer.OrderAnnotation.class)
public class UserTests {
	
	private User user;
	private Set<ConstraintViolation<User>> violations;
	
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
	
	@Order(1)
	@Nested
	@DisplayName("Name")
	@TestMethodOrder(OrderAnnotation.class)
	public class NameTests{
		@Order(0)
		@DisplayName("Null Name")
		void testNameIsNull() {
			user.setName(null);
			
			violations = validator.validateProperty(user, "name");
			assertNotEquals(0, violations.size(), () -> "Valid name" );
		}
		@Order(1)
		@DisplayName("Empty Name")
		@ParameterizedTest(name="Invalid Test {index}")
		@ValueSource(strings= {"", "    ", " \n ", "\t"})
		void testeNomeInvalidoSemCaracteresVazios(String name) {
			user.setName(name);
			
			violations = validator.validateProperty(user, "name");
			assertNotEquals(0, violations.size(), () -> "Valid name" );
		}
		
		@Order(2)
		@DisplayName("Name Character Limits")
		@ParameterizedTest(name="Invalid Test {index} -> {0}.")
		@ValueSource(strings= {
				"a", 
				"a", 
				" a ", 
				"aa",
				"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
				})
		void testeNomeInvalidoComCaracteresPoucosCaracteres(String name) {
			user.setName(name);
			
			violations = validator.validateProperty(user, "name");
			assertNotEquals(0, violations.size(), () -> "Valid name");
		}
		
		@Order(3)
		@DisplayName("Valid Name")
		@ParameterizedTest(name="Valid Test {index} -> {0}.")
		@ValueSource(strings= {
				"\t\nabcdefghijlmnopqrstuvxyzABCDEFGHIJLMNOPQRSTUVXYZ\t\n",
				"Filipe Farias Chagas",
				"\taaa\t",
				"abc"
				})
		void testeNomeValido(String name) {
			user.setName(name);
			
			violations = validator.validateProperty(user, "name");
			assertEquals(0, violations.size(), () -> "Invalid name");
		}
		
	}
	
	@Order(2)
	@Nested
	@DisplayName("Email")
	@TestMethodOrder(OrderAnnotation.class)
	public class EmailTests{
		@Order(0)
		@DisplayName("Null Email")
		@ParameterizedTest(name="Teste inválido {index}")
		@ValueSource(strings= {"", "    ", " \t "})
		void testNullEmail(String email) {
			user.setEmail(null);
			
			violations = validator.validateProperty(user, "email");
			assertNotEquals(0, violations.size(), () -> "Valid email" );
		}
		@Order(1)
		@DisplayName("Empty Email")
		@ParameterizedTest(name="Teste inválido {index}")
		@ValueSource(strings= {"", "    ", " \t "})
		void testeEmailInvalidoSemCaracteres(String email) {
			user.setEmail(email);
			
			violations = validator.validateProperty(user, "email");
			assertNotEquals(0, violations.size(), () -> "Valid email" );
		}
		
		@Order(2)
		@DisplayName("Email out of pattern")
		@ParameterizedTest(name="Teste inválido {index} -> {0} ")
		@ValueSource(strings= {"filipe farias@email.com", "@gmail.com", "1filipe@"})
		void testeEmailInvalidoComCaracteres(String email) {
			user.setEmail(email);
			
			violations = validator.validateProperty(user, "email");
			assertNotEquals(0, violations.size(), () -> "Valid email" );
		}
		@Order(3)
		@DisplayName("Valid Email")
		@ParameterizedTest(name="Teste válido {index} -> {0} ")
		@ValueSource(strings= {"filipe.farias@email.com", "f@f.f", "filipe@gmail"})
		void testEmailValido(String email) {
			user.setEmail(email);
			
			violations = validator.validateProperty(user, "email");
			assertEquals(0, violations.size(), () -> "Invalid Email");
		}
	}
	
	@Order(3)
	@Nested
	@DisplayName("Password")
	@TestMethodOrder(OrderAnnotation.class)
	public class PasswordTests{
		@Order(1)
		@DisplayName("Empty Password")
		@ParameterizedTest(name="Invalid test {index}")
		@ValueSource(strings= {"", "    ", " \t\n\t\r "})
		void testPasswordNoCharacters(String password) {
			user.setPassword(password);
			
			violations = validator.validateProperty(user, "password");
			assertNotEquals(0, violations.size(), () -> "Valid password" );
		}
		@Order(1)
		@DisplayName("Password Character Limit")
		@ParameterizedTest(name="Invalid test {index} -> ")
		@ValueSource(strings= {"abcdefg", "abcdefghijklmnopqrstuvwxyzABCDE", ""})
		void testPasswordCharacterLimit(String password) {
			user.setPassword(password);
			
			violations = validator.validateProperty(user, "password");
			assertNotEquals(0, violations.size(), () -> "Valid password" );
		}
		@Order(3)
		@DisplayName("Password doesn't contain spaces")
		@ParameterizedTest(name="Invalid test {index} -> {0} ")
		@ValueSource(strings= {"abcdef ghi", "a\taaaaaa\t"})
		void testePasswordNoSpaces(String password) {
			user.setPassword(password);
			
			violations = validator.validateProperty(user, "password");
			assertNotEquals(0, violations.size(), () -> "Valid password" );
		}
		@Order(4)
		@DisplayName("Password Valid")
		@ParameterizedTest(name="Valid test {index} -> {0} ")
		@ValueSource(strings= {"abcdefgh", "abcdefghijklmnopqrstuvwxyzABCD","a1b*C3d9_"})
		void testePasswordValid(String password) {
			user.setPassword(password);
			
			violations = validator.validateProperty(user, "password");
			assertEquals(0, violations.size(), () -> "Invalid password" );
		}
	}
	
}
