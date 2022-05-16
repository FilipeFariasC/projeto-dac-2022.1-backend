package br.edu.ifpb.dac.groupd.tests.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import br.edu.ifpb.dac.groupd.model.Alarm;
import br.edu.ifpb.dac.groupd.repository.AlarmRepository;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@Testable
@DisplayName("AlarmService")
public class AlarmRepositoryTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private AlarmRepository alarmRepository; 
	
	@Test
	@DisplayName("return list Alarm")
	void listFind() {
		Alarm saveAlrm = alarmRepository.save(new Alarm());
		
		List<Alarm> alarms = testRestTemplate.exchange("/search", HttpMethod.GET,
				null, new ParameterizedTypeReference<List<Alarm>>() {
				}).getBody();
		
		Assertions.assertThat(alarms).isNotNull().isNotEmpty().hasSize(1);
		
		Assertions.assertThat(alarms.get(0).getClass()).isEqualTo(saveAlrm.getClass());
		
	}
	
	
	
}
