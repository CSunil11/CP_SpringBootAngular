package com.ackermans.criticalpath.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
public class AckermanLogger extends BaseEntity {
	
	@Column(unique=true)
	private String taskId;
	
	private String userId;
	
	@OneToOne
	private User createdBy;
	
	private String remarks;
	
	@Override
	public String getEntityName() {
		// TODO Auto-generated method stub
		return null;
	}

}
