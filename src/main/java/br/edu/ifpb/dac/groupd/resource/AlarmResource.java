package br.edu.ifpb.dac.groupd.resource;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.dac.groupd.dto.AlarmDto;
import br.edu.ifpb.dac.groupd.dto.post.AlarmPostDto;
import br.edu.ifpb.dac.groupd.exception.AlarmNotFoundException;
import br.edu.ifpb.dac.groupd.exception.BraceletNotInFenceException;
import br.edu.ifpb.dac.groupd.exception.FenceNotFoundException;
import br.edu.ifpb.dac.groupd.exception.LocationNotFoundException;
import br.edu.ifpb.dac.groupd.model.Alarm;
import br.edu.ifpb.dac.groupd.service.AlarmService;
import br.edu.ifpb.dac.groupd.service.AlarmServiceConvert;

@RestController
@RequestMapping("/alarms")
public class AlarmResource {
	
	@Autowired
	private AlarmService alarmService;
	
	@Autowired
	private AlarmServiceConvert alarmServiceConvert;
	
	@PostMapping
	public ResponseEntity<?> saveAlarm(
			@Valid
			@RequestBody AlarmPostDto postDto) {
		try {

			Alarm alarm = alarmService.saveAlarm(postDto);
			
			AlarmDto dto = alarmServiceConvert.mapToDto(alarm);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(dto);
			
		} catch (FenceNotFoundException | LocationNotFoundException | BraceletNotInFenceException e) {
			
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateAlarm(
			@PathVariable("id") Long idAlarm, 
			@Valid
			@RequestBody
			AlarmPostDto postDto) {
		try {
			Alarm alarm = alarmService.updateAlarm(idAlarm, postDto);
			AlarmDto dto = alarmServiceConvert.mapToDto(alarm);
			
			return ResponseEntity.ok(dto);
		} catch (AlarmNotFoundException | FenceNotFoundException | LocationNotFoundException | BraceletNotInFenceException exception) {
			
			return ResponseEntity.badRequest().body(exception.getMessage());
		}
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<?> alarmSeen(@PathVariable("id") Long idAlarm){
		try {
			Alarm alarm = alarmService.alarmSeen(idAlarm);
			AlarmDto dto = alarmServiceConvert.mapToDto(alarm);
			
			return ResponseEntity.ok(dto);
		} catch (AlarmNotFoundException exception) {
			
			return ResponseEntity.badRequest().body(exception.getMessage());
		}
	}
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteAlarm(@PathVariable("id") Long idAlarm) {
		try {
			alarmService.deleteAlarm(idAlarm);
			return ResponseEntity.noContent().build();
		}catch(AlarmNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findAlarmById(@PathVariable("id") Long alarmId){
		try {
			Alarm alarm = alarmService.findAlarmById(alarmId);
			
			AlarmDto dto = alarmServiceConvert.mapToDto(alarm);
			
			return ResponseEntity.ok(dto);
		} catch (AlarmNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	
	@GetMapping("/search")
	public ResponseEntity<?> find(
			@RequestParam(value = "idAlarm",required = true) Long idAlarm,
			@RequestParam(value = "seen", required = false) boolean seen) {
		
		try {
			Alarm filter = new Alarm();
			
			filter.setId(idAlarm);
			filter.setSeen(seen);
			
			List<Alarm> alarms = alarmService.findFilter(filter);
			List<AlarmDto> dtos = alarmServiceConvert.alarmsToDTO(alarms);
			
			return ResponseEntity.ok(dtos);
		}catch(Exception e) {
			
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
