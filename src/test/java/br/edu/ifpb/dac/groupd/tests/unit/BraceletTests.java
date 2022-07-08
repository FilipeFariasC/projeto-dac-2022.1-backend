package br.edu.ifpb.dac.groupd.tests.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.commons.annotation.Testable;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;

import br.edu.ifpb.dac.groupd.model.entities.Bracelet;
import br.edu.ifpb.dac.groupd.model.entities.Coordinate;
import br.edu.ifpb.dac.groupd.model.entities.Fence;
import br.edu.ifpb.dac.groupd.model.entities.Location;

@Testable
@DisplayName("Bracelet")
public class BraceletTests {
	
	private Bracelet bracelet;
	@Mock
	private Fence fence;
	@Mock
	private Coordinate coordinate;
	
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
		bracelet.setName(null);
		
		violations = validator.validateProperty(bracelet, "name");
		violations.stream().forEach(System.out::println);
		
		assertNotEquals(0, violations.size(), () -> "Valid name" );
	}
	@DisplayName("Name is empty")
	@ParameterizedTest(name="Invalid Teste {index}")
	@ValueSource(strings= {"", "\r", "\r\r", " ", "\t", "\n", " \t "})
	void testNameEmpty(String name) {
		bracelet.setName(name);
		violations = validator.validateProperty(bracelet, "name");
		assertNotEquals(0, violations.size(), () -> "Valid name" );
	}
	@DisplayName("Name has more than 50 characters")
	@Test
	void testNameMoreThanFiftyCharacters() {
		bracelet.setName("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		violations = validator.validateProperty(bracelet, "name");
		assertNotEquals(0, violations.size(), () -> "Valid name" );
	}
	@DisplayName("Name is Valid")
	@ParameterizedTest(name="Invalid Test {index} -> {0}")
	@ValueSource(strings= {"a","1","abc", "123","abc123","123abc","\t\taaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\t\t","aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"})
	void testNameValid(String name) {
		bracelet.setName(name);
		violations = validator.validateProperty(bracelet, "name");
		assertEquals(0, violations.size(), () -> "Valid name" );
	}
	
	@Test
	void fence() {
		fence = new Fence();
		fence.setId(null);
		fence.setBracelets(null);
		
		coordinate = new Coordinate();
		coordinate.setLatitude(-8.07439);
		coordinate.setLongitude(-37.2646);
		
		fence.setCoordinate(coordinate);
		fence.setRadius(10.00);
		fence.setStartTime(LocalTime.now());
		fence.setFinishTime(LocalTime.now().plusHours(1));
		
		openMocks(this);
		
		bracelet.setMonitor(fence);
		
		assertEquals(fence, bracelet.getMonitor());
		
	}
	
	/**
	@Test
	void fenceMock() {
		fence.addBracelet(bracelet);
		verify(fence.getBracelets()).add(bracelet);
		
	}
	**/
	
	@Test
	void testAddFence() {
		
	}
	
	@Test
	void Location() {
		Location location = new Location();
		location.setId(null);
		location.setBracelet(bracelet);
		
		coordinate = new Coordinate();
		coordinate.setLatitude(-8.07439);
		coordinate.setLongitude(-37.2646);
		
		
		location.setCoordinate(coordinate);
		location.setCreationDate(null);
		
		List<Location> list = new LinkedList<Location>();
		List<Location> locations = spy(list);
		locations.add(location);
		
		verify(locations).add(location);
		
		when(locations.size()).thenReturn(1);
		
		violations = validator.validateProperty(bracelet, "locations");
		assertEquals(0, violations.size(), () -> "Invalid location" );
	}
	
	
	
		
}
