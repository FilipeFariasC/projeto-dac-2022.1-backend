package br.edu.ifpb.dac.groupd.tests.integration;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ifpb.dac.groupd.dto.post.BraceletPostDto;
import br.edu.ifpb.dac.groupd.dto.post.UserPostDto;
import br.edu.ifpb.dac.groupd.model.User;
import br.edu.ifpb.dac.groupd.service.UserService;

@Testable
@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@DisplayName("Bracelet Resources Tests")
class BraceletResourceTests {
	
	private final String USER_PREFIX = "http://localhost:8080/api/users";
	
	private final String BRACELETS_PREFIX = "http://localhost:8080/api/bracelets";
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private PasswordEncoder passEncoder;
	
	@Autowired
	private UserService userService;
	
	private User user;
	
	private BraceletPostDto dto;
	
	@BeforeEach
	void setUp() throws Exception {
		dto = new BraceletPostDto();
		UserPostDto userPostDto = new UserPostDto();
		userPostDto.setEmail("f@f.com");
		userPostDto.setPassword("abcdefgh");
		userPostDto.setName("Fil");
		
		user = userService.create(userPostDto);
	}

	@AfterEach
	void tearDown() throws Exception {
		userService.deleteByUsername(user.getEmail());
	}


	@ParameterizedTest
	@DisplayName("Register Invalid Bracelet")
	@MethodSource("provideInvalidBraceletName")	
	void testRegisterBraceletInvalid(String name) {
		dto.setName(name);
		
		String response = assertDoesNotThrow(
			()->mockMvc.perform(
				post(BRACELETS_PREFIX)
					.with(user(user.getUsername()).password(user.getPassword()))
					.contentType("application/json")
					.content(mapper.writeValueAsString(dto))
				).andExpect(status().isBadRequest())
				.andReturn().getResponse().getContentAsString()
		);
		
		assertThat(response, containsString("name"));
	}
	
	private static Stream<Arguments> provideInvalidBraceletName(){
		return Stream.of(
			Arguments.of(""),
			Arguments.of("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxy")
		);
	}

}
