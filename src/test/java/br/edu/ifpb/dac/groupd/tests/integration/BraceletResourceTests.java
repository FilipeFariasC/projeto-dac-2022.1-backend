package br.edu.ifpb.dac.groupd.tests.integration;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ifpb.dac.groupd.dto.BraceletDto;
import br.edu.ifpb.dac.groupd.dto.post.BraceletPostDto;
import br.edu.ifpb.dac.groupd.dto.post.UserPostDto;
import br.edu.ifpb.dac.groupd.exception.UserEmailInUseException;
import br.edu.ifpb.dac.groupd.exceptionhandler.errors.AttributeErrorData;
import br.edu.ifpb.dac.groupd.exceptionhandler.errors.ErrorResponse;
import br.edu.ifpb.dac.groupd.model.Bracelet;
import br.edu.ifpb.dac.groupd.model.User;
import br.edu.ifpb.dac.groupd.service.BraceletService;
import br.edu.ifpb.dac.groupd.service.UserService;

@Testable
@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@DisplayName("Bracelet Resources Tests")
@ActiveProfiles("test")
class BraceletResourceTests {
	
	private final String USER_PREFIX = "http://localhost:8080/api/users";
	
	private final String BRACELETS_PREFIX = "http://localhost:8080/api/bracelets";
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BraceletService braceletService;
	
	
	private User user;
	
	private BraceletPostDto dto;
	
	@BeforeEach
	void setUp() throws Exception {
		dto = new BraceletPostDto();
		UserPostDto userPostDto = validUser();
		user = userService.create(userPostDto);
	}
	
	private UserPostDto validUser() {
		UserPostDto userPostDto = new UserPostDto();
		userPostDto.setEmail("f@f.com");
		userPostDto.setPassword("abcdefgh");
		userPostDto.setName("Fil");
		
		return userPostDto;
	}

	@AfterEach
	void tearDown() throws Exception {
		userService.deleteByUsername(user.getEmail());
	}


	@ParameterizedTest(name="{index} -> {0}")
	@DisplayName("Register Invalid Bracelet")
	@MethodSource("provideInvalidBraceletName")	
	void testRegisterBraceletInvalid(BraceletPostDto braceletPostDto) {
		
		String response = assertDoesNotThrow(
			()->mockMvc.perform(
				post(BRACELETS_PREFIX)
					.with(user(user.getUsername()).password(user.getPassword()))
					.contentType("application/json")
					.content(mapper.writeValueAsString(braceletPostDto))
				).andExpect(status().isBadRequest())
				.andReturn().getResponse().getContentAsString()
		);
		
		ErrorResponse errorResponse = assertDoesNotThrow(()->mapper.readValue(response, ErrorResponse.class));
		
		assertTrue(
			errorResponse
				.getErrors()
				.stream()
				.anyMatch(
					(error)->{
						if(error instanceof AttributeErrorData aed) {
							return aed.getFieldName().equals("name");
						}
						return false;
					}
			)
		);
		List<Bracelet> bracelets = assertDoesNotThrow(
			()->
				{
					return braceletService.getAllBracelets(user.getEmail());
				}
			);
		assertTrue(bracelets.isEmpty());
	}
	
	@ParameterizedTest(name="{index} -> {0}")
	@DisplayName("Register Valid Bracelet")
	@MethodSource("provideValidBraceletName")	
	void testRegisterBraceletValid(BraceletPostDto braceletPostDto) {
		
		String response = assertDoesNotThrow(
			()->mockMvc.perform(
				post(BRACELETS_PREFIX)
					.with(user(user.getUsername()).password(user.getPassword()))
					.contentType("application/json")
					.content(mapper.writeValueAsString(braceletPostDto))
				).andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString()
		);
		
		BraceletDto braceletDto = assertDoesNotThrow(()->mapper.readValue(response, BraceletDto.class));
		
		Bracelet bracelet = assertDoesNotThrow(()->braceletService.findByBraceletId(user.getEmail(), braceletDto.getIdBracelet()));
		
		equals(braceletPostDto, braceletDto, bracelet);
	}
	
	//void testRegisterBraceletValid
	
	@Test
	void createUser() throws Exception{
		try {
			UserPostDto create = validUser();

			create.setEmail("fil@pm.me");
			
			userService.create(create);
		} catch (UserEmailInUseException e) {
		}
	}
	
	
	private static Stream<Arguments> provideInvalidBraceletName(){
		return Stream.of(
			Arguments.of(new BraceletPostDto(null)),
			Arguments.of(new BraceletPostDto("")),
			Arguments.of(new BraceletPostDto("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxy"))
		);
	}
	private static Stream<Arguments> provideValidBraceletName(){
		return Stream.of(
			Arguments.of(new BraceletPostDto("a")),
			Arguments.of(new BraceletPostDto("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwx"))
		);
	}
	
	private void equals(BraceletPostDto postDto, BraceletDto dto, Bracelet bracelet) {
		assertAll(
			()->postDto.getName().equals(dto.getName()),
			()->postDto.getName().equals(bracelet.getName()),
			()->dto.getIdBracelet().equals(bracelet.getId()),
			()->dto.getName().equals(bracelet.getName())
				);
	}
}
