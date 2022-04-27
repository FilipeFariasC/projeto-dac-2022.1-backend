package br.edu.ifpb.dac.groupd.tests.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalTime;
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

import br.edu.ifpb.dac.groupd.model.Fence;

@Testable
@DisplayName("Fence")
public class FenceTests {

	private Fence fence;

	private Set<ConstraintViolation<Fence>> violations;

	@Autowired
	private static Validator validator;

	@BeforeAll
	public static void setUpBeforeAll() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@BeforeEach
	public void setUpBeforeEach() {
		fence = new Fence();
	}

	@DisplayName("Valid Radius")
	@ParameterizedTest(name = "Valid Radius {index} -> {0}")
	@ValueSource(doubles = { 01.0, 10.0, 20.0 })
	void testValidRadius(Double radius) {
		fence.setRadius(radius);

		violations = validator.validateProperty(fence, "radius");
		assertEquals(0, violations.size(), () -> "Valid Radius");

	}

	@DisplayName("Invalid Radius")
	@ParameterizedTest(name = "Invalid Radius {index} -> {0}")
	@ValueSource(doubles = { -10.0, -0.1, 0.0, 0.9 })
	void testInvalidRadius(Double radius) {
		fence.setRadius(radius);

		violations = validator.validateProperty(fence, "radius");
		assertNotEquals(0, violations.size(), () -> "Invalid radius");

	}
	
	@Test
	@DisplayName("Valid Creation Time")
	public void testValidCreationTime() {
		LocalTime time = LocalTime.now();

		fence.setStartTime(time);

		violations = validator.validateProperty(fence, "startTime");

		assertEquals(0, violations.size(), "Valid time stamp");
	}
	
	@Test
	@DisplayName("Null Creation Time")
	public void testCreationTimeNull() {
		
		fence.setStartTime(null);

		violations = validator.validateProperty(fence, "startTime");

		assertEquals(0, violations.size(), "Null time stamp");
	}
}
