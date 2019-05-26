package com.ackermans.criticalpath.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

@JsonFormat(shape=Shape.OBJECT)
public enum SuccessStatus {

	//User Success
	USER_SAVE("success.user.save"),
	USER_UPDATE("success.user.update"),
	USER_DO_ACTIVE("success.user.do.active"),
	USER_DO_INACTIVE("success.user.do.inactive"),
	
	//Country Success
	ADD_COUNTRY("success.country.add"),
	COUNTRY_EXISTS("success.country.exists"),

	//Email verification
	RESET_LINK_SENT("success.reset.link.sent"),
	LINK_VERIFIED("success.link.veryfied"),
	PASSWORD_CHANGED("success.password.changed"),
	
	//General Success Message
	NO_RECORDS_FOUND("success.no.records.found"), 
	SUCCESS_DELETE("success.delete"),
	SUCCESS_RESTORE("success.restore"),
	SUCCESS_LOGOUT("success.logout");
	
	private String messageKey;
	
	private SuccessStatus(String messageKey) {
		this.messageKey = messageKey;
	}

	public String getMessageKey() {
		return messageKey;
	}
}
