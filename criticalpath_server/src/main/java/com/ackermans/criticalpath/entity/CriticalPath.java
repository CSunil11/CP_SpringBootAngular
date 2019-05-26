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

import com.ackermans.criticalpath.utils.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
public class CriticalPath extends BaseEntity {

	@Column(unique=true)
	private String title;
	
	private String description;
	
	private int startDay;
	
	private int length;
	
	private Date completeDate;
	
	private String completedBy;
	
	@ManyToOne
	private TaskStatus status;
	
	@ManyToOne
	private StockTakeCycle cycle;
	
	@ManyToOne
	private UserLogin createdBy;
	
	@ManyToMany
	@JoinTable(name="store_critical_path", 
		joinColumns = {@JoinColumn(name="critical_path_id")}, 
		inverseJoinColumns = {@JoinColumn(name="store_id")})
	private Set<Store> stores;

	@Override
	@JsonIgnore
	public String getEntityName() {
		return this.getClass().getSimpleName();
	}
	
	@Override
	public String toString() {
		return this.getTitle();
	}
}
