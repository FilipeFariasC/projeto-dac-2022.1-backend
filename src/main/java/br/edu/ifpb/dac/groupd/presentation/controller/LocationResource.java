package br.edu.ifpb.dac.groupd.presentation.controller;

import java.net.URI;
import java.security.Principal;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import br.edu.ifpb.dac.groupd.business.exception.LocationCreationDateInFutureException;
import br.edu.ifpb.dac.groupd.business.exception.LocationNotFoundException;
import br.edu.ifpb.dac.groupd.business.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.business.service.AlarmService;
import br.edu.ifpb.dac.groupd.business.service.LocationService;
import br.edu.ifpb.dac.groupd.business.service.converter.AlarmConverterService;
import br.edu.ifpb.dac.groupd.business.service.converter.LocationConverterService;
import br.edu.ifpb.dac.groupd.model.entities.Alarm;
import br.edu.ifpb.dac.groupd.model.entities.Location;
import br.edu.ifpb.dac.groupd.presentation.dto.LocationRequest;
import br.edu.ifpb.dac.groupd.presentation.dto.LocationResponse;
import br.edu.ifpb.dac.groupd.presentation.dto.LocationResponseMin;

@RestController
@RequestMapping({"/users/locations", "/locations"})
public class LocationResource {
	@Autowired
	private LocationService locationService;
	
	@Autowired
	private AlarmService alarmService;
	
	@Autowired
	private LocationConverterService locationConverter;
	
	@Autowired
	private AlarmConverterService alarmConverter;
	
	@PostMapping
	@ResponseStatus(code=HttpStatus.CREATED)
	public ResponseEntity<?> create(
			Principal principal,
			@Valid
			@RequestBody
			LocationRequest postDto,
			HttpServletResponse response
			) throws BraceletNotFoundException, LocationCreationDateInFutureException, UserNotFoundException {
		Location location = locationService.create(getPrincipalId(principal), postDto);
		
		LocationResponse dto = locationConverter.locationToResponse(location);
		try {
			Alarm alarm = alarmService.findByLocationId(location.getId());
			dto.setAlarm(alarmConverter.alarmToResponseMin(alarm));
		} catch (AlarmNotFoundException e) {
		}
		
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequestUri()
				.path("/?id={pulseiraId}")
				.buildAndExpand(location.getId())
				.toUri();
		
		return ResponseEntity.created(uri).body(dto);
	}
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable(name="id", required=true) Long id) throws LocationNotFoundException{
		Location location = locationService.findById(id);
		
		LocationResponseMin dto = locationConverter.locationToResponseMin(location);
		
		return ResponseEntity.ok(dto);

	}
	@GetMapping
	public ResponseEntity<?> findByBraceletId(Principal principal,
			@RequestParam("bracelet") Long pulseiraId,
			Pageable pageable) throws BraceletNotFoundException, UserNotFoundException {
		Page<Location> locations = locationService.findByBraceletId(getPrincipalId(principal), pulseiraId, pageable);

		Page<LocationResponseMin> dtos = locations
			.map(locationConverter::locationToResponseMin);
		
		return ResponseEntity.ok(dtos);
	}
	
	
	private Long getPrincipalId(Principal principal) {
		return Long.parseLong(principal.getName());
	}
}
