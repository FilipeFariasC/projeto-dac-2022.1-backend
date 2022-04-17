package br.edu.ifpb.dac.groupd.resource;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.dac.groupd.dto.BraceletDto;
import br.edu.ifpb.dac.groupd.dto.post.BraceletPostDto;
import br.edu.ifpb.dac.groupd.exception.BraceletNotFoundException;
import br.edu.ifpb.dac.groupd.model.Bracelet;
import br.edu.ifpb.dac.groupd.service.BraceletService;
import br.edu.ifpb.dac.groupd.service.BraceletServiceConvert;


@RestController
@RequestMapping("/bracelets")
public class BraceletResouce {
	
	@Autowired
	private BraceletService braceletService;
	@Autowired
	private BraceletServiceConvert braceletServiceConvert;
	
	@PostMapping
	public ResponseEntity saveBracelet(
			@Valid
			@RequestBody BraceletPostDto postDto) {
		try {

			Bracelet bracelet = braceletService.save(postDto);
			BraceletDto dto = mapToDto(bracelet);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(dto);
			
		} catch (Exception e) {
			
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("{id}")
	public ResponseEntity updateBracelet(@PathVariable("id") Long idBracelet, BraceletPostDto postDto) {
		try {
			Bracelet bracelet = braceletService.update(idBracelet, postDto);
			BraceletDto dto = mapToDto(bracelet);
			
			return ResponseEntity.ok(dto);
		} catch (BraceletNotFoundException exception) {
			
			return ResponseEntity.badRequest().body(exception.getMessage());
		}
	}
	
	
	@DeleteMapping("id")
	public ResponseEntity<?> deleteBracelet(@PathVariable("id") Long idBracelete) {
		try {
			braceletService.delete(idBracelete);
			return ResponseEntity.noContent().build();
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping
	public ResponseEntity<?> find(
			@RequestParam(value = "idBracelet",required = true) Long idBracelet,
			@RequestParam(value = "nome", required = true)String name) {
		
		try {
			Bracelet filter = new Bracelet();
			filter.setIdBracelet(idBracelet);
			filter.setName(name);
			
			List<Bracelet> bracelets = braceletService.findFilter(filter);
			List<BraceletDto> dtos = braceletServiceConvert.braceletToDTO(bracelets);
			
			return ResponseEntity.ok(dtos);
		}catch(Exception e) {
			
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	private BraceletDto mapToDto(Bracelet bracelet){
		BraceletDto dto = new BraceletDto();
		
		dto.setIdBracelet(bracelet.getIdBracelet());
		dto.setName(bracelet.getName());
		
		return dto;
	}

}
