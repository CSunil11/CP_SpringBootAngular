package com.ackermans.criticalpath.entity;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
public class ApiLogger extends BaseEntity {
	
	private Long transactionId;
	
	private String msgId;
	
	private String module;
	
	private Long thirdPartyId;
	
	private String status;
	
	private String actionBy;
	
	private String remark;
	
	@Override
	public String getEntityName() {
		return this.getClass().getSimpleName();
	}

}
