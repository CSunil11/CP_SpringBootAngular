package com.ackermans.criticalpath.admin.dto;

import java.util.Set;

import com.ackermans.criticalpath.entity.Store;

import lombok.Data;

@Data
public class StoreCloseDateRequest {

	private Long id;
	
	private String name;
	
	private String description;
	
	private String closeDate;
	
	private Boolean isActive;
	
	private Set<Store> stores;
}
