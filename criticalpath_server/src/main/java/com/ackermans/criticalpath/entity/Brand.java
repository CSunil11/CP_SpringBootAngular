package com.ackermans.criticalpath.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.ackermans.criticalpath.utils.Constants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
public class Brand extends BaseEntity {
	
	@Column(unique=true, length=Constants.EVENT_NAME_LENGTH)
	private String name;
	
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
