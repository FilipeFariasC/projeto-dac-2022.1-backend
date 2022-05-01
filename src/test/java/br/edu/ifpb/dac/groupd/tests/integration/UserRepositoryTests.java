package br.edu.ifpb.dac.groupd.tests.integration;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.annotation.Testable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.edu.ifpb.dac.groupd.repository.UserRepository;

@Testable
@DisplayName("User Repository")
@ExtendWith(MockitoExtension.class)
class UserRepositoryTests {
	
	@Mock
	private UserRepository userRepo;
	
	
	@BeforeEach
	void setUp() {
		openMocks(this);
	}
	
	@Test
	void test() {
		when(userRepo.existsById(1L)).thenReturn(true);
		
		assertTrue(userRepo.existsById(1L));
		System.out.println(userRepo.existsById(1L));
	}

}
