package com.ackermans.criticalpath.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
public class AuditLog extends BaseEntity {
	
	@Column(columnDefinition="text", nullable=false)
	private String message;
	
	private String actionBy;

	@Override
	public String getEntityName() {
		// TODO Auto-generated method stub
		this.getClass();
		return this.getClass().getSimpleName();
	}

}
