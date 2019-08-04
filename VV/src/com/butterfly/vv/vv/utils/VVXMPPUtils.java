package com.butterfly.vv.vv.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import org.jivesoftware.smack.util.StringUtils;

import android.graphics.Bitmap;

import com.beem.project.btf.utils.AppProperty;

public class VVXMPPUtils {
	private final static String[] CONSTELLATION_CODE = new String[] { "摩羯座",
			"水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座",
			"天蝎座", "射手座", "摩羯座" };

	public static String makeJidCompleted(String jid) {
		if (jid == null)
			return null;
		StringBuilder build = new StringBuilder();
		String name = StringUtils.parseName(jid);
		build.append(name);
		if (!"".equals(name) && !name.contains("@")) {
			build.append("@").append(
					AppProperty.getInstance().XMPPSERVER_SERVENAME);
		}
		return build.toString();
	}
	public static String makeJidParsed(String jid) {
		if (jid == null)
			return null;
		String name = StringUtils.parseName(jid);
		return name;
	}
	public static InputStream Bitmap2InputStream(Bitmap bm, int quality) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, quality, baos);
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return is;
	}
	// 根据日期获取星座
	public static String getConstellation(Date date) {
		String constellation = "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		switch (cal.get(Calendar.MONTH)) {
			case 0:
				constellation = (day >= 21) ? CONSTELLATION_CODE[10]
						: CONSTELLATION_CODE[9];
				break;
			case 1:
				constellation = (day >= 20) ? CONSTELLATION_CODE[11]
						: CONSTELLATION_CODE[10];
				break;
			case 2:
				constellation = (day >= 21) ? CONSTELLATION_CODE[0]
						: CONSTELLATION_CODE[11];
				break;
			case 3:
				constellation = (day >= 21) ? CONSTELLATION_CODE[1]
						: CONSTELLATION_CODE[0];
				break;
			case 4:
				constellation = (day >= 22) ? CONSTELLATION_CODE[2]
						: CONSTELLATION_CODE[1];
				break;
			case 5:
				constellation = (day >= 22) ? CONSTELLATION_CODE[3]
						: CONSTELLATION_CODE[2];
				break;
			case 6:
				constellation = (day >= 23) ? CONSTELLATION_CODE[4]
						: CONSTELLATION_CODE[3];
				break;
			case 7:
				constellation = (day >= 24) ? CONSTELLATION_CODE[5]
						: CONSTELLATION_CODE[4];
				break;
			case 8:
				constellation = (day >= 24) ? CONSTELLATION_CODE[6]
						: CONSTELLATION_CODE[5];
				break;
			case 9:
				constellation = (day >= 24) ? CONSTELLATION_CODE[7]
						: CONSTELLATION_CODE[6];
				break;
			case 10:
				constellation = (day >= 23) ? CONSTELLATION_CODE[8]
						: CONSTELLATION_CODE[7];
				break;
			case 11:
				constellation = (day >= 22) ? CONSTELLATION_CODE[9]
						: CONSTELLATION_CODE[8];
				break;
			default:
				break;
		}
		return constellation;
	}
	public static int getAge(Date birthDay) throws Exception {
		Calendar cal = Calendar.getInstance();
		if (cal.before(birthDay)) {
			throw new IllegalArgumentException(
					"The birthDay is before Now.It's unbelievable!");
		}
		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH);
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birthDay);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
		int age = yearNow - yearBirth;
		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				} else {
				}
			} else {
				age--;
			}
		} else {
		}
		return age;
	}
}
