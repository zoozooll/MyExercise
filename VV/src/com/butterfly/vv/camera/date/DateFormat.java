package com.butterfly.vv.camera.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author andyhe
 * @create 2014-08-07
 * @version 1.0
 * @desc 日期格式化常用的一些接口都写到这个文件里面
 */
public class DateFormat extends Date {
	private static final long serialVersionUID = 1L;
	public static final String Format_yyyy_MM_dd = "yyyy-MM-dd";
	public static final String Format_yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";

	public DateFormat() {
		super();
	}
	/**
	 * 1970年的秒差时间，格式化成自己想要的日期格式
	 * @param time 1970的秒差，
	 * @param format 要格式化成什么格式
	 * @return String 返回格式化后的日期字符串
	 */
	public static String getDateByDoubleTime(double time, String format) {
		Date date = new Date((long) (time * 1000));
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
	public static Date getDateByString(String date, String format)
			throws ParseException {
		SimpleDateFormat fmt = new SimpleDateFormat(format);
		Date date2 = fmt.parse(date);
		return date2;
	}
	public static long getCurrentTime() {
		return (Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
	}
	public static boolean isToday(double time) {
		boolean isToday = false;
		Date date1 = new Date((long) (time * 1000));
		isToday = DateFormat.isSameDay(date1, new Date());
		return isToday;
	}
	public boolean isSameDay(Date date) {
		return DateFormat.isSameDay(this, date);
	}
}
