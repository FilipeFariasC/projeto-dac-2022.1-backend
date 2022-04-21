package br.edu.ifpb.dac.groupd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.dto.post.FencePostDto;
import br.edu.ifpb.dac.groupd.model.Fence;
import br.edu.ifpb.dac.groupd.repository.FenceRepository;

@Service
public class FenceService {
	@Autowired
	private FenceRepository fenceRepo;
	/*
	public Fence create(FencePostDto postDto) {
		
	}
	*/
}
