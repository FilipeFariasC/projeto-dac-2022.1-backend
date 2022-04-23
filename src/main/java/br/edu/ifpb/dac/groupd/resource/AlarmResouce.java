package br.edu.ifpb.dac.groupd.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.dac.groupd.dto.AlarmDto;
import br.edu.ifpb.dac.groupd.dto.post.AlarmPostDto;
import br.edu.ifpb.dac.groupd.model.Alarm;
import br.edu.ifpb.dac.groupd.service.AlarmService;

@RestController
@RequestMapping("/alarm")
public class AlarmResouce {
	
	@Autowired
	private AlarmService alarmService;
	
	@PostMapping
	public ResponseEntity<?> saveAlarm(
			@Valid
			@RequestBody AlarmPostDto postDto) {
		try {

			Alarm alarm = alarmService.saveAlarm(postDto);
			AlarmDto dto = mapFromDto(alarm);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(dto);
			
		} catch (Exception e) {
			
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	
	private AlarmDto mapFromDto(Alarm alarm){
		AlarmDto dto = new AlarmDto();
		dto.setLocation(alarm.getLocation());
		dto.setRegisterDate(alarm.getRegisterDate());
		dto.setSeen(alarm.getSeen());
		dto.setFence(alarm.getFence());
		return dto;
	}	

}
