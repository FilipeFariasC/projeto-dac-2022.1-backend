package br.edu.ifpb.dac.groupd.tests.integration;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.stream.Stream;

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

import br.edu.ifpb.dac.groupd.business.exception.BraceletNotFoundException;
import br.edu.ifpb.dac.groupd.model.entities.Coordinate;
import br.edu.ifpb.dac.groupd.presentation.controller.exceptionhandler.errors.ErrorData;
import br.edu.ifpb.dac.groupd.presentation.controller.exceptionhandler.errors.ErrorResponse;
import br.edu.ifpb.dac.groupd.presentation.dto.BraceletRequest;
import br.edu.ifpb.dac.groupd.presentation.dto.BraceletResponse;
import br.edu.ifpb.dac.groupd.presentation.dto.FenceRequest;
import br.edu.ifpb.dac.groupd.presentation.dto.FenceResponse;
import br.edu.ifpb.dac.groupd.presentation.dto.LocationRequest;
import br.edu.ifpb.dac.groupd.presentation.dto.LocationResponse;
import br.edu.ifpb.dac.groupd.presentation.dto.UserRequest;
import br.edu.ifpb.dac.groupd.presentation.dto.UserResponse;
import br.edu.ifpb.dac.groupd.tests.builder.FenceRequestBuilder;

@Testable
@DisplayName("Location Resources Tests")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
@ActiveProfiles("test")
@Testcontainers(disabledWithoutDocker = true)
class LocationResourcesTest {
	private final String PREFIX = "http://localhost:8080/api";
	private final String USER_PREFIX = PREFIX+"/users";
	private final String FENCE_PREFIX = PREFIX+"/fences";
	private final String BRACELET_PREFIX = PREFIX+"/bracelets";
	private final String LOCATION_PREFIX = PREFIX+"/locations";
	private final Locale PT_BR = new Locale("pt","BR");
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	private UserRequest userDto = validUser();
	private UserResponse user;

	private BraceletResponse bracelet;
	private BraceletRequest braceletDto = new BraceletRequest();
	
	private FenceRequest fenceDto;
	private FenceResponse fence;
	
	private LocationRequest locationDto;
	private LocationResponse location;
	
	private Coordinate invalidCoordinate() {
		return new Coordinate(-91.0, -181.0);
	}
	private Coordinate validCoordinate() {
		return new Coordinate(-90.0, -180.0);
	}
	
	private UserRequest validUser() {
		UserRequest dto = new UserRequest();
		dto.setEmail("admin@admin.com");
		dto.setName("admin");
		dto.setPassword("admin20221");
		
		return dto;
	}
	private FenceRequest validFence() {
		return new FenceRequestBuilder()
			.withName("Cerca")
			.withCoordinate(validCoordinate())
			.withRadius(1.0)
			.build();
	}
	private BraceletRequest validBracelet() {
		BraceletRequest obj = new BraceletRequest();
		
		obj.setName("abc");
		
		return obj;
	}
	
	private LocationRequest validLocation() {
		LocationRequest location = new LocationRequest();
		
		location.setBraceletId(bracelet.getId());
		location.setCoordinate(validCoordinate());
		
		return location;
	}
	
	@Autowired
	private MessageSource messageSource;
	
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
		
		String fenceResponse = mockMvc.perform(
			post(FENCE_PREFIX)
				.with(user(this.user.getId().toString()))
				.contentType("application/json")
				.content(mapper.writeValueAsString(fenceDto))
		).andReturn().getResponse().getContentAsString();
		
		this.fence = assertDoesNotThrow(()->mapper.readValue(fenceResponse, FenceResponse.class));
		
		this.locationDto = validLocation();
	}

	@AfterEach
	void tearDown() throws Exception {
		mockMvc.perform(
			delete(String.format("%s/user", USER_PREFIX))
				.with(user(this.user.getId().toString()).password(userDto.getPassword()))
		);
	}
	@Test
	void testCreateInvalidCreationTime() {
		locationDto = validLocation();
		locationDto.setCreationDate(LocalDateTime.now().plusSeconds(1));
		
		String locationResponse = assertDoesNotThrow(()->{
			return mockMvc.perform(post(LOCATION_PREFIX)
				.with(user(user.getId().toString()))
				.header("Accept-Language", "pt-BR")
				.contentType("application/json")
				.content(mapper.writeValueAsString(locationDto))
				)
			.andExpect(status().isBadRequest())
			.andReturn().getResponse().getContentAsString(UTF_8);
		});
		
		ErrorResponse errors = assertDoesNotThrow(()->mapper.readValue(locationResponse, ErrorResponse.class));
		assertThat(errors.getErrors()).hasSize(1);
		ErrorData error = errors.getErrors().get(0);
		String propertyName = messageSource.getMessage("locationRequest.creationDate", null, PT_BR);
		assertThat(error.getMessageUser()).isEqualTo(
			messageSource.getMessage("br.edu.ifpb.dac.groupd.validation.contraints.ValidTimestamp.message", 
				new String[]{propertyName}, 
				PT_BR));
		errors.getErrors().stream().forEach((n)->System.out.println(n.getMessageUser()));
	}
	
	@Test
	void testCreateInvalidCoordinate() {
		locationDto = validLocation();
		locationDto.setCoordinate(invalidCoordinate());;
		
		String locationResponse = assertDoesNotThrow(()->{
			return mockMvc.perform(post(LOCATION_PREFIX)
				.with(user(user.getId().toString()))
				.header("Accept-Language", "pt-BR")
				.contentType("application/json")
				.content(mapper.writeValueAsString(locationDto))
				)
			.andExpect(status().isBadRequest())
			.andReturn().getResponse().getContentAsString(UTF_8);
		});
		
		ErrorResponse errors = assertDoesNotThrow(()->mapper.readValue(locationResponse, ErrorResponse.class));
		assertThat(errors.getErrors()).hasSize(2);
		String latitudeName = messageSource.getMessage("coordinate.latitude", null, PT_BR);
		String longitudeName = messageSource.getMessage("coordinate.longitude", null, PT_BR);
		
		errors.getErrors().stream().forEach(error -> assertThat(error.getMessageUser())
			.isIn(Stream.of(
				formatCoordinateValidation(longitudeName, 180L),
				formatCoordinateValidation(latitudeName, 90L)
			).toList()));
	}
	
	private String formatCoordinateValidation(String property, Long value) {
		Long abs = Math.abs(value);
		return String.format("%s deve ser entre %s e %s.", property, -abs, abs);
	}
	
	@Test
	void testCreateInvalidBraceletId() {
		locationDto = validLocation();
		locationDto.setBraceletId(bracelet.getId()+1);;
		
		String locationResponse = assertDoesNotThrow(()->{
			return mockMvc.perform(post(LOCATION_PREFIX)
				.with(user(user.getId().toString()))
				.header("Accept-Language", "pt-BR")
				.contentType("application/json")
				.content(mapper.writeValueAsString(locationDto))
				)
			.andExpect(status().isNotFound())
			.andReturn().getResponse().getContentAsString(UTF_8);
		});
		
		ErrorResponse errors = assertDoesNotThrow(()->mapper.readValue(locationResponse, ErrorResponse.class));
		assertThat(errors.getErrors()).hasSize(1);
		
		ErrorData error = errors.getErrors().get(0);
		
		assertThat(error.getMessageUser())
			.isEqualTo(new BraceletNotFoundException(bracelet.getId()+1).getMessage());
	}

	@Test
	void testCreateValid() {
		String locationResponse = assertDoesNotThrow(()->{
			return mockMvc.perform(post(LOCATION_PREFIX)
				.with(user(user.getId().toString()))
				.header("Accept-Language", "pt-BR")
				.contentType("application/json")
				.content(mapper.writeValueAsString(locationDto))
				)
			.andExpect(status().isCreated())
			.andReturn().getResponse().getContentAsString(UTF_8);
		});
		
		location = assertDoesNotThrow(()-> mapper.readValue(locationResponse, LocationResponse.class));
		
		equals(locationDto, location);
	}
	
	@Test
	void createValidGenerateAlarm() {
		String registerBraceletEndpoint = String.format("%s/registerBracelet", FENCE_PREFIX);
		
		assertDoesNotThrow(()->{
			return mockMvc.perform(
			post(registerBraceletEndpoint)
				.param("fence", fence.getId().toString())
				.param("bracelet", bracelet.getId().toString())
				.header("Accept-Language", "pt-BR")
				.with(user(user.getId().toString()))
			).andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		});
		String setStatusEndpointFence = String.format("%s/%d/setStatus", FENCE_PREFIX, fence.getId());
		
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
		
		locationDto.setCoordinate(new Coordinate(-89.99991, -179.99991));
		
		testCreateValid();
		
		assertNotNull(location.getAlarm());
	}
	
	private void equals(LocationRequest request, LocationResponse response) {
		assertAll(()-> assertEquals(request.getBraceletId(), response.getBracelet().getId()),
			()->assertEquals(request.getCoordinate(),response.getCoordinate()),
			()->{
				if(request.getCreationDate() != null) {
					assertEquals(request.getCreationDate(), response.getCreationDate());
				}
			}
		);
	}

}
