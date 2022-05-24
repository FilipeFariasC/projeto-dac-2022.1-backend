package br.edu.ifpb.dac.groupd.tests.integration;


import static org.junit.Assert.assertEquals;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.edu.ifpb.dac.groupd.dto.AlarmDto;
import br.edu.ifpb.dac.groupd.exception.FenceEmptyException;
import br.edu.ifpb.dac.groupd.model.Alarm;
import br.edu.ifpb.dac.groupd.model.Fence;
import br.edu.ifpb.dac.groupd.repository.AlarmRepository;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureTestDatabase
@Testable
@DisplayName("AlarmService")
public class AlarmRepositoryTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private AlarmRepository alarmRepository; 
	
	private Alarm alarm;
	

	@BeforeAll
	public void iniciar() {
		Fence fence = new Fence();
		try {
			fence.setActive(true);
		} catch (FenceEmptyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.alarm = new Alarm();
		alarm.setFence(fence);
		alarm.setLocation(null);
	}
	
	@Test
	@DisplayName("return list Alarm")
	void listFind() {
		Alarm saveAlrm = alarmRepository.save(this.alarm);
		
		
		List<Alarm> alarms = testRestTemplate.exchange("/alarms/search", HttpMethod.GET,
				null, new ParameterizedTypeReference<List<Alarm>>() {
				}).getBody();
		
		Assertions.assertThat(alarms).isNotNull().isNotEmpty().hasSize(1);
		
		Assertions.assertThat(alarms.get(0).getClass()).isEqualTo(saveAlrm.getClass());
		
	}
	
/**	
	@Test
	@DisplayName("return id")
	void findAlarmById() {
		Alarm saveAlrm = alarmRepository.save(new Alarm());
		
		Long experctedId = saveAlrm.getId();
		
		AlarmDto alarm = testRestTemplate.getForObject("/alarms/id", AlarmDto.class, experctedId);
		
		Assertions.assertThat(alarm).isNotNull();
		
		Assertions.assertThat(alarm.getId()).isNotNull().isEqualTo(experctedId);
		
	}
	**/
	
	
	/**
	 void criarNovaAvaliacaoTest() {

	        AvaliacaoForm form = new AvaliacaoForm();
	        form.setNota(5);
	        form.setComentario("Mediano");

	        HttpEntity<AvaliacaoForm> httpEntity = new HttpEntity<>(form);

	        ResponseEntity<AvaliacaoDto> response = this.testRestTemplate
	            .exchange("/avaliacao", HttpMethod.POST, httpEntity, AvaliacaoDto.class);
	    
	        assertEquals(response.getStatusCode(), HttpStatus.OK);
	        assertEquals(response.getBody().getNota(), 5);
	    }
**/	 
	@Test
    public void buscarAlarmPorIdTest() {
        Alarm alarmSalva = this.alarmRepository.save(new Alarm());

        ResponseEntity<AlarmDto> response =
        		this.testRestTemplate
            .exchange("/alarms/{id}" + alarmSalva.getId(), HttpMethod.GET, null, AlarmDto.class);
    
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().getSeen(), 8);
    }
	
	
    
    
	
	
	
	
	
}
