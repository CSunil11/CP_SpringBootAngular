package com.ackermans.criticalpath.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	/**
	 * MM/dd/yyyy
	 */
	public static final String DATE_FORMAT = "MM/dd/yyyy";
	
	/**
	 * Formatter for MM/dd/yyyy
	 */
	public static final SimpleDateFormat FORMATTER = new SimpleDateFormat(DATE_FORMAT);
	
	/**
	 * Get current date in MM/dd/yyyy format
	 * 
	 * @return
	 */
	public static String getCurrentDate() {
		return FORMATTER.format(new Date());
	}
	
	/**
	 * Get date object for given date string in MM/dd/yyyy format
	 * 
	 * @return
	 * @throws ParseException 
	 */
	public static Date getDate(String dateStr) throws ParseException {
		return FORMATTER.parse(dateStr);
	}
	
	/**
	 * Get differnce between two dates into minutes
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long getDiffInMin(Date date1, Date date2) {
		return getDiffInMin(date1.getTime(), date2.getTime());
	}
	
	/**
	 * Get difference between current date and given date in minutes
	 * 
	 * @param date
	 * @return
	 */
	public static long getDiffInMin(Date date) {
		return getDiffInMin(new Date(), date);
	}
	
	/**
	 * Get difference between current miliseconds and given miliseconds in minutes
	 * 
	 * @param miliseconds
	 * @return
	 */
	public static long getDiffInMin(long miliseconds) {
		return getDiffInMin(new Date().getTime(), miliseconds);
	}
	
	/**
	 * Get differnce between two miliseconds into minutes
	 * 
	 * @param miliseconds1
	 * @param miliseconds2
	 * @return
	 */
	public static long getDiffInMin(long miliseconds1, long miliseconds2) {
		long diffMs = miliseconds1 - miliseconds2;
		long diffSec = diffMs / 1000;
		long min = diffSec / 60;
		return min;
	}

	/**
	 * Get current date in YYYY-MM-DDTHH:mm:ss.A format
	 * i.e. 2019-03-14T10:06:08.671518000
	 * 
	 * @return
	 */
	public static String getCurrentDateInISOFormat() {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSS");
		return dateFormatter.format(new Date());
	}

	/**
	 * Get given date in yyyy-MM-dd format
	 * 
	 * @param cycleCompletionDate
	 * @return
	 */
	public static String dateToString(Date cycleCompletionDate) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormatter.format(cycleCompletionDate);
	}
}
