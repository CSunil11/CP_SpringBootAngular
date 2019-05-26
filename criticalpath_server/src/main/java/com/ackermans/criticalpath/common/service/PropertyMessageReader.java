package com.ackermans.criticalpath.common.service;

import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.ackermans.criticalpath.enums.ErrorStatus;
import com.ackermans.criticalpath.enums.SuccessStatus;

@Service
public class PropertyMessageReader {

	private static Logger logger = LogManager.getFormatterLogger(PropertyMessageReader.class);

	private static MessageSource messageSource;

	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		PropertyMessageReader.messageSource = messageSource;
	}

	/**
	 * To read only success message
	 * 
	 * @param successStatus
	 * @param data
	 * @return
	 */
	public static String getSuccessMessage(SuccessStatus successStatus, String... data) {
		return getMessage(successStatus.getMessageKey(), data);
	}
	
	/**
	 * To read only error messages
	 * 
	 * @param errorStatus
	 * @param data
	 * @return
	 */
	public static String getErrorMessage(ErrorStatus errorStatus, String... data) {
		return getMessage(errorStatus.getErrorKey(), data);
	}
	
	/**
	 * Get message for given key, if key is incorrect then key will be returned as message
	 * 
	 * @param key
	 * @param data
	 * @return
	 */
	public static String getMessage(String key, Object[] data) {
		try {
			return messageSource.getMessage(key, data, Locale.US);
		} catch (Exception e) {
			logger.error("No message found for key :: " + key, e);
			return getMessage(ErrorStatus.NO_MESSAGE_DEFINED.getErrorKey(), null);
		}
	}
}
