package br.edu.ifpb.dac.groupd.tests.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.beans.factory.annotation.Autowired;

import br.edu.ifpb.dac.groupd.model.Location;

@Testable
@DisplayName("Location")
public class LocationTests {

	private Location location;
	
	private Set<ConstraintViolation<Location>> violations;
	
	@Autowired
	private static Validator validator;
	
	@BeforeAll
	public static void setUpBeforeAll() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
	}
	@BeforeEach
	public void setUpBeforeEach() {
		location = new Location();
	}
	@Test
	@DisplayName("Null Creation Date")
	public void testNullCreationDate() {
		location.setCreationDate(null);
		
		violations = validator.validateProperty(location, "creationDate");
		
		assertNotEquals(0, violations.size(), "Valid Timestamp");
	}
	@Test
	@DisplayName("Future Creation Date")
	public void testFutureCreationDate() {
		LocalDateTime date = LocalDateTime.now().plusSeconds(1);
		
		location.setCreationDate(date);
		
		violations = validator.validateProperty(location, "creationDate");
		
		assertNotEquals(0, violations.size(), "Valid Timestamp");
	}
	@Test
	@DisplayName("Valid Creation Date")
	public void testValidCreationDate() {
		LocalDateTime date = LocalDateTime.now().minusNanos(1);
		
		location.setCreationDate(date);
		
		violations = validator.validateProperty(location, "creationDate");
		
		assertEquals(0, violations.size(), "Valid Timestamp");
	}
}
