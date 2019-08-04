package com.butterfly.vv.camera;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author andyhe
 * @create 2014-08-07
 * @version 1.0
 * @desc 日期格式化常用的一些接口都写到这个文件里面
 */
public class MyDateFormat extends Date {
	public static final String Format_yyyy_MM_dd = "yyyy-MM-dd";
	public static final String Format_yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";
	public static final long MIN_IN_MILLIS = 60 * 1000;
	public static final long HOUR_IN_MILLIS = MIN_IN_MILLIS * 60;
	public static final long DAY_IN_MILLIS = HOUR_IN_MILLIS * 24;

	public MyDateFormat() {
		super();
	}
	/**
	 * 1970年的秒差时间，格式化成自己想要的日期格式
	 * @param startTime 1970的秒差，
	 * @param format 要格式化成什么格式
	 * @return String 返回格式化后的日期字符串
	 */
	public static String getDateByTimeMillis(long timeInMillis, String format) {
		Date date = new Date(timeInMillis);
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String dateStr = formatter.format(date);
		return dateStr;
	}
	/**
	 * 判断两个日期是不是同一天
	 * @param day1 要比较的日期1，
	 * @param day2 要比较的日期2
	 * @return boolean 返回true表示两个日期是同一天，否则不是同一天
	 */
	public static boolean isSameDay(Date day1, Date day2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String ds1 = sdf.format(day1);
		String ds2 = sdf.format(day2);
		if (ds1.equals(ds2)) {
			return true;
		} else {
			return false;
		}
	}
	// 从字符串中获取日期
	public static Date getDateByString(String date, String format)
			throws ParseException {
		SimpleDateFormat fmt = new SimpleDateFormat(format);
		Date date2 = fmt.parse(date);
		return date2;
	}
	public static boolean isDateLatest(String date1Str, String date2Str) {
		String format = "yyyy:MM:dd HH:mm:ss";
		SimpleDateFormat fmt = new SimpleDateFormat(format);
		Date date1, date2;
		boolean ret = false;
		try {
			date1 = fmt.parse(date1Str);
			date2 = fmt.parse(date2Str);
			ret = date1.getTime() > date2.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	// 获取当前时间
	public static long getCurrentTime() {
		return (new Date().getTime() / 1000);
	}
	// 判断是否是今天
	public static boolean isToday(double time) {
		boolean isToday = false;
		Date date1 = new Date((long) (time * 1000));
		isToday = MyDateFormat.isSameDay(date1, new Date());
		return isToday;
	}
	// 判断是否时同一天
	public boolean isSameDay(Date date) {
		return MyDateFormat.isSameDay(this, date);
	}
	public static String getIntervalTimeFromMillis(long millis) {
		String retStr = null;
		if (millis > DAY_IN_MILLIS) {
			long days = millis / DAY_IN_MILLIS;
			retStr = days + "天前";
		} else if (millis > HOUR_IN_MILLIS) {
			long hours = millis / HOUR_IN_MILLIS;
			retStr = hours + "小时前";
		} else if (millis > MIN_IN_MILLIS) {
			long mins = millis / MIN_IN_MILLIS;
			retStr = mins + "分钟前";
		} else if (millis >= 0) {
			retStr = "1分钟前";
		} else {
			retStr = "现在";
		}
		return retStr;
	}
	public static String getIntervalDayFromMillis(long millis) {
		String retStr = null;
		if (millis > DAY_IN_MILLIS) {
			long days = millis / DAY_IN_MILLIS;
			retStr = days + "天前";
		} else if (millis > 0) {
			retStr = "今天";
		} else {
			retStr = "未知";
		}
		return retStr;
	}
}
