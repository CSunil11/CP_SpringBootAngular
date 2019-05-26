package com.ackermans.criticalpath.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Permission extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String permKey;

	private String description;
	
	private Long displayOrder;

	@Override
	public String getEntityName() {
		return this.getClass().getSimpleName();
	}
}
