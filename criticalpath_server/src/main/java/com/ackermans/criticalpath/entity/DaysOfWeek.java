package com.ackermans.criticalpath.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
public class DaysOfWeek extends BaseEntity {

	@Column(unique=true)
	private String name;
	
	@Override
	public String getEntityName() {
		return this.getClass().getSimpleName();
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
}
