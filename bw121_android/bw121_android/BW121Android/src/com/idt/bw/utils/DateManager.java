/**
 * 
 */
package com.idt.bw.utils;

import android.content.ContentResolver;
import android.content.Context;

/**
 * @author aaronli
 *
 */
public class DateManager {

	public static String getSystemDateFormat(Context c) {
		ContentResolver cv = c.getContentResolver();
		String strTimeFormat = android.provider.Settings.System
				.getString(cv, android.provider.Settings.System.DATE_FORMAT)
				.replace('-', ' ').replace("MM", "MMM");
		return strTimeFormat;
	}
}
