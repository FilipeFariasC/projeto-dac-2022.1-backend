package br.edu.ifpb.dac.groupd.business.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.business.exception.AlarmNotFoundException;
import br.edu.ifpb.dac.groupd.model.entities.Alarm;
import br.edu.ifpb.dac.groupd.model.entities.Fence;
import br.edu.ifpb.dac.groupd.model.entities.Location;
import br.edu.ifpb.dac.groupd.model.repository.AlarmRepository;

@Service
public class AlarmService {
	
	@Autowired
	private AlarmRepository alarmRepository;
	
	public Alarm saveAlarm(Location location, Fence fence, Double distance) {
		Alarm alarm = new Alarm();
		
		alarm.setFence(fence);
		alarm.setLocation(location);
		alarm.setSeen(false);
		alarm.setDistance(distance);
		
		return alarmRepository.save(alarm);

	}
	
	public List<Alarm> getAll(){
		return alarmRepository.findAll();
	}
	
	public Page<Alarm> findByFenceId(Long fenceId, Pageable pageable){
		return alarmRepository.findByFence(fenceId, pageable);
	}
	public Page<Alarm> findByBraceletId(Long braceletId, Pageable pageable){
		return alarmRepository.findByBracelet(braceletId, pageable);
	}
	public Alarm findByLocationId(Long locationId) throws AlarmNotFoundException {
		Optional<Alarm> register = alarmRepository.findByLocation(locationId);
		
		if(register.isEmpty())
			throw new AlarmNotFoundException(String.format("Não foi encontrado alarmes para a location de identificador %d", locationId));
		
		return register.get();
	}
	
	public Alarm alarmSeen(Long idAlarm) throws AlarmNotFoundException {
		Optional<Alarm> register = alarmRepository.findById(idAlarm);
		
		if(register.isEmpty()) {
			throw new AlarmNotFoundException(idAlarm);
		}
		Alarm alarm = register.get();
		alarm.setSeen(true);
		
		return alarmRepository.save(alarm);
	}
	
	public Alarm findAlarmById(Long idAlarm) throws AlarmNotFoundException  {
		Optional<Alarm> register = alarmRepository.findById(idAlarm);
		
		if(register.isEmpty()) 
			throw new AlarmNotFoundException(idAlarm);
		
		
		return register.get();
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
	
	

}
