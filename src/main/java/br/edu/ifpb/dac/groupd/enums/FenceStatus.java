package br.edu.ifpb.dac.groupd.enums;

import javax.persistence.AttributeConverter;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FenceStatus {
	DISABLE("Disabled", 0L),
	ACTIVE("Active", 1L);
	
	private String status;
	private Long id;
	
	private FenceStatus(String status, Long id) {
		this.status = status;
		this.id = id;
	}
	
	@JsonValue
	public String getStatus() {
		return status;
	}

	void setStatus(String status) {
		this.status = status;
	}

	Long getId() {
		return id;
	}

	void setId(Long id) {
		this.id = id;
	}
	
	public static FenceStatus getStatus(String status) {
		if(status == null)
			return null;
					
		for (FenceStatus fence:FenceStatus.values()) {
			if(fence.getStatus().equals(status)) {
				return fence;
			}
		}
		return null;
	}
}

class FenceStatusConverter implements AttributeConverter<FenceStatus, String>{

	@Override
	public String convertToDatabaseColumn(FenceStatus attribute) {
		return attribute.getStatus();
	}

	@Override
	public FenceStatus convertToEntityAttribute(String dbData) {
		FenceStatus status = FenceStatus.getStatus(dbData);
		
		if(status == null)
			throw new IllegalArgumentException(dbData);
		
		return status;
	}
	
}
