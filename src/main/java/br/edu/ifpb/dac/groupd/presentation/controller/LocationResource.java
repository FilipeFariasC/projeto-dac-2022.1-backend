package br.edu.ifpb.dac.groupd.presentation.controller;

import java.net.URI;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.edu.ifpb.dac.groupd.business.exception.AlarmNotFoundException;
import br.edu.ifpb.dac.groupd.business.exception.BraceletNotFoundException;
import br.edu.ifpb.dac.groupd.business.exception.BraceletNotRegisteredException;
import br.edu.ifpb.dac.groupd.business.exception.LocationCreationDateInFutureException;
import br.edu.ifpb.dac.groupd.business.exception.LocationNotFoundException;
import br.edu.ifpb.dac.groupd.business.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.business.service.AlarmService;
import br.edu.ifpb.dac.groupd.business.service.LocationService;
import br.edu.ifpb.dac.groupd.model.entities.Location;
import br.edu.ifpb.dac.groupd.presentation.dto.LocationRequest;
import br.edu.ifpb.dac.groupd.presentation.dto.LocationResponse;

@RestController
@RequestMapping("/locations")
public class LocationResource {
	@Autowired
	private LocationService locationService;
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private AlarmService alarmService;
	
	@PostMapping
	@ResponseStatus(code=HttpStatus.CREATED)
	public ResponseEntity<?> create(
			Principal principal,
			@Valid
			@RequestBody
			LocationRequest postDto,
			HttpServletResponse response
			) {
		try {
			Location location = locationService.create(principal.getName(),postDto);
			
			LocationResponse dto = mapToDto(location);
			
			URI uri = ServletUriComponentsBuilder
					.fromCurrentRequestUri()
					.path("/?id={pulseiraId}")
					.buildAndExpand(location.getId())
					.toUri();
			
			return ResponseEntity.created(uri).body(dto);
		} catch (BraceletNotFoundException | LocationCreationDateInFutureException | UserNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		} catch(BraceletNotRegisteredException exception) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
		}
	}
	@GetMapping
	public ResponseEntity<?> findById(@RequestParam(name="id", required=true) Long id){
		try {
			Location location = locationService.findById(id);
			
			LocationResponse dto = mapToDto(location);
			
			return ResponseEntity.ok(dto);
		} catch (LocationNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
		
	}
	@GetMapping("/{pulseiraId}")
	public ResponseEntity<?> findByBraceletId(Principal principal,@PathVariable("pulseiraId") Long pulseiraId){
		try {
			List<Location> locations = locationService.findByBraceletId(principal.getName(),pulseiraId);
			
			List<LocationResponse> dtos = locations
					.stream()
					.map(
						location->{
							return mapToDto(location);
						}
					)
					.toList();
			
			return ResponseEntity.ok(dtos);
		} catch (BraceletNotFoundException | UserNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		} catch (BraceletNotRegisteredException exception) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
		}
	}
	
	private void getAlarmIdIfExists(LocationResponse locationDto, Long locationId) {
		try {
			locationDto.setAlarmId(alarmService.findByLocationId(locationId).getId());
		} catch (AlarmNotFoundException e) {
		}
	}
	
	public LocationResponse mapToDto(Location location){
		LocationResponse dto = mapper.map(location, LocationResponse.class);
		
		dto.setBraceletId(location.getBracelet().getId());
		getAlarmIdIfExists(dto, location.getId());
		
		return dto;
	 }
	 
}
