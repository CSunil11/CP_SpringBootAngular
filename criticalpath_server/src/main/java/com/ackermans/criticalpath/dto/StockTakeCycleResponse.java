package com.ackermans.criticalpath.dto;

import com.ackermans.criticalpath.entity.StockTakeCycle;

import lombok.Data;

@Data
public class StockTakeCycleResponse {
	
	private String name;

	private String length;

	private String doNotCount;
	
	private String createdBy;
	
	public static StockTakeCycleResponse fromEntity(StockTakeCycle stokeTakeCycle) {
		StockTakeCycleResponse stokeTakeCycleResponse = new StockTakeCycleResponse() ;
		
		stokeTakeCycleResponse.setName(stokeTakeCycle.getName());
		stokeTakeCycleResponse.setLength(stokeTakeCycle.getLength());
		stokeTakeCycleResponse.setDoNotCount(stokeTakeCycle.getDoNotCount());
		stokeTakeCycleResponse.setCreatedBy(stokeTakeCycle.getCreatedBy().getName());
		
		return stokeTakeCycleResponse;
	}
}
