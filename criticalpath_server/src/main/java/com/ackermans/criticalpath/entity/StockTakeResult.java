package com.ackermans.criticalpath.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
public class StockTakeResult extends BaseEntity {
	
	@Column(columnDefinition = "TEXT")
	private String stockTakeResultObject;
	
	@ManyToOne
	private Store store;
	
	private Date dateRam;
	
	@Column(columnDefinition = "TEXT")
	private String reasonRam;
	
	private String statusRam;
	
	private Date dateDsm;
	
	@Column(columnDefinition = "TEXT")
	private String reasonDsm;
	
	private String statusDsm;
	
	private Date dateSm;
	
	@Column(columnDefinition = "TEXT")
	private String reasonSm;
	
	private String statusSm;
	
	private String statusFinal;
	
	@Override
	public String getEntityName() {
		return this.getClass().getSimpleName();
	}


}
