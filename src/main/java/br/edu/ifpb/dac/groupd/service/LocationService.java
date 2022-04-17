package br.edu.ifpb.dac.groupd.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import br.edu.ifpb.dac.groupd.dto.post.LocationPostDto;
import br.edu.ifpb.dac.groupd.exception.BraceletNotFoundException;
import br.edu.ifpb.dac.groupd.exception.LocationNotFoundException;
import br.edu.ifpb.dac.groupd.model.Bracelet;
import br.edu.ifpb.dac.groupd.model.Location;
import br.edu.ifpb.dac.groupd.repository.LocationRepository;

public class LocationService {
	@Autowired
	private LocationRepository locationRepo;
	
	@Autowired
	private BraceletService braceletService;
	
	@Autowired
	private ModelMapper mapper;
	
	public Location create(LocationPostDto dto) throws BraceletNotFoundException {
		Bracelet bracelet = braceletService.findById(dto.getBraceletId());
		
		Location location = mapfromDto(dto);
		location.setBracelet(bracelet);
		
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
		return mapper.map(dto, Location.class);
	}
	
}
