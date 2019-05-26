package com.ackermans.criticalpath.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
public class AckStockTakeCycle extends BaseEntity {

	@Column(unique=false)
	private String name;
	
	private String length;
	
	private String doNotCount;
	
	private Date stokeTakeDate;
	
	private Date stokeStartDate;

	private Boolean isDisplay;
	
	private String time;
	
	@Column(columnDefinition="boolean default false", nullable = false)
	Boolean isCompleted;
	
	@ManyToOne
	private User createdBy;
	
	@ManyToOne
	private Store store;

	public String getEntityName() {
		return this.getClass().getSimpleName();
	}
}
