package br.edu.ifpb.dac.groupd.tests.unit;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.either;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Set;
import java.util.stream.Stream;

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.commons.annotation.Testable;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import br.edu.ifpb.dac.groupd.dto.post.UserPostDto;
import br.edu.ifpb.dac.groupd.model.Bracelet;
import br.edu.ifpb.dac.groupd.model.Coordinate;
import br.edu.ifpb.dac.groupd.model.Fence;
import br.edu.ifpb.dac.groupd.model.Role;
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
			assertThat(constraint.getMessageTemplate(), containsString("NotEmpty"));
		}
		
		@Order(2)
		@DisplayName("Empty Name")
		@ParameterizedTest(name="Invalid Test {index}")
		@ValueSource(strings= {"", "    ", " \n ", "\t"})
		void testInvalidNameEmptyCharacters(String name) {
			user.setName(name);
			
			violations = validator.validateProperty(user, "name");
			assertNotEquals(0, violations.size(), () -> "Valid name" );
			
			ConstraintViolation<User> constraint = violations.iterator().next();
			assertTrue(constraint.getPropertyPath().toString().contains("name"));
			assertThat(constraint.getMessageTemplate(), either(containsString("NotEmpty")).or(containsString("Size")));
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
		void testeInvalidNameWithFewerCharacters(String name) {
			user.setName(name);
			
			violations = validator.validateProperty(user, "name");
			assertNotEquals(0, violations.size(), () -> "Valid name");
			ConstraintViolation<User> constraint = violations.iterator().next();
			assertTrue(constraint.getPropertyPath().toString().contains("name"));
			assertThat(constraint.getMessageTemplate(), containsString("Size"));
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
		void testValidName(String name) {
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
		@Test
		public void testNullEmail() {
			user.setEmail(null);
			
			violations = validator.validateProperty(user, "email");
			assertNotEquals(0, violations.size(), () -> "Valid email" );
			ConstraintViolation<User> constraint = violations.iterator().next();
			
			assertTrue(constraint.getPropertyPath().toString().contains("email"));
			assertThat(constraint.getMessageTemplate(), containsString("NotEmpty"));
		}
		@Order(2)
		@DisplayName("Empty Email")
		@ParameterizedTest(name="Teste inválido {index}")
		@ValueSource(strings= {"", "    ", " \t "})
		void testInvalidEmailEmptyCharacters(String email) {
			user.setEmail(email);
			
			violations = validator.validateProperty(user, "email");
			assertNotEquals(0, violations.size(), () -> "Valid email" );
			
			ConstraintViolation<User> constraint = violations.iterator().next();
			assertTrue(constraint.getPropertyPath().toString().contains("email"));
			assertThat(constraint.getMessageTemplate(), containsString("NotEmpty"));
		}
		
		@Order(3)
		@DisplayName("Email out of pattern")
		@ParameterizedTest(name="Teste inválido {index} -> {0} ")
		@ValueSource(strings= {"filipe farias@email.com", "@gmail.com", "1filipe@"})
		void testInvalidEmailWithCharacters(String email) {
			user.setEmail(email);
			
			violations = validator.validateProperty(user, "email");
			assertNotEquals(0, violations.size(), () -> "Valid email" );
			ConstraintViolation<User> constraint = violations.iterator().next();
			assertThat(constraint.getMessageTemplate(), containsString("Email"));
		}
		@Order(4)
		@DisplayName("Valid Email")
		@ParameterizedTest(name="Teste válido {index} -> {0} ")
		@ValueSource(strings= {"filipe.farias@email.com", "f@f.f", "filipe@gmail"})
		void testValidEmail(String email) {
			user.setEmail(email);
			
			violations = validator.validateProperty(user, "email");
			assertEquals(0, violations.size(), () -> "Invalid Email");
		}
	}
	
	@Order(3)
	@Nested
	@DisplayName("Bracelets")
	@TestMethodOrder(OrderAnnotation.class)
	@ExtendWith(MockitoExtension.class)
	public class BraceletsTests{
		
		@Spy
		private Set<Bracelet> bracelets;// = spy(Set.class);
		
		@BeforeEach
		public void setUp() {
			user.setName("Filipe");
			user.setEmail("filipe@filipe.com");
			user.setPassword("abcdefghij");
			user.setBracelets(bracelets);

			openMocks(this);
		}
		
		@Order(1)
		@DisplayName("Add Invalid Bracelet")
		@ParameterizedTest(name="Invalid test {index} -> name=\"{0}\"")
		@ValueSource(strings={
				"", 
				"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
				})
		void testAddInvalidBracelet(String name) {
			Bracelet bracelet = new Bracelet();
			bracelet.setName(name);
		
			user.addBracelet(bracelet);
			
			verify(bracelets, never()).add(bracelet);
		}
		
		@Order(1)
		@DisplayName("Add Valid Bracelet")
		@ParameterizedTest(name="Valid test {index} -> name=\"{0}\"")
		@ValueSource(strings={
				"a", 
				"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
				})
		void testAddValidBracelet(String name) {
			Bracelet bracelet = new Bracelet();
			bracelet.setName(name);
			
			user.addBracelet(bracelet);
			
			violations = validator.validateProperty(user, "bracelets");
			
			assertEquals(0, violations.size(), () -> "Invalid bracelet" );
			verify(bracelets, only()).add(bracelet);
		}
	}
	@Order(4)
	@Nested
	@DisplayName("Fences")
	@TestMethodOrder(OrderAnnotation.class)
	@ExtendWith(MockitoExtension.class)
	public class FencesTests{
		
		@Spy
		private Set<Fence> fences;// = spy(Set.class);
		
		@BeforeEach
		public void setUp() {
			user.setFences(fences);
			
			openMocks(this);
		}
		
		@Order(1)
		@DisplayName("Add Invalid Fence")
		@ParameterizedTest(name="Invalid test {index} -> name=\"{0}\"")
		@MethodSource("invalidFences")
		void testAddInvalidFence(Fence fence) {
			user.addFence(fence);
			
			verify(fences, never()).add(fence);
		}
		
		private static Stream<Arguments> invalidFences(){
			return Stream.of(
				Arguments.of(createFence(-91.0, -180.0, 1.0)),
				Arguments.of(createFence(-90.0, -181.0, 1.0)),
				Arguments.of(createFence(-90.0, -180.0, 0.0)),
				Arguments.of(createFence(91.0, 180.0, 1.0)),
				Arguments.of(createFence(90.0, 181.0, 1.0)),
				Arguments.of(createFence(90.0, 180.0, 0.0))
					);
		}
		private static Stream<Arguments> validFences(){
			return Stream.of(
				Arguments.of(createFence(-90.0, -180.0, 1.0)),
				Arguments.of(createFence(90.0, 180.0, 1.0))
					);
		}
		
		private static Fence createFence(Double latitude, Double longitude, Double radius) {
			Fence fence = new Fence();
			fence.setCoordinate(new Coordinate(latitude, longitude));
			fence.setRadius(radius);
			
			return fence;
		}
		
		@Order(2)
		@DisplayName("Add Valid Fence")
		@ParameterizedTest(name="Valid test {index} -> name=\"{0}\"")
		@MethodSource("validFences")
		void testAddValidBracelet(Fence fence) {
			user.addFence(fence);
			
			verify(fences, only()).add(fence);
		}
	}
	@Order(5)
	@Nested
	@DisplayName("Roles")
	@TestMethodOrder(OrderAnnotation.class)
	@ExtendWith(MockitoExtension.class)
	public class RolesTests{
		
		@Spy
		private Set<Role> roles;// = spy(Set.class);
		
		@BeforeEach
		public void setUp() {
			user.setRoles(roles);

			openMocks(this);
		}
		
		@Order(1)
		@DisplayName("Add Role")
		@Test
		void testAddRole() {
			Role role = new Role("USER");
			
			user.addAuthority(role);
	
			verify(roles, only()).add(role);
		}
	}
	@Order(6)
	@Nested
	@DisplayName("Password")
	@TestMethodOrder(OrderAnnotation.class)
	public class PasswordTests{
		
		private UserPostDto user = new UserPostDto();
		
		private Set<ConstraintViolation<UserPostDto>> violations;
		
		
		void setUp() {
			user = new UserPostDto();
		}
		
		@Order(1)
		@DisplayName("Null Password")
		@Test
		void testPasswordNull() {
			user.setPassword(null);
			
			violations = validator.validateProperty(user, "password");
			ConstraintViolation<UserPostDto> constraint = violations.iterator().next();
			
			assertNotEquals(0, violations.size(), () -> "Valid password" );
			assertThat(constraint.getPropertyPath().toString(), containsString("password"));
			assertThat(constraint.getMessageTemplate(), either(containsString("NotNull")).or(containsString("NotEmpty")));
		}
		@Order(2)
		@DisplayName("Empty Password")
		@ParameterizedTest(name="Invalid test {index}")
		@ValueSource(strings= {"", "    ", " \t\n\t\r "})
		void testPasswordNoCharacters(String password) {
			user.setPassword(password);
			
			violations = validator.validateProperty(user, "password");
			ConstraintViolation<UserPostDto> constraint = violations.iterator().next();
			
			assertNotEquals(0, violations.size(), () -> "Valid password" );
			assertThat(constraint.getPropertyPath().toString(), containsString("password"));
			assertThat(constraint.getMessageTemplate(), either(containsString("Size")).or(containsString("Pattern")));
		}
		@Order(3)
		@DisplayName("Password Character Limit")
		@ParameterizedTest(name="Invalid test {index} -> ")
		@ValueSource(strings= {"abcdefg", "abcdefghijklmnopqrstuvwxyzABCDE"})
		void testPasswordCharacterLimit(String password) {
			user.setPassword(password);
			
			violations = validator.validateProperty(user, "password");
			ConstraintViolation<UserPostDto> constraint = violations.iterator().next();
			
			assertNotEquals(0, violations.size(), () -> "Valid password" );
			assertThat(constraint.getPropertyPath().toString(), containsString("password"));
			assertThat(constraint.getMessageTemplate(), containsString("Size"));
		}
		
		@Order(4)
		@DisplayName("Password doesn't contain spaces")
		@ParameterizedTest(name="Invalid test {index} -> {0} ")
		@ValueSource(strings= {"abcdef ghi", "a\taaaaaa\t"})
		void testePasswordNoSpaces(String password) {
			user.setPassword(password);
			
			violations = validator.validateProperty(user, "password");
			ConstraintViolation<UserPostDto> constraint = violations.iterator().next();
			
			assertNotEquals(0, violations.size(), () -> "Valid password" );
			assertThat(constraint.getPropertyPath().toString(), containsString("password"));
			assertThat(constraint.getMessageTemplate(), containsString("Pattern"));
		}
		@Order(5)
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
