package com.ackermans.criticalpath.common.dto;

import org.springframework.validation.FieldError;

import com.ackermans.criticalpath.enums.ErrorStatus;

public class CPException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private ErrorStatus status;

	private Object[] data;

	private FieldError fieldError;
	
	public CPException(String errorMessage) {
		super(errorMessage);
	}
	
	public CPException(ErrorStatus status, Exception ex) {
		super(ex);
		this.status = status;
	}
	
	public CPException(ErrorStatus status, Object... data) {
		super(status.name());
		this.status = status;
		this.data = data;
	}

	public CPException(FieldError fieldError, Object... data) {
		this.fieldError = fieldError;
		this.data = data;
	}

	public ErrorStatus getStatus() {
		return this.status;
	}

	public Object[] getData() {
		return data;
	}

	public FieldError getFieldError() {
		return fieldError;
	}
}
