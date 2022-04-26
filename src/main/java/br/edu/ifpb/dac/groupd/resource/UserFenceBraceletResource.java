package br.edu.ifpb.dac.groupd.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.dac.groupd.exception.BraceletNotRegisteredException;
import br.edu.ifpb.dac.groupd.exception.FenceNotRegisteredException;
import br.edu.ifpb.dac.groupd.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.service.UserFenceBraceletService;

@RestController
@RequestMapping("/users/{id}/fences/{fenceId}/{braceletId}")
public class UserFenceBraceletResource {
	
	@Autowired
	private UserFenceBraceletService userFenceBraceletService;
	
	@PostMapping
	public ResponseEntity<?> addBraceletFence(
			@PathVariable("id") Long id,
			@PathVariable("fenceId") Long fenceId,
			@PathVariable("braceletId") Long braceletId){
		try {
			userFenceBraceletService.addBraceletFence(id, fenceId, braceletId);
			
			return ResponseEntity.ok(String.format("Adicionada pulseira de identificador %d na cerca de identificador %d!", braceletId, fenceId));
		} catch (UserNotFoundException | FenceNotRegisteredException | BraceletNotRegisteredException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		} 
	}
	@DeleteMapping
	public ResponseEntity<?> deleteBraceletFence(
			@PathVariable("id") Long id,
			@PathVariable("fenceId") Long fenceId,
			@PathVariable("braceletId") Long braceletId){
		try {
			userFenceBraceletService.removeBraceletFence(id, fenceId, braceletId);
			
			return ResponseEntity.ok(String.format("Removida pulseira de identificador %d da cerca de identificador %d!", braceletId, fenceId));
		} catch (UserNotFoundException | FenceNotRegisteredException | BraceletNotRegisteredException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		} 
	}
	
}
