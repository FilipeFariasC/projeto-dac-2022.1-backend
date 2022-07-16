package br.edu.ifpb.dac.groupd.business.service.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.model.entities.Alarm;
import br.edu.ifpb.dac.groupd.presentation.dto.AlarmResponse;
import br.edu.ifpb.dac.groupd.presentation.dto.AlarmResponseMin;

@Service
public class AlarmConverterService {
	
	@Autowired
	private ModelMapper mapper;
	
	public AlarmResponse alarmToResponse(Alarm alarm) {
		return mapper.map(alarm, AlarmResponse.class);
	}
	public AlarmResponseMin alarmToResponseMin(Alarm alarm) {
		return mapper.map(alarm, AlarmResponseMin.class);
	}


}
