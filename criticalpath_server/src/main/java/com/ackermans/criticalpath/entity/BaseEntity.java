package com.ackermans.criticalpath.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import lombok.Data;

@Data
@MappedSuperclass
public abstract class BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	Date createdDateTime;	
	
	Date updatedDateTime;
	
	//Have to specify the colunDefination otherwise it takes bit(1) as datatype in database
	@Column(columnDefinition="boolean default true", nullable = false)
	Boolean isActive;
	
	//Have to specify the colunDefination otherwise it takes bit(1) as datatype in database
	@Column(columnDefinition="boolean default false", nullable = false)
	Boolean isDelete;
	
	@PrePersist
	private void setCreatedDatetime() {
		this.createdDateTime = new Date();
		this.isActive = this.isActive != null ? this.isActive : Boolean.TRUE;
		this.isDelete = this.isDelete != null ? this.isDelete : Boolean.FALSE;
	}

	@PreUpdate
	private void setUpdatedDatetime() {
		this.updatedDateTime = new Date();
		this.isActive = this.isActive != null ? this.isActive : Boolean.TRUE;
		this.isDelete = this.isDelete != null ? this.isDelete : Boolean.FALSE;
	}

	//This method will be used for audit purpose to format message properly.
	public abstract String getEntityName();	
}
