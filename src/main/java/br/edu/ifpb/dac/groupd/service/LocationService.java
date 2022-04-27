package br.edu.ifpb.dac.groupd.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.dto.post.LocationPostDto;
import br.edu.ifpb.dac.groupd.exception.BraceletNotFoundException;
import br.edu.ifpb.dac.groupd.exception.LocationCreationDateInFutureException;
import br.edu.ifpb.dac.groupd.exception.LocationNotFoundException;
import br.edu.ifpb.dac.groupd.model.Bracelet;
import br.edu.ifpb.dac.groupd.model.Location;
import br.edu.ifpb.dac.groupd.repository.BraceletRepository;
import br.edu.ifpb.dac.groupd.repository.LocationRepository;

@Service
public class LocationService {
	@Autowired
	private LocationRepository locationRepo;
	
	@Autowired
	private BraceletService braceletService;
	
	@Autowired
	private BraceletRepository braceletRepo;
	
	public Location create(LocationPostDto dto) throws BraceletNotFoundException, LocationCreationDateInFutureException {
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
		
		Bracelet bracelet = braceletService.findById(dto.getBraceletId());
		
		Location mapped = mapfromDto(dto);
		mapped.setBracelet(bracelet);
		
		Location location =locationRepo.save(mapped);
		
		bracelet.addLocation(mapped);
		
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
	
	public List<Location> findByBraceletId(Long braceletId) throws BraceletNotFoundException {
		Bracelet bracelet = braceletService.findById(braceletId);
		
		return bracelet.getLocations().stream().toList();
	}
	
	
	public Location mapfromDto(LocationPostDto dto) {
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
