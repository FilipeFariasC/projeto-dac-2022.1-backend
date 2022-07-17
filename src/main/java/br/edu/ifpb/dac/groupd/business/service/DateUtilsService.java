package br.edu.ifpb.dac.groupd.business.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class DateUtilsService {
	
	public LocalDateTime convertDateToLocalDateTime(Date date) {
	    return date.toInstant()
	      .atZone(ZoneId.systemDefault())
	      .toLocalDateTime();
	}

}
