package com.ackermans.criticalpath.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
public class Event extends BaseEntity {

	public Event() {}
	
	public Event(String name) {
		this.name = name;
	}
	
	@Column(unique=true, length=30)
	private String name;
	
	private String description;
	
	@Override
	public String getEntityName() {
		return this.getClass().getSimpleName();
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
}
