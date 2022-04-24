package br.edu.ifpb.dac.groupd.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.dto.AlarmDto;
import br.edu.ifpb.dac.groupd.dto.post.AlarmPostDto;
import br.edu.ifpb.dac.groupd.model.Alarm;

@Service
public class AlarmServiceConvert {
	
	public Alarm mapFromDto(AlarmPostDto dto){
		Alarm alarm = new Alarm();
		alarm.setRegisterDate(dto.getRegisterDate());
		
		if(alarm.getRegisterDate() == null) {
			alarm.setRegisterDate(LocalDateTime.now());
		}
		
		return alarm;
	}
	
	public AlarmDto mapToDto(Alarm alarm){
		AlarmDto dto = new AlarmDto();
		dto.setLocation(alarm.getLocation().getId());
		dto.setRegisterDate(alarm.getRegisterDate());
		dto.setSeen(alarm.getSeen());
		dto.setFence(alarm.getFence().getId());
		return dto;
	}
	
	public List<AlarmDto> alarmsToDTO(List<Alarm> alarms){
		
		List<AlarmDto> dtos = new ArrayList<>();
		
		for(Alarm alarm : alarms) {
			AlarmDto alarmDTO = mapToDto(alarm);
			dtos.add(alarmDTO);
		}
		
		return dtos;
	}
	
	

}
