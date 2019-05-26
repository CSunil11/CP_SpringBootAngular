package com.ackermans.criticalpath.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
public class LocationDivision extends BaseEntity {
	
	@Column(unique=true)
	private String name;
	
	@OneToOne
	@JoinColumn(name="user_id")
	private User divisionalSalesManagers;
	
//	@OneToOne
//	@JoinColumn(name="ram_user_id")
//	private User regionalManagers;
	
	@ManyToOne
	private Country country;
	
	@ManyToOne
	private Province province;
	
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
