package com.ackermans.criticalpath.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
public class User extends BaseEntity {

	private String name;
	
	private String phone;
	
	@ManyToOne
	private Brand brand;
	
	@ManyToOne
	private Store store;
	
	private Long thirdPartyId;
	
	@OneToOne(fetch=FetchType.LAZY)
	private UserLogin userLogin;
	
	private Boolean isResetLinkExpired;

	@Override
	public String getEntityName() {
		return this.getClass().getSimpleName();
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
}
