package br.edu.ifpb.dac.groupd.tests.integration;



import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ifpb.dac.groupd.dto.post.UserPostDto;
import br.edu.ifpb.dac.groupd.model.User;
import br.edu.ifpb.dac.groupd.service.UserService;

@Testable
@DisplayName("User Resources")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserResourcesTests {
	
	private final String PREFIX = "http://localhost:8080/api/users";
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private PasswordEncoder passEncoder;
	
	@Autowired
	private UserService userService;
	
	private UserPostDto dto = new UserPostDto();
	
	@BeforeEach
	void setUp() {
		dto = new UserPostDto();
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	@AfterEach
	void tearDown() {
		assertDoesNotThrow(() -> userService.deleteByUsername(dto.getEmail()));
	}
	
	
	@Test
	void testUserRegister() {
		
		dto.setEmail("ffc@pm.me");
		dto.setName("Filipe");
		dto.setPassword("ffc20221");
		
		String response = assertDoesNotThrow(()->{
			return
			mockMvc.perform(
				post(PREFIX)
					.contentType("application/json")
					.content(mapper.writeValueAsString(dto))
			)
			.andExpect(status().isCreated())
			.andReturn().getResponse().getContentAsString();
		});
		
		User user = assertDoesNotThrow(()->{
			return userService.findByEmail(dto.getEmail());
		});
		
		assertTrue(equals(dto, user));
		
		
	}
	
	private boolean equals(UserPostDto dto, User user) {
		return 
			(dto.getName().equals(user.getName()) &&
			 dto.getEmail().equals(user.getEmail()) &&
			 passEncoder.matches(dto.getPassword(), user.getPassword())
			);
				
	}
}
