package com.oregonscientific.meep.store2.inapp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

	public int checkApiVersion(int apiVersion) {
		// Check whether api version is supported or not
		for (int support : Consts.SUPPORT_VERSIONS) {
			// if version is valid
			if (support == apiVersion) {
				return Consts.RESULT_OK;
			}
		}
		// not support this api version
		return Consts.RESULT_ERROR;
	}
	
	/**
     * encode string
     * @param code
     * @param message
     * @return
     */
	public static String Encode(String code,String message){
		MessageDigest md;
		String encode = null;
		try {
			md = MessageDigest.getInstance(code);
			encode = byteArrayToHexString(md.digest(message
					.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return encode;
	}
	
	/**
	 * byte[] to HEX String
	 * @param b
	 * @return
	 */
	private static String byteArrayToHexString(byte[] b) {
		String result = "";
		for (int i=0; i < b.length; i++) {
			result +=
          Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
		}
		return result;
	}

}
