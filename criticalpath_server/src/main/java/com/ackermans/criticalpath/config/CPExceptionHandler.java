package com.ackermans.criticalpath.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.ackermans.criticalpath.common.dto.ApiResponse;
import com.ackermans.criticalpath.common.dto.CPException;
import com.ackermans.criticalpath.common.service.ResponseGenerator;
import com.ackermans.criticalpath.enums.ErrorStatus;

/**
 * Global error handling component
 *
 */
@ControllerAdvice
public class CPExceptionHandler {

	private final Logger log = LogManager.getFormatterLogger(getClass());
	
	/**
	 * Handle AmsException and send BAD_REQUEST error code
	 * 
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(CPException.class)
	public ResponseEntity<ApiResponse> handleBadRequest(final CPException ex, final WebRequest request) {
		log.error("CPException >> ", ex);
		return ResponseGenerator.errorResponse(ex, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Handle Global Exception and send INTERNAL_SERVER_ERROR error code
	 * 
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse> handleException(final Exception ex, final WebRequest request) {
		log.error("Exception >> ", ex);
		return ResponseGenerator.errorResponse(new CPException(ErrorStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
