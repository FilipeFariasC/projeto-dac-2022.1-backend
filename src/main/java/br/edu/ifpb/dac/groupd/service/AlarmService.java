package br.edu.ifpb.dac.groupd.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.exception.AlarmNotFoundException;
import br.edu.ifpb.dac.groupd.exception.BraceletNotInFenceException;
import br.edu.ifpb.dac.groupd.exception.FenceNotFoundException;
import br.edu.ifpb.dac.groupd.exception.LocationNotFoundException;
import br.edu.ifpb.dac.groupd.model.Alarm;
import br.edu.ifpb.dac.groupd.model.Fence;
import br.edu.ifpb.dac.groupd.repository.AlarmRepository;

@Service
public class AlarmService {
	
	@Autowired
	private AlarmRepository alarmRepository;
	@Autowired
	private AlarmServiceConvert alarmServiceConvert;
	
	@Autowired
	private FenceService fenceService;
	
	@Autowired
	private LocationService locationService;
	
	public Alarm saveAlarm(Long locationId, Long fenceId) throws FenceNotFoundException, LocationNotFoundException, BraceletNotInFenceException {
		// TODO 
		
		return null;
	}
	
	public List<Alarm> getAll(){
		return alarmRepository.findAll();
	}
	
	public List<Alarm> getSeen(Boolean seen){
		return alarmRepository.findBySeen(seen);
	}
	
	public List<Alarm> getFence(Fence fence){
		return alarmRepository.findByfence(fence);
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
