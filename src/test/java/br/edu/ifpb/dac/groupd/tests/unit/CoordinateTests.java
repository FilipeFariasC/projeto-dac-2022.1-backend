package br.edu.ifpb.dac.groupd.tests.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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

import br.edu.ifpb.dac.groupd.model.entities.Coordinate;

@Testable
@DisplayName("Coordinates")
public class CoordinateTests {
	
	private Coordinate coordinate;
	
	private Set<ConstraintViolation<Coordinate>> violations;
	
	@Autowired
	private static Validator validator;
	
	@BeforeAll
	public static void setUpBeforeAll() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
	}
	@BeforeEach
	public void setUpBeforeEach() {
		coordinate = new Coordinate();
	}
	
	
	@DisplayName("Invalid Latitude")
	@ParameterizedTest(name="Invalid Latitude {index} -> {0}")
	@ValueSource(doubles = {-91.0, 91.0})
	void testInvalidLatitude(Double latitude) {
		coordinate.setLatitude(latitude);
		
		violations = validator.validateProperty(coordinate, "latitude");
//		violations.stream().forEach(System.out::println);
		assertNotEquals(0, violations.size(), () -> "Valid latitude" );
	}
	@DisplayName("Valid Latitude")
	@ParameterizedTest(name="Valid Latitude {index} -> {0}")
	@ValueSource(doubles = {-90.0, 90.0})
	void testValidLatitude(Double latitude) {
		coordinate.setLatitude(latitude);
		
		violations = validator.validateProperty(coordinate, "latitude");
		assertEquals(0, violations.size(), () -> "Invalid latitude" );
	}
	
	@DisplayName("Invalid Longitude")
	@ParameterizedTest(name="Invalid Longitude {index} -> {0}")
	@ValueSource(doubles = {-181.0, 181.0})
	void testInvalidLongitude(Double longitude) {
		coordinate.setLongitude(longitude);
		
		violations = validator.validateProperty(coordinate, "longitude");
		
//		violations.stream().forEach(System.out::println);
		assertNotEquals(0, violations.size(), () -> "Valid longitude" );
	}
	@DisplayName("Valid Longitude")
	@ParameterizedTest(name="Valid Longitude {index} -> {0}")
	@ValueSource(doubles = {-180.0, 180.0})
	void testValidLongitude(Double longitude) {
		coordinate.setLongitude(longitude);
		
		violations = validator.validateProperty(coordinate, "longitude");
		
		assertEquals(0, violations.size(), () -> "Invalid longitude" );
	}
}
