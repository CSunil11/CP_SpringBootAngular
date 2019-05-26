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
public class StockTakeCycle extends BaseEntity {

	@Column(unique=true)
	private String name;
	
	private String length;
	
	private String doNotCount;
	
	private Date stokeTakeDate;

	private Boolean isDisplay;
	
	@ManyToOne
	private User createdBy;
	
//	@ManyToMany
//	@JoinTable(name="store_stock_take_cycle", 
//		joinColumns = {@JoinColumn(name="stock_take_cycle_id")}, 
//		inverseJoinColumns = {@JoinColumn(name="store_id")})
//	private Set<Store> stores;

	public String getEntityName() {
		return this.getClass().getSimpleName();
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
}
