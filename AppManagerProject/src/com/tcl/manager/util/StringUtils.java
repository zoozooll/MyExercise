package com.tcl.manager.util;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.tcl.manager.application.ManagerApplication;

public class StringUtils {
	
	private static final long KB = 1024;
	
	private static final long MB = 1024 * 1024;
	
	private static final long GB = 1024 * 1024 * 1024;
	
	private static final long SECOND_TO_MINUTE = 60;
	
	private static final long SECOND_TO_HOUR = 60 * 60;

	public static boolean isNull(String str) {
		return str == null;
	}

	// 字符串为空
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	// 字符串（包括中文）长度
	public static int strLen(String s) {
		int length = 0;
		for (int i = 0; i < s.length(); i++) {
			int ascii = Character.codePointAt(s, i);
			if (ascii >= 0 && ascii <= 255)
				length++;
			else
				length += 2;

		}
		return length;
	}

	public static String substring(String s, int subLen) {
		return substring(s, subLen, "..");
	}

	public static String substring(String s, int subLen, String postfix) {
		int length = 0;
		for (int i = 0; i < s.length(); i++) {
			int ascii = Character.codePointAt(s, i);
			if (ascii >= 0 && ascii <= 255) {
				length++;
			} else {
				length += 2;
			}
			if (length > subLen || (length == subLen && i + 1 != s.length())) {
				return s.substring(0, i).concat(postfix);
			}
		}
		return s;
	}

	public static byte[] getBytesUtf8(String string) {
		return StringUtils.getBytesUnchecked(string, "utf-8");
	}
	
	public static String parseBytesStringAuto(long bytesLen) {
		String str = "";
		float result = 0f;
		if (bytesLen >= GB) {
			result = (float)bytesLen / (float)GB;
			str = String.format("%.1f GB", result);
		} else if (bytesLen >= MB) {
			result = (float)bytesLen / (float)MB;
			str = String.format("%.1f MB", result);
		} else if (bytesLen >= KB) {
			result = (float)bytesLen / (float)KB;
			str = String.format("%.1f KB", result);
		} else {
			str = String.format("%d Bytes", bytesLen);
		}
		return str;
	}
	
	public static String parseSecondsStringAuto(long seconds) {
		String str = "";
		long hours = 0L, mins = 0L, secs = 0L;
		if (seconds >= SECOND_TO_HOUR) {
			hours = seconds / SECOND_TO_HOUR;
			seconds = seconds % SECOND_TO_HOUR;
			mins = seconds / SECOND_TO_MINUTE;
			str = String.format("%d hours %d min", hours, mins);
		} else if (seconds >= SECOND_TO_MINUTE) {
			mins = seconds / SECOND_TO_MINUTE;
			str = String.format("%d min", mins);
		} else {
			str = String.format("%d sec", seconds);
		}
		return str;
	}
	
	public static String parseMinutesStringAuto(long seconds) {
		String str = "";
		float hours = 0L, mins = 0L, secs = 0L;
		if (seconds >= SECOND_TO_HOUR) {
			hours = (float)seconds / (float)SECOND_TO_HOUR;
			str = String.format("%.1f hours", hours);
		} else {
			mins = (float)seconds/ (float)SECOND_TO_MINUTE;
			str = String.format("%.1f min", mins);
		}
		return str;
	}

	public static byte[] getBytesUnchecked(String string, String charsetName) {
		if (string == null) {
			return null;
		}
		try {
			return string.getBytes(charsetName);
		} catch (UnsupportedEncodingException e) {
			throw StringUtils.newIllegalStateException(charsetName, e);
		}
	}

	private static IllegalStateException newIllegalStateException(
			String charsetName, UnsupportedEncodingException e) {
		return new IllegalStateException(charsetName + ": " + e);
	}

	public static Date stringToDate(String str) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {

		}
		return date;
	}
	public static String timeToDate(String str) {
		try{
			Date d = new Date(Long.parseLong(str));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(d);
		}catch(Exception e){
			
		}
		return "";
	}
	
	public static String timeToDate(Long time) {
		Date d = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		return sdf.format(d);
	}
	
	/**
	 * 阿拉伯语中String.format的时候，会将数字转化成阿拉伯语
	 * 为什么呢？Android阿拉伯文机子中时间日期本地语言中没有0-9，设置语言无效
	 * 
	 * yyyy-MM-DD hh:mm:ss
	 * 
	 * @param time
	 * @return
	 */
	public static String getTime2GMT(){
		StringBuffer sb  = new StringBuffer();
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.US);
		
		sb.append(calendar.get(Calendar.YEAR));
		sb.append("-");
		if(calendar.get(Calendar.MONTH) + 1< 10){
			sb.append(0); //补零
		}
		sb.append(calendar.get(Calendar.MONTH) + 1);
		sb.append("-");
		if(calendar.get(Calendar.DAY_OF_MONTH) < 10){
			sb.append(0); //补零
		}
		sb.append(calendar.get(Calendar.DAY_OF_MONTH));
		sb.append(" ");
		if(calendar.get(Calendar.HOUR_OF_DAY) < 10){
			sb.append(0); //补零
		}
		sb.append(calendar.get(Calendar.HOUR_OF_DAY));
		sb.append(":");
		if(calendar.get(Calendar.MINUTE) < 10){
			sb.append(0); //补零
		}
		sb.append(calendar.get(Calendar.MINUTE));
		sb.append(":");
		if(calendar.get(Calendar.SECOND) < 10){
			sb.append(0); //补零
		}
		sb.append(calendar.get(Calendar.SECOND));
		

		return sb.toString();
	}
	
}

