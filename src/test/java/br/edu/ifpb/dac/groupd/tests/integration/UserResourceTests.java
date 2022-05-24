package br.edu.ifpb.dac.groupd.tests.integration;


import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ifpb.dac.groupd.dto.UserDto;
import br.edu.ifpb.dac.groupd.dto.post.UserPostDto;
import br.edu.ifpb.dac.groupd.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.model.User;
import br.edu.ifpb.dac.groupd.service.UserService;

@Testable
@DisplayName("User Resources Tests")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
@ActiveProfiles("test")
public class UserResourceTests {
	
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
		UserDto userDto= assertDoesNotThrow(()->mapper.readValue(response,UserDto.class));
		
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
		
		dto = validUser();
		
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
		
		assertThat(response, containsString("email"));
		assertThat(response, containsString(dto.getEmail()));
		
		deleteUser(differentEmail);
	}
	@Test
	@DisplayName("Update user email with a valid email")
	@Order(5)
	void testPutEmailValid() {
		dto = validUser();
		
		UserPostDto dtoNewUser = validUser();
		
		String differentEmail = "ffc@protonmail.com";
		String differentPassword = "filipefariasc";
		
		dtoNewUser.setEmail(differentEmail);
		dtoNewUser.setPassword(differentPassword);
		
		assertDoesNotThrow(
			()->
			{
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
			
			}
		);
		
		String newEmail = "ffc@pm.me";
		dtoNewUser.setEmail(newEmail);
		
		String response = assertDoesNotThrow(()->{
			return
			mockMvc.perform(
				put(PREFIX)
				.with(user(differentEmail).password(differentPassword))
					.contentType("application/json")
					.content(mapper.writeValueAsString(dtoNewUser))
			)
			.andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString();
		});
		
		UserDto userDto = assertDoesNotThrow(()->mapper.readValue(response, UserDto.class));
		
		User user = assertDoesNotThrow(()->{
			return userService.findByEmail(dtoNewUser.getEmail());
		});
		
		equalsDto(dto, userDto);
		equalsUser(dto, user);
		
		deleteUser(newEmail);
	}
	
	@Test
	@DisplayName("Delete User Logged In")
	@Order(6)
	void testDeleteCurrentUser() {
		dto = validUser();
		
		assertDoesNotThrow(()->
			mockMvc.perform(
				post(PREFIX)
					.contentType("application/json")
					.content(mapper.writeValueAsString(dto))
			));
		
		String response =assertDoesNotThrow(
			()->{return mockMvc.perform(
				delete(PREFIX)
					.with(user(dto.getEmail()).password(dto.getPassword()))
					.contentType("application/json")
					.content(mapper.writeValueAsString(dto))
				).andExpect(status().isOk())
					.andReturn().getResponse().getContentAsString();
			}
		);
		
		assertThat(response, containsString(dto.getEmail()));
		
		assertThrows(UserNotFoundException.class, ()->{userService.findByEmail(dto.getEmail());});
		
	}
	
	void deleteUser(String email) {
		try {
			userService.deleteByUsername(email);
		} catch (UserNotFoundException e) {
		}
	}
	
	UserPostDto validUser() {
		UserPostDto dto = new UserPostDto();
		dto.setEmail("filipefariasc@protonmail.com");
		dto.setName("Filipe Farias");
		dto.setPassword("ffc20221*");
		
		return dto;
	}
	private void equalsDto(UserPostDto dto, UserDto userDto) {
		assertAll(
				()->dto.getName().equals(userDto.getName()),
				()->dto.getEmail().equals(userDto.getEmail())
		);
	}
	private void equalsUser(UserPostDto dto, User user) {
		
		assertAll(
			()->dto.getName().equals(user.getName()),
			()->dto.getEmail().equals(user.getName()),
			()->passEncoder.matches(dto.getPassword(), user.getPassword())
		);
	}
}
