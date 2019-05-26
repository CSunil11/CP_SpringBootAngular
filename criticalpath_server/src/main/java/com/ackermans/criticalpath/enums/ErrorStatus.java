package com.ackermans.criticalpath.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

@JsonFormat(shape=Shape.OBJECT)
public enum ErrorStatus {
	
	//General Error Message
	INTERNAL_SERVER_ERROR("9999", "error.internal.server.error"),
	NO_MESSAGE_DEFINED("9998", "error.no.message.defined"),
	LINK_EXPIRED("9997","error.link.expired"),
	
	/**
	 * No account is registered with {0}
	 * <p>
	 * i.e.<br/>
	 * No accont is registered with test@test.com<br/>
	 * </p>
	 * @param email - email which is not found
	 */
	EMAIL_NOT_FOUND("9996","error.email.not.found"),
	
	/**
	 * Duplicate record found
	 */
	DUPLICATE_RECORD("9995","error.duplicate.record"),
	
	/**
	 * Should not be greater than defined value
	 * <p>
	 * i.e.<br/>
	 * Name should not be greater than 100<br/>
	 * Email should not be greater than 100
	 * </p>
	 * 
	 * @param FieldName name of field for which we want to show error message
	 * @param MaxLength	max allowed length
	 * 
	 */
	MAX_LENGTH("9994","error.max.length"),
	
	/**
	 * {0} is invalid date, format should be {1}
	 * <p>
	 * i.e.<br/>
	 * 12-12-2018 is invalid date, format should be MM/dd/yyyy<br/>
	 * </p>
	 * @param invalidDate value which is invalid
	 * @param format correct/expected format like MM/dd/yyyy
	 */
	INVALID_DATE("9993", "error.invalid.date"),
	INVALID_REQUEST("9992", "error.invalid.request"),
	INVALID_CURRENT_PASSWORD("9991", "error.invalid.current.password"),
	/**
	 * Can not send email to {0}. Please try again later.
	 * <p>
	 * i.e.<br/>
	 * Can not send email to test@test.com. Please try again later.
	 * </p>
	 * @param email email for which we can not send email
	 */
	EMAIL_SEND_FAILED("9990","error.cannot.send.email"),
	INVALID_RESET_LINK("9989","error.reset.password.link.invalid"),
	
	//StoreCloseDate error messages
	CLOSE_DATE_NOT_FOUND("1000", "error.close.date.not.found"), 
	STORES_NOT_FOUND("1001", "error.stores.note.found"),
	STORES_EXISTS("1001", "error.stores.exists"),
	CLOSE_DATE_EXISTS("1002", "error.close.date.exists"),
	
	//Stock Take Cycle error messages
	STOCK_TAKE_CYCLE_EXISTS("1003", "error.stock.take.cycle.exists"),
	
	//Country exists
	COUNTRY_EXISTS("1004", "error.country.exists"),
	
	//Task Status Exists
	TASK_STATUS_EXISTS("1005", "error.taskStatus.exists");

	private String errorCode;
	
	private String errorKey;
	
	private ErrorStatus(String errorCode, String errorKey) {
		this.errorCode = errorCode;
		this.errorKey = errorKey;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorKey() {
		return errorKey;
	}
}
