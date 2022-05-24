package br.edu.ifpb.dac.groupd.tests.integration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Testable
@DisplayName("Fence Resources Tests")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
@ActiveProfiles("test")
class FenceResourcesTestes {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	// CREATE
	@Test
	void testCreateFenceInvalid() {
		fail("Not yet implemented");
	}
	@Test
	void testCreateFenceValid() {
		fail("Not yet implemented");
	}

	
	// READ
	@Test
	void testGetAllFencesByInvalidId() {
		fail("Not yet implemented");
	}
	@Test
	void testGetAllFencesByValidId() {
		fail("Not yet implemented");
	}
	
	// UPDATE
	@Test
	void testUpdateFenceInvalidId() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdateFenceValidIdInvalidFence() {
		fail("Not yet implemented");
	}
	@Test
	void testUpdateFenceValidIdValidFence() {
		fail("Not yet implemented");
	}
	
	// Update
	@Test
	void testStatusActiveNoBracelet() {
		fail("Not yet implemented");
	}
	@Test
	void testStatusActiveNoBraceletAvailable() {
		fail("Not yet implemented");
	}
	@Test
	void testStatusActiveBraceletAvailable() {
		fail("Not yet implemented");
	}
	
	// DELETE
	@Test
	void testDeleteFence() {
		fail("Not yet implemented");
	}

}
