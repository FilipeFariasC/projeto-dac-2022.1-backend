package br.edu.ifpb.dac.groupd.resource;

import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.dac.groupd.dto.BraceletDTO;
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
	public ResponseEntity saveBracelet(@RequestBody BraceletDTO dto) {
		try {
			Bracelet bracelet = braceletServiceConvert.dtoToBracelet(dto);
			bracelet = braceletService.save(bracelet);
			dto = braceletServiceConvert.braceletToDTO(bracelet);
			
			return new ResponseEntity(dto, HttpStatus.CREATED);
			
		} catch (Exception e) {
			
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("{id}")
	public ResponseEntity updateBracelet(@PathVariable("id") Long idBracelet, BraceletDTO dto) {
		try {
			dto.setIdBracelet(idBracelet);
			Bracelet bracelet = braceletServiceConvert.dtoToBracelet(dto);
			bracelet = braceletService.update(idBracelet,bracelet);
			dto = braceletServiceConvert.braceletToDTO(bracelet);
			
			return ResponseEntity.ok(dto);
		} catch (Exception e) {
			
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	
	@DeleteMapping("id")
	public ResponseEntity deleteBracelet(@PathVariable("id") Long idBracelete) {
		try {
			braceletService.delete(idBracelete);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping
	public ResponseEntity find(
			@RequestParam(value = "idBracelet",required = true) Long idBracelet,
			@RequestParam(value = "nome", required = true)String name) {
		
		try {
			Bracelet filter = new Bracelet();
			filter.setIdBracelet(idBracelet);
			filter.setName(name);
			
			List<Bracelet> bracelets = braceletService.findFilter(filter);
			List<BraceletDTO> dtos = braceletServiceConvert.braceletToDTO(bracelets);
			
			return ResponseEntity.ok(dtos);
		}catch(Exception e) {
			
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
