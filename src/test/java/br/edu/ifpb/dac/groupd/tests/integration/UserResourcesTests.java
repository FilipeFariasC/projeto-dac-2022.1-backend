package br.edu.ifpb.dac.groupd.tests.integration;


import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ifpb.dac.groupd.dto.post.UserPostDto;
import br.edu.ifpb.dac.groupd.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.model.User;
import br.edu.ifpb.dac.groupd.service.UserService;

@Testable
@DisplayName("User Resources")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserResourcesTests {
	
	private final String PREFIX = "http://localhost:8080/api/users";
	
	@Autowired
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
	}
	@AfterEach
	void tearDown() {
		try {
			userService.deleteByUsername(dto.getEmail());
		} catch (UserNotFoundException e) {
		}
	}
	
	@Test
	void testInvalidUserRegister() {
		dto.setEmail("f@f");
		dto.setName("Fi");
		dto.setPassword("ffc20 221");
		
		String response = assertDoesNotThrow(()->{
			return
			mockMvc.perform(
				post(PREFIX)
					.contentType("application/json")
					.content(mapper.writeValueAsString(dto))
			)
			.andExpect(status().isBadRequest())
			.andReturn().getResponse().getContentAsString();
		});
		
		assertThat(response, containsString("email"));
		assertThat(response, containsString("password"));
		assertThat(response, containsString("email"));
		
		assertThrows(UserNotFoundException.class, ()->userService.findByEmail(dto.getEmail()));
		
	}
	@Test
	void testValidUserRegister() {
		
		dto = validUser();
		
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
	
	@Test
	void testPostEmailAlreadyUsed() {
		dto = validUser();
		try {
			mockMvc.perform(
				post(PREFIX)
					.contentType("application/json")
					.content(mapper.writeValueAsString(dto))
			);
		} catch (Exception e) {
		}
		
		
		String response = assertDoesNotThrow(()->{
			return
			mockMvc.perform(
				post(PREFIX)
					.contentType("application/json")
					.content(mapper.writeValueAsString(dto))
			)
			.andExpect(status().isConflict())
			.andReturn().getResponse().getContentAsString();
		});
		assertThat(response, containsString(dto.getEmail()));
	}
	
	@Test
	void testPutEmailAlreadyUsed() {
		dto = validUser();
		
		UserPostDto dtoNewUser = validUser();
		
		String differentEmail = "ffc@protonmail.com";
		String differentPassword = "filipefariasc";
		
		dtoNewUser.setEmail(differentEmail);
		dtoNewUser.setPassword(differentPassword);
		
		try {
			mockMvc.perform(
				post(PREFIX)
					.contentType("application/json")
					.content(mapper.writeValueAsString(dto))
			);
			mockMvc.perform(
				post(PREFIX)
					.contentType("application/json")
					.content(mapper.writeValueAsString(dtoNewUser))
			);
			
		} catch (Exception e) {
		}
		
		validUser();
		
		String response = assertDoesNotThrow(()->{
			return
			mockMvc.perform(
				put(PREFIX)
				.with(user(differentEmail).password(differentPassword))
					.contentType("application/json")
					.content(mapper.writeValueAsString(dto))
			)
			.andExpect(status().isConflict())
			.andReturn().getResponse().getContentAsString();
		});
		System.out.println(response);
		assertThat(response, containsString(dto.getEmail()));
	}
	
	
	
	UserPostDto validUser() {
		UserPostDto dto = new UserPostDto();
		dto.setEmail("filipefariasc@protonmail.com");
		dto.setName("Filipe Farias");
		dto.setPassword("ffc20221*");
		
		return dto;
	}
	
	private boolean equals(UserPostDto dto, User user) {
		return 
			(dto.getName().equals(user.getName()) &&
			 dto.getEmail().equals(user.getEmail()) &&
			 passEncoder.matches(dto.getPassword(), user.getPassword())
			);
				
	}
}
