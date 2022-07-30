package br.edu.ifpb.dac.groupd.tests.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ifpb.dac.groupd.business.exception.FenceEmptyException;
import br.edu.ifpb.dac.groupd.model.entities.Coordinate;
import br.edu.ifpb.dac.groupd.presentation.controller.exceptionhandler.errors.ErrorResponse;
import br.edu.ifpb.dac.groupd.presentation.dto.BraceletRequest;
import br.edu.ifpb.dac.groupd.presentation.dto.BraceletResponse;
import br.edu.ifpb.dac.groupd.presentation.dto.FenceRequest;
import br.edu.ifpb.dac.groupd.presentation.dto.FenceResponse;
import br.edu.ifpb.dac.groupd.presentation.dto.UserRequest;
import br.edu.ifpb.dac.groupd.presentation.dto.UserResponse;
import br.edu.ifpb.dac.groupd.tests.builder.FenceRequestBuilder;

@Testable
@DisplayName("Fence Resources Tests")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
@ActiveProfiles("test")
@Testcontainers(disabledWithoutDocker = true)
class FenceResourcesTests {
	private final String PREFIX = "http://localhost:8080/api";
	private final String USER_PREFIX = PREFIX+"/users";
	private final String FENCE_PREFIX = USER_PREFIX+"/fences";
	private final String BRACELET_PREFIX = USER_PREFIX+"/bracelets";
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	private UserRequest userDto = validUser();
	
	private FenceRequest fenceDto;
	
	private UserResponse user;
	
	private BraceletResponse bracelet;
	
	private BraceletRequest braceletDto = validBracelet();
	
	@Autowired
	private MessageSource messageSource;
	
	private UserRequest validUser() {
		UserRequest dto = new UserRequest();
		dto.setEmail("admin@admin.com");
		dto.setName("admin");
		dto.setPassword("admin20221");
		
		return dto;
	}
	
	private BraceletRequest validBracelet() {
		BraceletRequest obj = new BraceletRequest();
		
		obj.setName("abc");
		
		return obj;
	}

	@BeforeEach
	void setUp() throws Exception {
		
		String userResponse = mockMvc.perform(
			post(USER_PREFIX)
				.contentType("application/json")
				.content(mapper.writeValueAsString(userDto))
		).andReturn().getResponse().getContentAsString();
		
		this.user = mapper.readValue(userResponse, UserResponse.class);
		
		fenceDto = validFence();
		braceletDto = validBracelet();
		
		String braceletResponse = mockMvc.perform(
			post(BRACELET_PREFIX)
				.with(user(this.user.getId().toString()))
				.contentType("application/json")
				.content(mapper.writeValueAsString(braceletDto))
		).andReturn().getResponse().getContentAsString();
		
		this.bracelet = assertDoesNotThrow(()->mapper.readValue(braceletResponse, BraceletResponse.class));
	}
	
	@AfterEach
	void tearDown() throws Exception {
		mockMvc.perform(
			delete(String.format("%s/%s",USER_PREFIX, "user"))
				.with(user(user.getId().toString()))
		);
	}
	
	private FenceRequest invalidFence() {
		return new FenceRequestBuilder()
			.withName("")
			.withCoordinate(new Coordinate(-91.0, -181.0))
			.withRadius(0.0)
			.build();
	}
	
	private FenceRequest validFence() {
		return new FenceRequestBuilder()
			.withName("Cerca")
			.withCoordinate(new Coordinate(-90.0, -180.0))
			.withRadius(1.0)
			.build();
	}
	// CREATE
	@Test
	void testCreateFenceInvalid() {
		fenceDto = invalidFence();
		String response = assertDoesNotThrow(()->{
			return mockMvc.perform(
			post(FENCE_PREFIX)
				.header("Accept-Language", "pt-BR")
				.with(user(user.getId().toString()))
				.contentType("application/json")
				.content(mapper.writeValueAsString(fenceDto))
			).andExpect(status().isBadRequest())
			.andReturn().getResponse().getContentAsString();
		});
		
		ErrorResponse errorResponse = assertDoesNotThrow(()->{
			return mapper.readValue(response, ErrorResponse.class);
		});
		
		assertThat(errorResponse.getErrors()).isNotEmpty();
	}
	@Test
	void testCreateFenceInvalidStartTime() {
		LocalTime agora = LocalTime.now();
		
		fenceDto.setStartTime(agora.plusMinutes(1l));
		fenceDto.setFinishTime(agora);
		String response = assertDoesNotThrow(()->{
			return mockMvc.perform(
			post(FENCE_PREFIX)
				.header("Accept-Language", "pt-BR")
				.with(user(user.getId().toString()))
				.contentType("application/json")
				.content(mapper.writeValueAsString(fenceDto))
			).andExpect(status().isBadRequest())
			.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		});
		
		ErrorResponse errorResponse = assertDoesNotThrow(()->mapper.readValue(response, ErrorResponse.class));
		
		assertThat(errorResponse.getErrors()).isNotEmpty();
		
		assertThat(errorResponse.getErrors().stream().findFirst().get().getMessageUser())
			.isEqualTo(messageSource.getMessage("br.edu.ifpb.dac.groupd.validation.contraints.ValidTimer.message", null, new Locale("pt","BR")));
	}
	
	@Test
	void testCreateFenceValid() {
		LocalTime agora = LocalTime.of(12,0); // 12:00
		fenceDto.setStartTime(agora);
		fenceDto.setFinishTime(agora.plusHours(2l).plusMinutes(30l)); // 14:30
		
		String response = assertDoesNotThrow(()->{
			return mockMvc.perform(
			post(FENCE_PREFIX)
				.header("Accept-Language", "pt-BR")
				.with(user(user.getId().toString()))
				.contentType("application/json")
				.content(mapper.writeValueAsString(fenceDto))
			).andExpect(status().isCreated())
			.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		});
		
		FenceResponse fenceResponse = assertDoesNotThrow(()->mapper.readValue(response, FenceResponse.class));
		
		equalFence(fenceDto, fenceResponse);
	}
	
	void equalFence(FenceRequest fenceDto, FenceResponse fenceResponse) {
		assertAll(
				()->assertEquals(fenceDto.getName(), fenceResponse.getName()),
				()->assertEquals(fenceDto.getRadius(), fenceResponse.getRadius()),
				()->assertEquals(fenceDto.getCoordinate(), fenceResponse.getCoordinate()),
				()->assertEquals(fenceDto.getStartTime(), fenceResponse.getStartTime()),
				()->assertEquals(fenceDto.getFinishTime(), fenceResponse.getFinishTime()));
	}
	
	@Test
	void testSetStatusTrueNoBracelets() {
		String response = assertDoesNotThrow(()->{
			return mockMvc.perform(
			post(FENCE_PREFIX)
				.header("Accept-Language", "pt-BR")
				.with(user(user.getId().toString()))
				.contentType("application/json")
				.content(mapper.writeValueAsString(fenceDto))
			).andExpect(status().isCreated())
			.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		});
		
		FenceResponse fenceResponse = assertDoesNotThrow(()->mapper.readValue(response, FenceResponse.class));
		String endpoint = String.format("%s/%d/setStatus", FENCE_PREFIX, fenceResponse.getId());
		String responseErrors = assertDoesNotThrow(()->{
			return mockMvc.perform(
			patch(endpoint)
				.param("active","true")
				.header("Accept-Language", "pt-BR")
				.with(user(user.getId().toString()))
				.contentType("application/json")
			)
			.andDo(print())
			.andExpect(status().isNotFound())
			.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		});
		
		ErrorResponse errors = assertDoesNotThrow(()->mapper.readValue(responseErrors, ErrorResponse.class));
		
		assertThat(errors.getErrors()).isNotEmpty();
		assertThat(errors.getErrors().stream().findFirst().get().getMessageUser())
			.isEqualTo(new FenceEmptyException(fenceResponse.getId()).getMessage());
	}
	
	@Test
	void testInvalidUpdate() {
		String response = assertDoesNotThrow(()->{
			return mockMvc.perform(
			post(FENCE_PREFIX)
				.header("Accept-Language", "pt-BR")
				.with(user(user.getId().toString()))
				.contentType("application/json")
				.content(mapper.writeValueAsString(fenceDto))
			).andExpect(status().isCreated())
			.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		});
		
		FenceResponse fenceResponse = assertDoesNotThrow(()->mapper.readValue(response, FenceResponse.class));
		String endpoint = String.format("%s/%d", FENCE_PREFIX, fenceResponse.getId());
		
		fenceDto = invalidFence();
		
		assertDoesNotThrow(()->{
			return mockMvc.perform(
			put(endpoint)
				.param("active","true")
				.header("Accept-Language", "pt-BR")
				.with(user(user.getId().toString()))
				.contentType("application/json")
				.content(mapper.writeValueAsString(fenceDto))
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		});
	}
	
	@Test
	void testValidUpdate() {
		String response = assertDoesNotThrow(()->{
			return mockMvc.perform(
			post(FENCE_PREFIX)
				.header("Accept-Language", "pt-BR")
				.with(user(user.getId().toString()))
				.contentType("application/json")
				.content(mapper.writeValueAsString(fenceDto))
			).andExpect(status().isCreated())
			.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		});
		
		FenceResponse fenceResponse = assertDoesNotThrow(()->mapper.readValue(response, FenceResponse.class));
		String endpoint = String.format("%s/%d", FENCE_PREFIX, fenceResponse.getId());
		
		LocalTime manha = LocalTime.of(8, 30);
		
		fenceDto = new FenceRequestBuilder()
			.withName("Outra cerca")
			.withCoordinate(new Coordinate(90.0,180.0))
			.withRadius(12.0)
			.withStartTime(manha)
			.withFinishTime(manha.plusHours(1).plusMinutes(45))
			.build();
		
		String updatedResponse = assertDoesNotThrow(()->{
			return mockMvc.perform(
			put(endpoint)
				.param("active","true")
				.header("Accept-Language", "pt-BR")
				.with(user(user.getId().toString()))
				.contentType("application/json")
				.content(mapper.writeValueAsString(fenceDto))
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		});
		
		FenceResponse updatedFence = assertDoesNotThrow(()->mapper.readValue(updatedResponse, FenceResponse.class));
		
		equalFence(fenceDto, updatedFence);
	}
	@Test
	void testCanMonitorBracelet() {
		String response = assertDoesNotThrow(()->{
			return mockMvc.perform(
			post(FENCE_PREFIX)
				.header("Accept-Language", "pt-BR")
				.with(user(user.getId().toString()))
				.contentType("application/json")
				.content(mapper.writeValueAsString(fenceDto))
			).andExpect(status().isCreated())
			.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		});
		
		FenceResponse fenceResponse = assertDoesNotThrow(()->mapper.readValue(response, FenceResponse.class));
		
		String otherResponse = assertDoesNotThrow(()->{
			return mockMvc.perform(
			post(FENCE_PREFIX)
				.header("Accept-Language", "pt-BR")
				.with(user(user.getId().toString()))
				.contentType("application/json")
				.content(mapper.writeValueAsString(fenceDto))
			).andExpect(status().isCreated())
			.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		});
		
		FenceResponse otherFenceResponse = assertDoesNotThrow(()->mapper.readValue(otherResponse, FenceResponse.class));
		
		String registerBraceletEndpoint = String.format("%s/registerBracelet", FENCE_PREFIX);
		
		assertDoesNotThrow(()->{
			return mockMvc.perform(
			post(registerBraceletEndpoint)
				.param("fence", otherFenceResponse.getId().toString())
				.param("bracelet", bracelet.getId().toString())
				.header("Accept-Language", "pt-BR")
				.with(user(user.getId().toString()))
			).andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		});
		
		assertDoesNotThrow(()->{
			return mockMvc.perform(
			post(registerBraceletEndpoint)
				.param("fence", fenceResponse.getId().toString())
				.param("bracelet", bracelet.getId().toString())
				.header("Accept-Language", "pt-BR")
				.with(user(user.getId().toString()))
			).andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		});
		
		String setStatusEndpointFence = String.format("%s/%d/setStatus", FENCE_PREFIX, fenceResponse.getId());
		
		assertDoesNotThrow(()->{
			return mockMvc.perform(
			patch(setStatusEndpointFence)
				.param("active","true")
				.header("Accept-Language", "pt-BR")
				.with(user(user.getId().toString()))
				.contentType("application/json")
				.content(mapper.writeValueAsString(fenceDto))
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		});
	}
	
	@Test
	void testNoBraceletAvailable() {
		String response = assertDoesNotThrow(()->{
			return mockMvc.perform(
			post(FENCE_PREFIX)
				.header("Accept-Language", "pt-BR")
				.with(user(user.getId().toString()))
				.contentType("application/json")
				.content(mapper.writeValueAsString(fenceDto))
			).andExpect(status().isCreated())
			.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		});
		
		FenceResponse fenceResponse = assertDoesNotThrow(()->mapper.readValue(response, FenceResponse.class));
		
		String otherResponse = assertDoesNotThrow(()->{
			return mockMvc.perform(
			post(FENCE_PREFIX)
				.header("Accept-Language", "pt-BR")
				.with(user(user.getId().toString()))
				.contentType("application/json")
				.content(mapper.writeValueAsString(fenceDto))
			).andExpect(status().isCreated())
			.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		});
		
		FenceResponse otherFenceResponse = assertDoesNotThrow(()->mapper.readValue(otherResponse, FenceResponse.class));
		
		String registerBraceletEndpoint = String.format("%s/registerBracelet", FENCE_PREFIX);
		
		assertDoesNotThrow(()->{
			return mockMvc.perform(
			post(registerBraceletEndpoint)
				.param("fence", otherFenceResponse.getId().toString())
				.param("bracelet", bracelet.getId().toString())
				.header("Accept-Language", "pt-BR")
				.with(user(user.getId().toString()))
			).andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		});
		
		assertDoesNotThrow(()->{
			return mockMvc.perform(
			post(registerBraceletEndpoint)
				.param("fence", fenceResponse.getId().toString())
				.param("bracelet", bracelet.getId().toString())
				.header("Accept-Language", "pt-BR")
				.with(user(user.getId().toString()))
			).andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		});
		
		String setStatusEndpointOtherFence = String.format("%s/%d/setStatus", FENCE_PREFIX, otherFenceResponse.getId());
		
		assertDoesNotThrow(()->{
			return mockMvc.perform(
			patch(setStatusEndpointOtherFence)
				.param("active","true")
				.header("Accept-Language", "pt-BR")
				.with(user(user.getId().toString()))
				.contentType("application/json")
				.content(mapper.writeValueAsString(fenceDto))
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		});
		
		String setStatusEndpointFence = String.format("%s/%d/setStatus", FENCE_PREFIX, fenceResponse.getId());
		
		assertDoesNotThrow(()->{
			return mockMvc.perform(
			patch(setStatusEndpointFence)
				.param("active","true")
				.header("Accept-Language", "pt-BR")
				.with(user(user.getId().toString()))
				.contentType("application/json")
				.content(mapper.writeValueAsString(fenceDto))
			)
			.andDo(print())
			.andExpect(status().isConflict())
			.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		});
	}
	
}
