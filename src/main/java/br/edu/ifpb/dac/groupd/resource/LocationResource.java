package br.edu.ifpb.dac.groupd.resource;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.dac.groupd.dto.LocationDto;
import br.edu.ifpb.dac.groupd.dto.post.LocationPostDto;

@RestController
@RequestMapping("/locations")
public class LocationResource {
//	@Autowired
//	private LocationService locationService;
	@Autowired
	private ModelMapper mapper;
	
	/*
	@PostMapping
	@ResponseStatus(status=HttpStatus.CREATED)
	public ResponseEntityLocationDto create(
			@Valid
			@RequestBody
			LocationPostDto postDto) {
		
	}
	
	@GetMapping
	public ResponseEntity<?> getAll(){
		List<LocationDto> dtos = locationService.getAll()
			.stream()
			.map(this::mapToDto);
		
		
	}
	
	@GetMapping("{pulseiraId}")
	public LocationDto findByBraceletId(@PathVariable("pulseiraId") Long pulseiraId){
		LocationDto dto = locationService.getLocationByBraceletId(pulseiraId);
		
		
	}
	
	public LocationDto mapToDto(Location location){
		LocationDto dto = mapper.map(location, LocationDto.class);
		
		dto.setBraceletId(location.getId());
		
		return dto;
	 }
	 */
}
