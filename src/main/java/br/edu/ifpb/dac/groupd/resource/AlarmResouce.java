package br.edu.ifpb.dac.groupd.resource;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.dac.groupd.dto.AlarmDto;
import br.edu.ifpb.dac.groupd.dto.BraceletDto;
import br.edu.ifpb.dac.groupd.dto.post.AlarmPostDto;
import br.edu.ifpb.dac.groupd.exception.AlarmNotFoundException;
import br.edu.ifpb.dac.groupd.model.Alarm;
import br.edu.ifpb.dac.groupd.model.Bracelet;
import br.edu.ifpb.dac.groupd.model.Fence;
import br.edu.ifpb.dac.groupd.service.AlarmService;
import br.edu.ifpb.dac.groupd.service.AlarmServiceConvert;

@RestController
@RequestMapping("/alarm")
public class AlarmResouce {
	
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
			AlarmDto dto = alarmServiceConvert.mapFromDto(alarm);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(dto);
			
		} catch (Exception e) {
			
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("{id}")
	public ResponseEntity<?> updateAlarm(
			@PathVariable("id") Long idAlarm, AlarmPostDto postDto) {
		try {
			Alarm alarm = alarmService.updateAlarm(idAlarm, postDto);
			AlarmDto dto = alarmServiceConvert.mapFromDto(alarm);
			
			return ResponseEntity.ok(dto);
		} catch (AlarmNotFoundException exception) {
			
			return ResponseEntity.badRequest().body(exception.getMessage());
		}
	}
	
	@DeleteMapping("id")
	public ResponseEntity<?> deleteAlarm(@PathVariable("id") Long idAlarm) {
		try {
			alarmService.deleteAlarm(idAlarm);
			return ResponseEntity.noContent().build();
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping
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
