package com.ackermans.criticalpath.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
public class CloseDate extends BaseEntity {

	@Column(unique=true)
	private String name;
	
	@Column(unique=true)
	private Date closeDate;
	
	private String description;
	
	@ManyToMany(cascade=CascadeType.MERGE)
	@JoinTable(name="store_close_date", 
		joinColumns = {@JoinColumn(name="close_date_id")}, 
		inverseJoinColumns = {@JoinColumn(name="store_id")})
	private Set<Store> stores;
	
	@Override
	@JsonIgnore
	public String getEntityName() {
		return this.getClass().getSimpleName();
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
}
