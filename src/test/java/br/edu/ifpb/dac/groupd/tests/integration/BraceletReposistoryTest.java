package br.edu.ifpb.dac.groupd.tests.integration;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.platform.commons.annotation.Testable;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.edu.ifpb.dac.groupd.model.entities.Bracelet;
import br.edu.ifpb.dac.groupd.model.repository.BraceletRepository;
import br.edu.ifpb.dac.groupd.presentation.dto.BraceletResponse;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testable
@DisplayName("BraceleteReposistoryTest")
public class BraceletReposistoryTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@LocalServerPort
	private int port;
	
	@Mock
	private BraceletRepository braceletRepository; 

	private Bracelet bracelet = new Bracelet();;
	

	@BeforeAll
	public void iniciar() {
		/**
		Coordinate coordinate = new Coordinate();
		coordinate.setLatitude(-8.07439);
		coordinate.setLongitude(-37.2646);
		**/
		/**
		Location location = new Location();
		location.setBracelet(this.bracelet);
		location.setCoordinate(coordinate);
		location.setCreationDate(LocalDateTime.now());
		**/
		/**
		Fence fence = new Fence();
		fence.addBracelet(this.bracelet);
		fence.setCoordinate(coordinate);
		fence.setRadius(1.0);
		fence.setStartTime(LocalTime.now());
		fence.setFinishTime(LocalTime.now().plusHours(1));
		**/
		 
		bracelet.setName("Julia");
		//bracelet.setMonitor(fence);
		//bracelet.getFences().add(fence);
		//bracelet.getLocations().add(location);
		
	}
	
	
	@Test
	void test() {
		
		
		
	}
	@Test
	void criarBraceletTest() {
        HttpEntity<Bracelet> httpEntity = new HttpEntity<>(this.bracelet);

        ResponseEntity<BraceletResponse> response = this.testRestTemplate
            .exchange("/bracelets", HttpMethod.POST, httpEntity, BraceletResponse.class);
    
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().getName(), "Julia");
    }
	
	/**
	@Test
	@DisplayName("return list Bracelet")
	void listFind() {
		Alarm saveAlrm = alarmRepository.save(this.alarm);
		
		
		List<Alarm> alarms = testRestTemplate.exchange("/alarms/search", HttpMethod.GET,
				null, new ParameterizedTypeReference<List<Alarm>>() {
				}).getBody();
		
		Assertions.assertThat(alarms).isNotNull().isNotEmpty().hasSize(1);
		
		Assertions.assertThat(alarms.get(0).getClass()).isEqualTo(saveAlrm.getClass());
		
	}
	**/

}
