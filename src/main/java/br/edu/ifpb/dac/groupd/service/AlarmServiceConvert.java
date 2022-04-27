package br.edu.ifpb.dac.groupd.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.dto.AlarmDto;
import br.edu.ifpb.dac.groupd.model.Alarm;

@Service
public class AlarmServiceConvert {
	
	public AlarmDto mapToDto(Alarm alarm){
		
		AlarmDto dto = new AlarmDto();
		dto.setId(alarm.getId());
		dto.setLocation(alarm.getLocation().getId());
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
