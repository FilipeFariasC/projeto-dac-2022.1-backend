package br.edu.ifpb.dac.groupd.business.service.converter;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.model.entities.Fence;
import br.edu.ifpb.dac.groupd.presentation.dto.FenceRequest;
import br.edu.ifpb.dac.groupd.presentation.dto.FenceResponse;
import br.edu.ifpb.dac.groupd.presentation.dto.FenceResponseMin;

@Service
public class FenceConverterService {
	
	@Autowired
	private ModelMapper mapper;
	
	
	public Fence requestToFence(FenceRequest dto ) {
		Fence fence = mapper.map(dto, Fence.class); 
		
		return fence;
	}
	public FenceResponse fenceToResponse(Fence fence) {
		FenceResponse response = mapper.map(fence, FenceResponse.class); 
		
		return response;
	}
	public FenceResponseMin fenceToResponseMin(Fence fence) {
		FenceResponseMin responseMin = mapper.map(fence, FenceResponseMin.class); 
		
		return responseMin;
	}
	
	public List<FenceResponse> fencesToResponses(List<Fence> fences) {
		return fences.stream()
				.map(this::fenceToResponse)
				.toList();
	}
	
	public List<FenceResponseMin> fencesToResponsesMin(List<Fence> fences) {
		return fences.stream()
				.map(this::fenceToResponseMin)
				.toList();
	}

}
