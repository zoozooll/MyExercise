/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.util;

import java.util.Formatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class provide functionalities for HMAC
 * 
 * @author Stanley Lam
 */
public class HmacUtils {

	private HmacUtils() {}
	
	/**
	 * Processes the given input and finishes HMAC operation
	 * 
	 * @param input the input data
	 * @param key the key
	 * @return The HMAC result
	 */
	public static String process(String input, String key) {
		String returnValue = "";
		try {
			Mac mac = Mac.getInstance("HmacSHA1");
			SecretKeySpec sk = new SecretKeySpec(key.getBytes(), mac.getAlgorithm());
			mac.init(sk);
			byte[] result = mac.doFinal(input.getBytes());
			returnValue = hexEncode(result, new StringBuilder()).toString();
		} catch (Exception ex) {
			// Ignore
		}
		return returnValue;
	}
	
	/**
	 * Encodes the given byte array in hex
	 * 
	 * @param buffer The byte array to encode
	 * @param appendable The Appendable object whose char sequence is to be encoded
	 * @return a hex encoded Appendable object
	 */
	private static Appendable hexEncode(byte buffer[], Appendable appendable) {
		final Formatter formatter = new Formatter(appendable);
		for (int i = 0; i < buffer.length; i++) {
			formatter.format("%02x", buffer[i]);
		}
		formatter.close();
		return appendable;
	}
	
}
