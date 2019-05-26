package com.ackermans.criticalpath.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class Store extends BaseEntity {

	@Column(unique = true, length = 30)
	private String code;

	private String name;
	
	@Column(unique=true)
	private Long thirdPartyId;
	
	@ManyToOne
	private Country country;

	@ManyToOne
	private Brand brand;
	
	@ManyToOne
	private LocationDivision locationDivision;
	
	@ManyToOne
	private Province province;
	
	@ManyToOne
	private StockTakeCycle stockTakeCycle;
	
	@ManyToOne
	@JoinColumn(name="ram_user_id")
	private User regionalManagers;

	@Override
	public String getEntityName() {
		return this.getClass().getSimpleName();
	}
	
	@Override
	public String toString() {
		return this.getName();
	}

}
