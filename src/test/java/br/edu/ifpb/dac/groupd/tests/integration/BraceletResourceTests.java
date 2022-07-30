package br.edu.ifpb.dac.groupd.tests.integration;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ifpb.dac.groupd.business.exception.BraceletNotFoundException;
import br.edu.ifpb.dac.groupd.business.service.BraceletService;
import br.edu.ifpb.dac.groupd.business.service.UserServiceImpl;
import br.edu.ifpb.dac.groupd.model.entities.Bracelet;
import br.edu.ifpb.dac.groupd.model.entities.User;
import br.edu.ifpb.dac.groupd.presentation.controller.exceptionhandler.errors.AttributeErrorData;
import br.edu.ifpb.dac.groupd.presentation.controller.exceptionhandler.errors.ErrorResponse;
import br.edu.ifpb.dac.groupd.presentation.dto.BraceletRequest;
import br.edu.ifpb.dac.groupd.presentation.dto.BraceletResponse;
import br.edu.ifpb.dac.groupd.presentation.dto.UserRequest;

@Testable
@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@DisplayName("Bracelet Resources Tests")
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
class BraceletResourceTests {
	
	private final String BRACELETS_PREFIX = "http://localhost:8080/api/bracelets/";
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private BraceletService braceletService;
	
	
	private User user;
	
	@BeforeEach
	void setUp() throws Exception {
		UserRequest userPostDto = validUser();
		user = userService.create(userPostDto);
		
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}
	
	private UserRequest validUser() {
		UserRequest userPostDto = new UserRequest();
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
	void testRegisterBraceletInvalid(BraceletRequest braceletPostDto) {
		
		String response = assertDoesNotThrow(
			()->mockMvc.perform(
				post(BRACELETS_PREFIX)
					.with(user(user.getId().toString()).password(user.getPassword()))
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
					return braceletService.getAllBracelets(user.getId(), PageRequest.of(0, 5, Sort.by("id")));
				}
			);
		assertTrue(bracelets.isEmpty());
	}
	
	@ParameterizedTest(name="{index} -> {0}")
	@DisplayName("Register Valid Bracelet")
	@MethodSource("provideValidBraceletName")
	@Order(2)
	void testRegisterBraceletValid(BraceletRequest braceletPostDto) {
		
		String response = assertDoesNotThrow(
			()->mockMvc.perform(
				post(BRACELETS_PREFIX)
					.with(user(user.getId().toString()).password(user.getPassword()))
					.contentType("application/json")
					.content(mapper.writeValueAsString(braceletPostDto))
				).andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString()
		);
		
		BraceletResponse braceletDto = assertDoesNotThrow(()->mapper.readValue(response, BraceletResponse.class));
		
		Bracelet bracelet = assertDoesNotThrow(()->braceletService.findByBraceletId(user.getId(), braceletDto.getId()));
		
		equals(braceletPostDto, braceletDto, bracelet);
	}
	
	@Test
	@DisplayName("Get Bracelet Id Doesn't Exist")
	@Order(3)
	void testGetBraceletWithInvalidId() {
		BraceletRequest braceletPostDto = validBracelet();
		
		String response = assertDoesNotThrow(
			()->mockMvc.perform(
				get(BRACELETS_PREFIX+0)
					.with(user(user.getId().toString()).password(user.getPassword()))
					.contentType("application/json")
					.content(mapper.writeValueAsString(braceletPostDto))
				).andExpect(status().isNotFound())
				.andReturn().getResponse().getContentAsString(UTF_8)
		);
		
		ErrorResponse errors = assertDoesNotThrow(()->mapper.readValue(response, ErrorResponse.class));
		assertThat(errors.getErrors()).hasSize(1);
		assertThat(errors.getErrors().get(0).getMessageUser()).isEqualTo(new BraceletNotFoundException(0L).getMessage());
	}
	@Test
	@DisplayName("Get Bracelet Id Exist")
	@Order(4)
	void testGetBraceletWithValidId() {
		BraceletRequest braceletPostDto = validBracelet();
		
		String responsePost = assertDoesNotThrow(
			()->mockMvc.perform(
				post(BRACELETS_PREFIX)
					.with(user(user.getId().toString()).password(user.getPassword()))
					.contentType("application/json")
					.content(mapper.writeValueAsString(braceletPostDto))
				).andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString()
		);
		
		BraceletResponse braceletDtoPost = assertDoesNotThrow(()->mapper.readValue(responsePost, BraceletResponse.class));
		
		String responseGet = assertDoesNotThrow(
			()->mockMvc.perform(
				get(BRACELETS_PREFIX+braceletDtoPost.getId())
					.with(user(user.getId().toString()).password(user.getPassword()))
				).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString()
		);
		
		BraceletResponse braceletDtoGet = assertDoesNotThrow(()->mapper.readValue(responseGet, BraceletResponse.class));
		
		Bracelet bracelet = assertDoesNotThrow(()->braceletService.findByBraceletId(user.getId(), braceletDtoPost.getId()));
		
		assertThat(braceletDtoPost, is(equalTo(braceletDtoGet)));
		
		equals(braceletPostDto, braceletDtoPost, bracelet);
		equals(braceletPostDto, braceletDtoGet, bracelet);
	}
	
	
	
	@ParameterizedTest(name="{index} -> {0}")
	@DisplayName("Update Invalid Bracelet")
	@MethodSource("provideInvalidBraceletName")
	@Order(3)
	void testUpdateInvalidBracelet(BraceletRequest braceletPostDto) {
		
		BraceletRequest validBracelet = validBracelet();
		
		String responsePost = assertDoesNotThrow(
			()->mockMvc.perform(
				post(BRACELETS_PREFIX)
					.with(user(user.getId().toString()).password(user.getPassword()))
					.contentType("application/json")
					.content(mapper.writeValueAsString(validBracelet))
				).andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString()
		);
		
		BraceletResponse dto = assertDoesNotThrow(()->mapper.readValue(responsePost, BraceletResponse.class));
		
		String responsePut = assertDoesNotThrow(
			()->mockMvc.perform(
				put(BRACELETS_PREFIX+dto.getId())
					.with(user(user.getId().toString()).password(user.getPassword()))
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
	void updateValidBracelet(BraceletRequest braceletPostDto) {

		BraceletRequest validBracelet = validBracelet();
		
		String responsePost = assertDoesNotThrow(
			()->mockMvc.perform(
				post(BRACELETS_PREFIX)
					.with(user(user.getId().toString()).password(user.getPassword()))
					.contentType("application/json")
					.content(mapper.writeValueAsString(validBracelet))
				).andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString()
		);
		
		BraceletResponse dto = assertDoesNotThrow(()->mapper.readValue(responsePost, BraceletResponse.class));
		
		String responsePut = assertDoesNotThrow(
			()->mockMvc.perform(
				put(BRACELETS_PREFIX+dto.getId())
					.with(user(user.getId().toString()).password(user.getPassword()))
					.contentType("application/json")
					.content(mapper.writeValueAsString(braceletPostDto))
				).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString()
		);
		
		BraceletResponse braceletDto = assertDoesNotThrow(()->mapper.readValue(responsePut, BraceletResponse.class));
		
		Bracelet bracelet = assertDoesNotThrow(()->braceletService.findByBraceletId(user.getId(), braceletDto.getId()));
		
		equals(braceletPostDto, braceletDto, bracelet);
	}
	
	private static Stream<Arguments> provideInvalidBraceletName(){
		return Stream.of(
			Arguments.of(new BraceletRequest(null)),
			Arguments.of(new BraceletRequest("")),
			Arguments.of(new BraceletRequest("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxy"))
		);
	}
	private static Stream<Arguments> provideValidBraceletName(){
		return Stream.of(
			Arguments.of(new BraceletRequest("a")),
			Arguments.of(new BraceletRequest("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwx"))
		);
	}
	
	private BraceletRequest validBracelet() {
		return new BraceletRequest("a");
	}
	
	private void equals(BraceletRequest postDto, BraceletResponse dto, Bracelet bracelet) {
		assertAll(
			()->assertEquals(postDto.getName(),dto.getName()),
			()->assertEquals(postDto.getName(), bracelet.getName()),
			()->assertEquals(dto.getId(), bracelet.getId()),
			()->assertEquals(dto.getName(), bracelet.getName())
		);
	}
}
