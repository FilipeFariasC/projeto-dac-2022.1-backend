package br.edu.ifpb.dac.groupd.tests.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.commons.annotation.Testable;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;

import br.edu.ifpb.dac.groupd.model.Bracelet;
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
		
		@Order(1)
		@Test
		@DisplayName("Name is null")
		void testNameNull() {
			user.setName(null);
			
			violations = validator.validateProperty(user, "name");
			assertNotEquals(0, violations.size(), () -> "Valid name" );
			
			ConstraintViolation<User> constraint = violations.iterator().next();
			assertTrue(constraint.getPropertyPath().toString().contains("name"));
			assertTrue(constraint.getMessageTemplate().toString().contains("NotEmpty"));
		}
		
		@Order(2)
		@DisplayName("Empty Name")
		@ParameterizedTest(name="Invalid Test {index}")
		@ValueSource(strings= {"", "    ", " \n ", "\t"})
		void testeNomeInvalidoSemCaracteresVazios(String name) {
			user.setName(name);
			
			violations = validator.validateProperty(user, "name");
			assertNotEquals(0, violations.size(), () -> "Valid name" );
			
			ConstraintViolation<User> constraint = violations.iterator().next();
			assertTrue(constraint.getPropertyPath().toString().contains("name"));
			System.out.println(constraint.getMessageTemplate());
			assertTrue(constraint.getMessageTemplate().toString().contains("NotEmpty") || constraint.getMessageTemplate().toString().contains("Size"));
		}
		
		@Order(3)
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
			ConstraintViolation<User> constraint = violations.iterator().next();
			assertTrue(constraint.getPropertyPath().toString().contains("name"));
			assertTrue(constraint.getMessageTemplate().toString().contains("Size"));
		}
		
		@Order(4)
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
		@Order(1)
		@DisplayName("Null Email")
		@ParameterizedTest(name="Teste inválido {index}")
		@ValueSource(strings= {"", "    ", " \t "})
		void testNullEmail(String email) {
			user.setEmail(null);
			
			violations = validator.validateProperty(user, "email");
			assertNotEquals(0, violations.size(), () -> "Valid email" );
		}
		@Order(2)
		@DisplayName("Empty Email")
		@ParameterizedTest(name="Teste inválido {index}")
		@ValueSource(strings= {"", "    ", " \t "})
		void testeEmailInvalidoSemCaracteres(String email) {
			user.setEmail(email);
			
			violations = validator.validateProperty(user, "email");
			assertNotEquals(0, violations.size(), () -> "Valid email" );
		}
		
		@Order(3)
		@DisplayName("Email out of pattern")
		@ParameterizedTest(name="Teste inválido {index} -> {0} ")
		@ValueSource(strings= {"filipe farias@email.com", "@gmail.com", "1filipe@"})
		void testeEmailInvalidoComCaracteres(String email) {
			user.setEmail(email);
			
			violations = validator.validateProperty(user, "email");
			assertNotEquals(0, violations.size(), () -> "Valid email" );
		}
		@Order(4)
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
		@Order(2)
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
	
	@Order(4)
	@Nested
	@DisplayName("Bracelets")
	@TestMethodOrder(OrderAnnotation.class)
	public class BraceletsTests{
		
		@Spy
		private Set<@Valid Bracelet> bracelets = new HashSet<>();
		
		@BeforeEach
		public void setUp() {
			user.setName("Filipe");
			user.setEmail("filipe@filipe.com");
			user.setPassword("abcdefghij");
			user.setBracelets(bracelets);
			openMocks(this);
		}
		
		@Order(1)
		@DisplayName("Invalid Bracelet")
		@ParameterizedTest(name="Invalid test {index} -> {0}")
		@ValueSource(strings={
				"", 
				"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
				})
		void testBraceletInvalid(String name) {
			Bracelet bracelet = new Bracelet();
			bracelet.setName(name);
			
			user.addBracelet(bracelet);
			
			violations = validator.validate(user);
			assertNotEquals(0, violations.size(), () -> "Valid bracelet" );
			assertTrue(violations.iterator().next().getPropertyPath().toString().contains("bracelets"));
			
			verify(bracelets, never()).add(bracelet);
		}
		
	}
	
}
