package br.edu.ifpb.dac.groupd.tests.unit;

import static org.junit.jupiter.api.Assertions.*;

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

import br.edu.ifpb.dac.groupd.model.Alarm;

@Testable
@DisplayName("Alarm")
public class AlarmTests {

	private Alarm alarm;
	
	private Set<ConstraintViolation<Alarm>> violations;
	
	@Autowired
	private static Validator validator;
	
	@BeforeAll
	public static void setUpBeforeAll() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        System.out.println("Antes de todos");
	}
	
	@BeforeEach
	public void setUpBeforeEach() {
		alarm = new Alarm();
		System.out.println("antes de cada test");
		System.out.println(alarm.toString());
	}
	
	@Test
	@DisplayName("Fence Not Null")
	void testFenceNotNull() {
		alarm.setFence(null);
		
		violations = validator.validateProperty(alarm, "fence");
		System.out.println("tamanho:"+violations.size());
		assertNotEquals(0, violations.size(),"Valid not fence");
	}
	
	
	@Test
	@DisplayName("Location Null")
	void testLocationNull() {
		alarm.setLocation(null);
		
		violations = validator.validateProperty(alarm, "location");
		assertEquals(0, violations.size(),"Valid location");
	}
	
	
	

}
