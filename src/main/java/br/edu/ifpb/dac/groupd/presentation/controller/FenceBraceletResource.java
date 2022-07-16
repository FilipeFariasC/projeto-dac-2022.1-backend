package br.edu.ifpb.dac.groupd.presentation.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.dac.groupd.business.exception.AbstractException;
import br.edu.ifpb.dac.groupd.business.service.FenceBraceletService;

@RestController
@RequestMapping({"/fences","/users/fences"})
public class FenceBraceletResource {
	
	@Autowired
	private FenceBraceletService fenceBraceletService;
	
	@PostMapping("/registerBracelet")
	public ResponseEntity<?> addBraceletFence(
			Principal principal,
			@RequestParam(name="fence", required=true) Long fenceId,
			@RequestParam(name="bracelet", required=true) Long braceletId)
		throws AbstractException{
		fenceBraceletService.addBraceletFence(getPrincipalId(principal), fenceId, braceletId);
		
		return ResponseEntity.ok(String.format("Adicionada pulseira de identificador %d na cerca de identificador %d!", braceletId, fenceId));
	}
	
	@DeleteMapping("removeBracelet")
	public ResponseEntity<?> deleteBraceletFence(
			Principal principal,
			@RequestParam(name="fence", required=true) Long fenceId,
			@RequestParam(name="bracelet", required=true) Long braceletId)
	throws AbstractException{
		fenceBraceletService.removeBraceletFence(getPrincipalId(principal), fenceId, braceletId);
		
		return ResponseEntity.ok(String.format("Removida pulseira de identificador %d da cerca de identificador %d!", braceletId, fenceId));
		
	}
	private Long getPrincipalId(Principal principal) {
		return Long.parseLong(principal.getName());
	}
}
