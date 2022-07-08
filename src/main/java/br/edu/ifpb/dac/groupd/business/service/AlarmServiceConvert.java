package br.edu.ifpb.dac.groupd.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.model.entities.Alarm;
import br.edu.ifpb.dac.groupd.presentation.dto.AlarmResponse;

@Service
public class AlarmServiceConvert {
	
	public AlarmResponse mapToDto(Alarm alarm){
		
		AlarmResponse dto = new AlarmResponse();
		dto.setId(alarm.getId());
		dto.setLocation(alarm.getLocation().getId());
		dto.setSeen(alarm.isSeen());
		dto.setFence(alarm.getFence().getId());
		
		return dto;
	}
	
	public List<AlarmResponse> alarmsToDto(List<Alarm> alarms){
		return alarms.stream()
				.map(this::mapToDto)
				.toList();
	}

}
