package br.edu.ifpb.dac.groupd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;

import br.edu.ifpb.dac.groupd.dto.post.AlarmPostDto;
import br.edu.ifpb.dac.groupd.exception.AlarmNotFoundException;
import br.edu.ifpb.dac.groupd.model.Alarm;
import br.edu.ifpb.dac.groupd.model.Fence;
import br.edu.ifpb.dac.groupd.repository.AlarmRepository;

public class AlarmService {
	
	@Autowired
	private AlarmRepository alarmRepository;
	
	public Alarm saveAlarm(AlarmPostDto dto) {
		 Alarm alarm = mapFromDto(dto);
		 
		return alarmRepository.save(alarm);
	}
	
	public List<Alarm> getAll(){
		return alarmRepository.findAll();
	}
	
	public List<Alarm> getSeen(Boolean seen){
		return alarmRepository.findByseen(seen);
	}
	
	public List<Alarm> getFence(Fence fence){
		return alarmRepository.findByfence(fence);
	}
	
	public Alarm updateAlarm(Long idAlarm, AlarmPostDto dto) throws AlarmNotFoundException  {
		if(!alarmRepository.existsById(idAlarm)) {
			throw new AlarmNotFoundException(idAlarm);
		}
		Alarm alarm = mapFromDto(dto);
		alarm.setId(idAlarm);
		return alarmRepository.save(alarm);
	}
	
	public void deleteAlarm(Long idAlarm) throws AlarmNotFoundException  {
		if(!alarmRepository.existsById(idAlarm)) 
			throw new AlarmNotFoundException(idAlarm);
		
		alarmRepository.deleteById(idAlarm);
	}
	
	public List<Alarm> findFilter(Alarm filter){
		Example<Alarm> example = Example.of(filter, ExampleMatcher.matching()
				.withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING));
		return alarmRepository.findAll(example);
	}
	
	private Alarm mapFromDto(AlarmPostDto dto){
		Alarm alarm = new Alarm();
		alarm.setLocation(dto.getLocation());
		alarm.setRegisterDate(dto.getRegisterDate());
		alarm.setSeen(dto.getSeen());
		alarm.setFence(dto.getFence());
		return alarm;
	}

}
