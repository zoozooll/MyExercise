package com.oregonscientific.meep.together.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

import com.oregonscientific.meep.Build;

public class Utils {
	// isValidEmailAddress: Check the email address is OK
	public static boolean isValidEmailAddress(String emailAddress) {
		String emailRegEx;
		Pattern pattern;
		// Regex for a valid email address
		emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";
		// Compare the regex with the email address
		pattern = Pattern.compile(emailRegEx);
		Matcher matcher = pattern.matcher(emailAddress);
		if (!matcher.find()) {
			return false;
		}
		return true;
	}

	public static void printLogcatDebugMessage(String text) {
//		if (Build.DEBUG)
			Log.d("MeepParental", text);
	}
	public static void printLogcatDebugMessage(String tag,String text) {
//		if (Build.DEBUG)
			Log.d(tag, text);
	}
}
