package br.edu.ifpb.dac.groupd.tests.integration;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ifpb.dac.groupd.dto.BraceletDto;
import br.edu.ifpb.dac.groupd.dto.post.BraceletPostDto;
import br.edu.ifpb.dac.groupd.dto.post.UserPostDto;
import br.edu.ifpb.dac.groupd.exception.BraceletNotFoundException;
import br.edu.ifpb.dac.groupd.exceptionhandler.errors.AttributeErrorData;
import br.edu.ifpb.dac.groupd.exceptionhandler.errors.ErrorResponse;
import br.edu.ifpb.dac.groupd.model.Bracelet;
import br.edu.ifpb.dac.groupd.model.User;
import br.edu.ifpb.dac.groupd.repository.BraceletRepository;
import br.edu.ifpb.dac.groupd.service.BraceletService;
import br.edu.ifpb.dac.groupd.service.UserService;

@Testable
@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@DisplayName("Bracelet Resources Tests")
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
class BraceletResourceTests {
	
	private final String USER_PREFIX = "http://localhost:8080/api/users/bracelets/";
	
	private final String BRACELETS_PREFIX = "http://localhost:8080/api/bracelets/";
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BraceletRepository braceletRepo;
	
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
	@Order(1)
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
		Page<Bracelet> bracelets = assertDoesNotThrow(
			()->
				{
					return braceletService.getAllBracelets(user.getEmail(), PageRequest.of(0, 5, Sort.by("id")));
				}
			);
		assertTrue(bracelets.isEmpty());
	}
	
	@ParameterizedTest(name="{index} -> {0}")
	@DisplayName("Register Valid Bracelet")
	@MethodSource("provideValidBraceletName")
	@Order(2)
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
		
		Bracelet bracelet = assertDoesNotThrow(()->braceletService.findByBraceletId(user.getEmail(), braceletDto.getId()));
		
		equals(braceletPostDto, braceletDto, bracelet);
	}
	
	@Test
	@DisplayName("Get Bracelet Id Doesn't Exist")
	@Order(3)
	void testGetBraceletWithInvalidId() {
		BraceletPostDto braceletPostDto = validBracelet();
		
		String response = assertDoesNotThrow(
			()->mockMvc.perform(
				get(BRACELETS_PREFIX+0)
					.with(user(user.getUsername()).password(user.getPassword()))
					.contentType("application/json")
					.content(mapper.writeValueAsString(braceletPostDto))
				).andExpect(status().isNotFound())
				.andReturn().getResponse().getContentAsString()
		);
		
		assertThat(response, containsString(new BraceletNotFoundException(0L).getMessage()));
	}
	@Test
	@DisplayName("Get Bracelet Id Exist")
	@Order(4)
	void testGetBraceletWithValidId() {
		BraceletPostDto braceletPostDto = validBracelet();
		
		String responsePost = assertDoesNotThrow(
			()->mockMvc.perform(
				post(BRACELETS_PREFIX)
					.with(user(user.getUsername()).password(user.getPassword()))
					.contentType("application/json")
					.content(mapper.writeValueAsString(braceletPostDto))
				).andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString()
		);
		
		BraceletDto braceletDtoPost = assertDoesNotThrow(()->mapper.readValue(responsePost, BraceletDto.class));
		
		String responseGet = assertDoesNotThrow(
			()->mockMvc.perform(
				get(BRACELETS_PREFIX+braceletDtoPost.getId())
					.with(user(user.getUsername()).password(user.getPassword()))
				).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString()
		);
		
		BraceletDto braceletDtoGet = assertDoesNotThrow(()->mapper.readValue(responseGet, BraceletDto.class));
		
		Bracelet bracelet = assertDoesNotThrow(()->braceletService.findByBraceletId(user.getUsername(), braceletDtoPost.getId()));
		
		assertThat(braceletDtoPost, is(equalTo(braceletDtoGet)));
		
		equals(braceletPostDto, braceletDtoPost, bracelet);
		equals(braceletPostDto, braceletDtoGet, bracelet);
	}
	
	
	
	@ParameterizedTest(name="{index} -> {0}")
	@DisplayName("Update Invalid Bracelet")
	@MethodSource("provideInvalidBraceletName")
	@Order(3)
	void testUpdateInvalidBracelet(BraceletPostDto braceletPostDto) {
		
		BraceletPostDto validBracelet = validBracelet();
		
		String responsePost = assertDoesNotThrow(
			()->mockMvc.perform(
				post(BRACELETS_PREFIX)
					.with(user(user.getUsername()).password(user.getPassword()))
					.contentType("application/json")
					.content(mapper.writeValueAsString(validBracelet))
				).andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString()
		);
		
		BraceletDto dto = assertDoesNotThrow(()->mapper.readValue(responsePost, BraceletDto.class));
		
		String responsePut = assertDoesNotThrow(
			()->mockMvc.perform(
				put(BRACELETS_PREFIX+dto.getId())
					.with(user(user.getUsername()).password(user.getPassword()))
					.contentType("application/json")
					.content(mapper.writeValueAsString(braceletPostDto))
				).andExpect(status().isBadRequest())
				.andReturn().getResponse().getContentAsString()
		);
		
		ErrorResponse errorResponse = assertDoesNotThrow(()->mapper.readValue(responsePut, ErrorResponse.class));
		
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
	}
	
	@ParameterizedTest(name="{index} -> {0}")
	@DisplayName("Update Valid Bracelet")
	@MethodSource("provideValidBraceletName")
	@Order(4)
	void updateValidBracelet(BraceletPostDto braceletPostDto) {

		BraceletPostDto validBracelet = validBracelet();
		
		String responsePost = assertDoesNotThrow(
			()->mockMvc.perform(
				post(BRACELETS_PREFIX)
					.with(user(user.getUsername()).password(user.getPassword()))
					.contentType("application/json")
					.content(mapper.writeValueAsString(validBracelet))
				).andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString()
		);
		
		BraceletDto dto = assertDoesNotThrow(()->mapper.readValue(responsePost, BraceletDto.class));
		
		String responsePut = assertDoesNotThrow(
			()->mockMvc.perform(
				put(BRACELETS_PREFIX+dto.getId())
					.with(user(user.getUsername()).password(user.getPassword()))
					.contentType("application/json")
					.content(mapper.writeValueAsString(braceletPostDto))
				).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString()
		);
		
		BraceletDto braceletDto = assertDoesNotThrow(()->mapper.readValue(responsePut, BraceletDto.class));
		
		Bracelet bracelet = assertDoesNotThrow(()->braceletService.findByBraceletId(user.getEmail(), braceletDto.getId()));
		
		equals(braceletPostDto, braceletDto, bracelet);
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
	
	private BraceletPostDto validBracelet() {
		return new BraceletPostDto("a");
	}
	
	private void equals(BraceletPostDto postDto, BraceletDto dto, Bracelet bracelet) {
		assertAll(
			()->postDto.getName().equals(dto.getName()),
			()->postDto.getName().equals(bracelet.getName()),
			()->dto.getId().equals(bracelet.getId()),
			()->dto.getName().equals(bracelet.getName())
		);
	}
}
