package br.edu.ifpb.dac.groupd.business.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.business.exception.BraceletNotFoundException;
import br.edu.ifpb.dac.groupd.business.exception.LocationCreationDateInFutureException;
import br.edu.ifpb.dac.groupd.business.exception.LocationNotFoundException;
import br.edu.ifpb.dac.groupd.business.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.business.service.converter.LocationConverterService;
import br.edu.ifpb.dac.groupd.business.service.interfaces.UserService;
import br.edu.ifpb.dac.groupd.model.entities.Bracelet;
import br.edu.ifpb.dac.groupd.model.entities.Fence;
import br.edu.ifpb.dac.groupd.model.entities.Location;
import br.edu.ifpb.dac.groupd.model.repository.BraceletRepository;
import br.edu.ifpb.dac.groupd.model.repository.LocationRepository;
import br.edu.ifpb.dac.groupd.presentation.dto.LocationRequest;

@Service
public class LocationService {
	@Autowired
	private UserService userService;
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
	
	@Autowired
	private LocationConverterService locationConverter;
	
	public Location create(Long id, LocationRequest dto) throws BraceletNotFoundException, LocationCreationDateInFutureException, UserNotFoundException {
		userService.findById(id);

		Bracelet bracelet = braceletService.findByBraceletId(id, dto.getBraceletId());
		
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
		
		Location mapped = locationConverter.requestToLocation(dto);
		mapped.setId(null);
		
		Location location = locationRepo.save(mapped);
		
		bracelet.addLocation(location);
		braceletRepo.save(bracelet);

		Fence fence = bracelet.getMonitor();
		if(fence != null) {
			alarmService.saveAlarm(location, fence);
		}
		
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
	
	public Page<Location> findByBraceletId(Long id, Long braceletId, Pageable pageable) throws BraceletNotFoundException, UserNotFoundException {
		userService.findById(id);
		
		return locationRepo.findByBraceletId(braceletId, pageable);
	}
	
	private String formatDate(LocalDateTime time){

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

		return time.format(formatter);
	}
}
