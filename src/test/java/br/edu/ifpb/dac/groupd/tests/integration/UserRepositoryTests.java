package br.edu.ifpb.dac.groupd.tests.integration;


import static org.mockito.Mockito.mockitoSession;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import br.edu.ifpb.dac.groupd.repository.UserRepository;

@Testable
@DisplayName("User Repository")
@RunWith(MockitoJUnitRunner.class)
class UserRepositoryTests {
	
	@Mock
	private UserRepository userRepo;
	
	
	@BeforeEach
	void setUp() {
		initMocks(this);
	}
	
	@Test
	void test() {
		when(userRepo.existsById(1L)).thenReturn(true);
		
		System.out.println(userRepo.existsById(1L));
	}

}
