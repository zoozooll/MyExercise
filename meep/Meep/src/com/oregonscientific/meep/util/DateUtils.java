/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.util;

import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * A class containing utility methods related to Date
 * 
 * @author Stanley Lam
 */
public class DateUtils {

	/**
	 * The Date is before the another another Date
	 */
	public static final int BEFORE = -1;
	
	/**
	 * The 2 Date are equal
	 */
	public static final int EQUAL = 0;
	
	/**
	 * The Date is after the another another Date
	 */
	public static final int AFTER = 1;
	
	/**
	 * Calculate day difference between two days
	 * 
	 * @param d1 The first date to be compared
	 * @param d2 Another date compared with d1
	 * @return the difference (in days) between the 2 dates
	 */
	public static int dayDifference(Date d1, Date d2) {
		DateTime dt1 = new DateTime(d1.getTime());
		DateTime dt2 = new DateTime(d2.getTime());
		Days days = Days.daysBetween(dt1, dt2); 
		return days.getDays();
	}
	
	/**
	 * Compare the 2 Dates and determine whether {@code d1} is equal to, before or after {@code d2}
	 * 
	 * @param d1 The Date to compare with {@code d2}
	 * @param d2 The Date to compare with {@code d1}
	 * @return 0 if the times of the two Dates are equal, -1 if the time of 
	 * {@code d1} is before {@code d2}, 1 if the time of {@code d1} is after {@code d2}.
	 */
	public static int compare(Date d1, Date d2) {
		Calendar day1 = Calendar.getInstance();
		Calendar day2 = Calendar.getInstance();
		
		day1.setTime(d1);
		day2.setTime(d2);
		
		return day1.compareTo(day2);
	}
}
