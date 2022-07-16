package br.edu.ifpb.dac.groupd.presentation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.dac.groupd.business.exception.AlarmNotFoundException;
import br.edu.ifpb.dac.groupd.business.service.AlarmService;
import br.edu.ifpb.dac.groupd.business.service.converter.AlarmConverterService;
import br.edu.ifpb.dac.groupd.model.entities.Alarm;
import br.edu.ifpb.dac.groupd.presentation.dto.AlarmResponse;

@RestController
@RequestMapping("/alarms")
public class AlarmResource {
	
	@Autowired
	private AlarmService alarmService;
	
	@Autowired
	private AlarmConverterService alarmConverter;
	

	@PatchMapping("/{id}")
	public ResponseEntity<?> alarmSeen(@PathVariable("id") Long idAlarm) throws AlarmNotFoundException{
		Alarm alarm = alarmService.alarmSeen(idAlarm);
		AlarmResponse dto = alarmConverter.alarmToResponse(alarm);
		
		return ResponseEntity.ok(dto);

	}
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteAlarm(@PathVariable("id") Long idAlarm) throws AlarmNotFoundException {
		alarmService.deleteAlarm(idAlarm);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findAlarmById(@PathVariable("id") Long alarmId) throws AlarmNotFoundException{
		Alarm alarm = alarmService.findAlarmById(alarmId);
		

		AlarmResponse dto = alarmConverter.alarmToResponse(alarm);
		
		return ResponseEntity.ok(dto);
	}
	
	@GetMapping("/search")
	public ResponseEntity<?> find(
			@RequestParam(value = "idAlarm",required = true) Long idAlarm,
			@RequestParam(value = "seen", required = false) boolean seen) {
		
		Alarm filter = new Alarm();
		
		filter.setId(idAlarm);
		filter.setSeen(seen);
		
		List<Alarm> alarms = alarmService.findFilter(filter);
		List<AlarmResponse> dtos = alarms.stream().map(alarmConverter::alarmToResponse)
			.toList();
		
		return ResponseEntity.ok(dtos);
	}
	
	@GetMapping("/fence/{id}")
	public ResponseEntity<?> findByFence(@PathVariable("id") Long fenceId, Pageable pageable){
		Page<AlarmResponse> alarms = alarmService.findByFenceId(fenceId, pageable)
				.map(alarmConverter::alarmToResponse);
		
		return ResponseEntity.ok(alarms);
	}
	@GetMapping("/bracelet/{id}")
	public ResponseEntity<?> findByBracelet(@PathVariable("id") Long braceletId, Pageable pageable){
		Page<AlarmResponse> alarms = alarmService.findByBraceletId(braceletId, pageable)
				.map(alarmConverter::alarmToResponse);
		
		return ResponseEntity.ok(alarms);
	}
}
