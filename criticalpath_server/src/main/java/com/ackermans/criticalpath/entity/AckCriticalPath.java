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
public class AckCriticalPath extends BaseEntity {

	@Column(unique=false)
	private String title;
	
	private String description;
	
	private int startDay;
	
	private int length;
	
	private Date completeDate;
	
	private String completedBy;
	
	@ManyToOne
	private TaskStatus status;
	
	@ManyToOne
	private AckStockTakeCycle cycle;
	
	@ManyToOne
	private UserLogin createdBy;
	
	@Override
	@JsonIgnore
	public String getEntityName() {
		return this.getClass().getSimpleName();
	}
}
