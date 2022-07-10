package br.edu.ifpb.dac.groupd.business.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.business.exception.BraceletNotFoundException;
import br.edu.ifpb.dac.groupd.business.exception.BraceletNotRegisteredException;
import br.edu.ifpb.dac.groupd.business.exception.LocationCreationDateInFutureException;
import br.edu.ifpb.dac.groupd.business.exception.LocationNotFoundException;
import br.edu.ifpb.dac.groupd.business.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.model.entities.Bracelet;
import br.edu.ifpb.dac.groupd.model.entities.Fence;
import br.edu.ifpb.dac.groupd.model.entities.Location;
import br.edu.ifpb.dac.groupd.model.repository.BraceletRepository;
import br.edu.ifpb.dac.groupd.model.repository.LocationRepository;
import br.edu.ifpb.dac.groupd.presentation.dto.LocationRequest;

@Service
public class LocationService {
	@Autowired
	private LocationRepository locationRepo;
	
	@Autowired
	private BraceletService braceletService;
	
	@Autowired
	private BraceletRepository braceletRepo;
	
	@Autowired
	private AlarmService alarmService;
	
	
	@Autowired
	private CoordinateService coordinateService;
	
	public Location create(String email, LocationRequest dto) throws BraceletNotFoundException, LocationCreationDateInFutureException, UserNotFoundException, BraceletNotRegisteredException {
		if(dto.getCreationDate() == null){
			dto.setCreationDate(LocalDateTime.now());
		} else {
			LocalDateTime now = LocalDateTime.now();
					
			if(dto.getCreationDate().isAfter(now)) {
				throw new LocationCreationDateInFutureException( 
						formatDate(dto.getCreationDate()),
						formatDate(now)
						);
			}
		}
		
		Bracelet bracelet = braceletService.findByBraceletId(email, dto.getBraceletId());
		
		Location mapped = mapfromDto(dto);
		mapped.setBracelet(bracelet);
		
		Location location = locationRepo.save(mapped);
		
		bracelet.addLocation(location);
		if(bracelet.getMonitor() != null) {
			Fence fence = bracelet.getMonitor();
			
			if(fence.getRadius() < coordinateService.calculateDistance(fence.getCoordinate(), location.getCoordinate()) ) {
				alarmService.saveAlarm(location, fence);
			}
		}
		
		braceletRepo.save(bracelet);
		
		return location;
	}
	
	public List<Location> getAll(){
		return locationRepo.findAll();
	}
	
	public Location findById(Long id) throws LocationNotFoundException {
		Optional<Location> location = locationRepo.findById(id);
		
		if(location.isEmpty())
			throw new LocationNotFoundException(id);
		
		return location.get();
	}
	
	public List<Location> findByBraceletId(String email, Long braceletId) throws BraceletNotFoundException, UserNotFoundException, BraceletNotRegisteredException {
		Bracelet bracelet = braceletService.findByBraceletId(email, braceletId);
		
		return bracelet.getLocations().stream().toList();
	}
	
	
	public Location mapfromDto(LocationRequest dto) {
		Location location = new Location();
		
		location.setCoordinate(dto.getCoordinate());
		location.setCreationDate(dto.getCreationDate());
		
		return location;
	}
	
	private String formatDate(LocalDateTime time){

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

		return time.format(formatter);
	}
}