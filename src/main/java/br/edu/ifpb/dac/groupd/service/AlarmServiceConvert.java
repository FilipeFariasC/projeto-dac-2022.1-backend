package br.edu.ifpb.dac.groupd.service;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.dac.groupd.dto.AlarmDto;
import br.edu.ifpb.dac.groupd.dto.post.AlarmPostDto;
import br.edu.ifpb.dac.groupd.model.Alarm;

public class AlarmServiceConvert {
	
	public Alarm mapFromDto(AlarmPostDto dto){
		Alarm alarm = new Alarm();
		alarm.setLocation(dto.getLocation());
		alarm.setRegisterDate(dto.getRegisterDate());
		alarm.setSeen(dto.getSeen());
		alarm.setFence(dto.getFence());
		return alarm;
	}
	
	public AlarmDto mapFromDto(Alarm alarm){
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
			AlarmDto alarmDTO = mapFromDto(alarm);
			dtos.add(alarmDTO);
		}
		
		return dtos;
	}
	
	

}
