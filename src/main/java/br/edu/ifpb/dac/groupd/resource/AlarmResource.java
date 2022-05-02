package br.edu.ifpb.dac.groupd.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.dac.groupd.dto.AlarmDto;
import br.edu.ifpb.dac.groupd.exception.AlarmNotFoundException;
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
			List<AlarmDto> dtos = alarmServiceConvert.alarmsToDto(alarms);
			
			return ResponseEntity.ok(dtos);
		}catch(Exception e) {
			
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	@GetMapping("/fence/{id}")
	public ResponseEntity<?> findByFence(@PathVariable("id") Long fenceId){
		List<AlarmDto> alarms = alarmService.findByFenceId(fenceId)
				.stream()
				.map(alarmServiceConvert::mapToDto)
				.toList();
		
		return ResponseEntity.ok(alarms);
	}
	@GetMapping("/bracelet/{id}")
	public ResponseEntity<?> findByBracelet(@PathVariable("id") Long braceletId){
		List<AlarmDto> alarms = alarmService.findByBraceletId(braceletId)
				.stream()
				.map(alarmServiceConvert::mapToDto)
				.toList();
		
		return ResponseEntity.ok(alarms);
	}
}
