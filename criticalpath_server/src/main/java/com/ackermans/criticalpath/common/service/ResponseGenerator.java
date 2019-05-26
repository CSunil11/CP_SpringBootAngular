package com.ackermans.criticalpath.common.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ackermans.criticalpath.common.dto.ApiResponse;
import com.ackermans.criticalpath.common.dto.CPException;
import com.ackermans.criticalpath.enums.ErrorStatus;
import com.ackermans.criticalpath.enums.SuccessStatus;

@Component
public class ResponseGenerator {

	private static Logger logger = LogManager.getFormatterLogger(ResponseGenerator.class);
	
	private static String ERROR_STATUS = "ERROR";
	
	private static String SUCCESS_STATUS = "SUCCESS";
	
	/**
	 * Generate a error response with required details 
	 * 
	 * @param ex
	 * @param httpStatus
	 * @return
	 */
	public static ResponseEntity<ApiResponse> errorResponse(CPException ex, HttpStatus httpStatus) {
		
		ErrorStatus errorStatus = ex.getStatus();
		String message;
		String errorCode = null;
		
		//If there is field validation error else exception is thrown
		if(ex.getFieldError() != null) {
			
			errorStatus = getErrorStatus(ex.getFieldError().getDefaultMessage());
			
			//If error status is null then return default error message provided by validation framework
			if(errorStatus == null) {
				message = ex.getFieldError().getDefaultMessage();
			} else {
				message = PropertyMessageReader.getMessage(errorStatus.getErrorKey(), ex.getData());
				errorCode = errorStatus.getErrorCode();
			}
			
		} else {
			
			message = PropertyMessageReader.getMessage(errorStatus.getErrorKey(), ex.getData());
			errorCode = errorStatus.getErrorCode();
		}
		
		return new ResponseEntity<ApiResponse>(
			new ApiResponse(
					ERROR_STATUS,
					errorCode, 
					message, 
					null),
			httpStatus);
	}
	
	/**
	 * Generate a success response with required details 
	 * 
	 * @param responseData
	 * @return
	 */
	public static ResponseEntity<ApiResponse> successResponse(Object responseData) {
		return new ResponseEntity<ApiResponse>(
				new ApiResponse(
						SUCCESS_STATUS, 
						null, 
						null, 
						responseData)
				, HttpStatus.OK);
	}
	
	/**
	 * Generate a success response with required details 
	 * 
	 * @param successStatus
	 * @param responseData
	 * @return
	 */
	public static ResponseEntity<ApiResponse> successResponse(SuccessStatus successStatus, Object responseData) {
		return ResponseGenerator.successResponse(successStatus, responseData, new String[0]);
	}

	/**
	 * Generate a success response with required details. MessageParam is the parameter which is required to generate message.
	 * <br>
	 * <b>For example: </b><br>
	 * success.office.save = <b>{0}</b> saved successfully. <br/>
	 * MessageParam will be replaced at <b>{0}</b>
	 * 
	 * @param successStatus
	 * @param responseData
	 * @param httpStatus
	 * @return
	 */
	public static ResponseEntity<ApiResponse> successResponse(SuccessStatus successStatus, Object responseData, String... messageParam) {
		return new ResponseEntity<ApiResponse>(
				new ApiResponse(
						SUCCESS_STATUS, 
						null, 
						PropertyMessageReader.getMessage(successStatus.getMessageKey(), messageParam), 
						responseData)
				, HttpStatus.OK);
	}
	
	/**
	 * Get ErrorStatus from string, if incorrect string return null
	 *  
	 * @param errorStatusString
	 * @return
	 */
	private static ErrorStatus getErrorStatus(String errorStatusString) {
		
		try {
			return ErrorStatus.valueOf(errorStatusString);
		} catch (Exception e) {
			logger.error("Exception while getting ErrorStatus from key :: " + errorStatusString, e);
			return null;
		}
	}
}
