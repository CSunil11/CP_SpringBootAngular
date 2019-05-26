package com.ackermans.criticalpath.common.dto;

import lombok.Getter;

@Getter
public class ApiResponse {

	//SUCCESS or ERROR
	private String status;
	
	private String errorCode;
	
	private String message;
	
	private Object data;
	
	public ApiResponse(String status, String errorCode, String message, Object data) {
		this.status = status;
		this.errorCode = errorCode;
		this.message = message;
		this.data = data;
	}
}
