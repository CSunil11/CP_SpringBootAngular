package com.ackermans.criticalpath.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
public class ManageNotification extends BaseEntity {

	@Column(unique=true)
	private String name;
	
	private String description;
	
	private String subject;
	
	private String body;
	
	private String userGroups;
	
	private String triggerEvent;
	
	private Long delayTime;
	
	private Long reminderTime;
	
	private Long reminderCount;
	
	private String notificationType;
	
	@Override
	public String getEntityName() {
		
		return this.getClass().getSimpleName();
	}
	
	@Override
	public String toString() {
		return this.getName();
	}

}
