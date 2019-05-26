package com.ackermans.criticalpath.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
public class Province extends BaseEntity {
	
	@Column(unique=true)
	private String name;
	
	@ManyToOne
	private Country country;
	
	@Column(unique=true)
	private Long thirdPartyId;
	
	@Override
	public String getEntityName() {	
		return this.getClass().getSimpleName();
	}
	
	@Override
	public String toString() {
		return this.getName();
	}

}
