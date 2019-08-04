/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.util.Log;

/**
 * A class containing utility methods related to Runtime
 * 
 * @author Stanley Lam
 */
public class RuntimeUtils {
	
	private static final String TAG = "RuntimeUtils";
	
	/**
	 * Executes the specified program in a separate native process. The new process 
	 * inherits the environment of the caller.
	 * 
	 * @param command the command to execute
	 * @return the result from executing {@code command}
	 */
	public static String exec(String command) {
		String result = "";
		if (command == null) {
			return result;
		}
		
		try {
			Process p = Runtime.getRuntime().exec(command);
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				result += line + "\n";
			}
		} catch (IOException e) {
			Log.e(TAG, e + " occurred when executing command " + command);
		}
		
		Log.d(TAG, result + " returned from executing " + command);
		return result;
	}

}
