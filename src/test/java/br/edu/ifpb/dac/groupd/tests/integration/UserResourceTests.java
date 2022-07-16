package br.edu.ifpb.dac.groupd.tests.integration;


import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ifpb.dac.groupd.business.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.business.service.UserServiceImpl;
import br.edu.ifpb.dac.groupd.business.service.interfaces.PasswordEncoderService;
import br.edu.ifpb.dac.groupd.model.entities.User;
import br.edu.ifpb.dac.groupd.presentation.dto.UserRequest;
import br.edu.ifpb.dac.groupd.presentation.dto.UserResponse;

@Testable
@DisplayName("User Resources Tests")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
@ActiveProfiles("test")
@Testcontainers(disabledWithoutDocker = true)
public class UserResourceTests {
	
	private final String PREFIX = "http://localhost:8080/api/users";
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private PasswordEncoderService passEncoder;
	
	@Autowired
	private UserServiceImpl userService;
	
	private UserRequest dto = new UserRequest();
	@BeforeEach
	void setUp() {
		dto = new UserRequest();
	}
	@AfterEach
	void tearDown() {
		deleteUser(dto.getEmail());
	}
	
	@Test
	@DisplayName("Register Invalid User")
	@Order(1)
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

		assertThat(response, containsString("name"));
		assertThat(response, containsString("email"));
		assertThat(response, containsString("password"));
		
		assertThrows(UserNotFoundException.class, ()->userService.findByEmail(dto.getEmail()));
		
	}
	@Test
	@DisplayName("Register Valid User")
	@Order(2)
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
		UserResponse userDto= assertDoesNotThrow(()->mapper.readValue(response,UserResponse.class));
		
		User user = assertDoesNotThrow(()->{
			return userService.findByEmail(dto.getEmail());
		});
		
		equalsDto(dto, userDto);
		equalsUser(dto, user);
	}
	
	@Test
	@DisplayName("Register user with email already registered")
	@Order(3)
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
	@DisplayName("Update user email with email already registered")
	@Order(4)
	void testPutEmailAlreadyUsed() {
		dto = validUser();
		
		UserRequest dtoNewUser = validUser();
		
		String differentEmail = "ffc@protonmail.com";
		String differentPassword = "filipefariasc";
		
		dtoNewUser.setEmail(differentEmail);
		dtoNewUser.setPassword(differentPassword);
		
		UserResponse user = assertDoesNotThrow(()->
			{
				return mapper.readValue(mockMvc.perform(
					post(PREFIX)
						.contentType("application/json")
						.content(mapper.writeValueAsString(dto))
				).andReturn().getResponse().getContentAsString(), UserResponse.class);
			}
		);
		UserResponse newUser = assertDoesNotThrow(()->
			{
				return mapper.readValue(mockMvc.perform(
					post(PREFIX)
						.contentType("application/json")
						.content(mapper.writeValueAsString(dtoNewUser))
				).andReturn().getResponse().getContentAsString(), UserResponse.class);
			}
		);
		
		dto = validUser();
		
		String response = assertDoesNotThrow(()->{
			return
			mockMvc.perform(
				put(PREFIX)
				.with(user(newUser.getId().toString()).password(differentPassword))
					.contentType("application/json")
					.content(mapper.writeValueAsString(dto))
			)
			.andExpect(status().isConflict())
			.andReturn().getResponse().getContentAsString();
		});
		
		assertThat(response, containsString("email"));
		assertThat(response, containsString(dto.getEmail()));
		
		deleteUser(differentEmail);
	}
	@Test
	@DisplayName("Update user email with a valid email")
	@Order(5)
	void testPutEmailValid() {
		dto = validUser();
		
		UserRequest dtoNewUser = validUser();
		
		String differentEmail = "ffc@protonmail.com";
		String differentPassword = "filipefariasc";
		
		dtoNewUser.setEmail(differentEmail);
		dtoNewUser.setPassword(differentPassword);
		
		UserResponse user = assertDoesNotThrow(()->{
			return  mapper.readValue(mockMvc.perform(
				post(PREFIX)
					.contentType("application/json")
					.content(mapper.writeValueAsString(dto))
			).andReturn().getResponse().getContentAsString(), UserResponse.class);
		});
		
		UserResponse newUser = assertDoesNotThrow(()->{
			return  mapper.readValue(mockMvc.perform(
				post(PREFIX)
					.contentType("application/json")
					.content(mapper.writeValueAsString(dtoNewUser))
			).andReturn().getResponse().getContentAsString(), UserResponse.class);
		});
		
		String newEmail = "ffc@pm.me";
		dtoNewUser.setEmail(newEmail);
		
		String response = assertDoesNotThrow(()->{
			return
			mockMvc.perform(
				put(PREFIX)
				.with(user(newUser.getId().toString()).password(differentPassword))
					.contentType("application/json")
					.content(mapper.writeValueAsString(dtoNewUser))
			)
			.andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString();
		});
		
		UserResponse userDto = assertDoesNotThrow(()->mapper.readValue(response, UserResponse.class));
		
		User updated = assertDoesNotThrow(()->{
			return userService.findByEmail(dtoNewUser.getEmail());
		});
		
		equalsDto(dto, userDto);
		equalsUser(dto, updated);
		
		deleteUser(newEmail);
	}
	
	@Test
	@DisplayName("Delete User Logged In")
	@Order(6)
	void testDeleteCurrentUser() {
		dto = validUser();
		
		String responseJson =assertDoesNotThrow(()->
			mockMvc.perform(
				post(PREFIX)
					.contentType("application/json")
					.content(mapper.writeValueAsString(dto))
			).andReturn().getResponse().getContentAsString()
		);
		
		UserResponse responseDto = assertDoesNotThrow(()-> mapper.readValue(responseJson, UserResponse.class));
		
		String response =assertDoesNotThrow(
			()->{return mockMvc.perform(
				delete(PREFIX+"/user")
					.with(user(responseDto.getId().toString()).password(dto.getPassword()))
					.contentType("application/json")
					.content(mapper.writeValueAsString(dto))
				).andExpect(status().isOk())
					.andReturn().getResponse().getContentAsString();
			}
		);
		
		assertEquals("Usuário deletado!", response);
		
		assertThrows(UserNotFoundException.class, ()->{userService.findByEmail(dto.getEmail());});
		
	}
	
	void deleteUser(String email) {
		try {
			userService.deleteByUsername(email);
		} catch (UserNotFoundException e) {
		}
	}
	
	UserRequest validUser() {
		UserRequest dto = new UserRequest();
		dto.setEmail("filipefariasc@protonmail.com");
		dto.setName("Filipe Farias");
		dto.setPassword("ffc20221*");
		
		return dto;
	}
	private void equalsDto(UserRequest dto, UserResponse userDto) {
		assertAll(
				()->dto.getName().equals(userDto.getName()),
				()->dto.getEmail().equals(userDto.getEmail())
		);
	}
	private void equalsUser(UserRequest dto, User user) {
		
		assertAll(
			()->dto.getName().equals(user.getName()),
			()->dto.getEmail().equals(user.getName()),
			()->passEncoder.matches(dto.getPassword(), user.getPassword())
		);
	}
}
