package com.mogoo.market.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	private static final SimpleDateFormat DATE_YYYY_MM_DD = new SimpleDateFormat(
			"yyyy-MM-dd");
	private static final SimpleDateFormat DATE_YYYYMMDDHHMM = new SimpleDateFormat(
			"yyyy-MM-dd");

	/**
	 * 返回时间，Date 类型
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date getDateYYYYMMDDHHMM(String dateStr) {
		Date date = null;
		try {
			date = DATE_YYYYMMDDHHMM.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 返回时间 字符串
	 * 
	 * @param dateStr
	 * @return
	 */
	public static String getDateStrYYYYMMDDHHMM(String dateStr) {

		return DATE_YYYYMMDDHHMM.format(getDateYYYYMMDDHHMM(dateStr));
	}

	public static String getDateStrYYYY_MM_DD(String dateStr) {

		return DATE_YYYY_MM_DD.format(getDateYYYYMMDDHHMM(dateStr));
	}

	public static String getDateStrYYYY_MM_DD() {
		return DATE_YYYY_MM_DD.format(new Date());
	}

	// test
	public static void main(String[] args) {

		System.out.println(getDateStrYYYY_MM_DD());
		System.out.println(getDateStrYYYYMMDDHHMM("2012-01-09 10:23:33"));
	}

}
