package com.ackermans.criticalpath.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import org.springframework.http.HttpMethod;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
public class Webhook extends BaseEntity {
	
	@Column(unique=true)
	private String url;
	
	@Enumerated(EnumType.STRING)
	HttpMethod method;
	
	@ManyToOne(cascade=CascadeType.ALL)
	private Event event;
	
	
	@Override
	public String getEntityName() {
		return this.getClass().getSimpleName();
	}
	
	@Override
	public String toString() {
		return this.getUrl();
	}

}
