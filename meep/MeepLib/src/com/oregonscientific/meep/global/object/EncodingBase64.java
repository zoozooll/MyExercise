package com.oregonscientific.meep.global.object;

import android.util.Base64;

public class EncodingBase64 {

	public static String encode(String str){
		try {
		    String encoded = Base64.encodeToString(str.getBytes("UTF-8"), Base64.DEFAULT);
			return encoded.trim();
		} catch (Exception e) {
			return str.trim();
		}
		
	}
	
	public static String decode(String str) {
		try {
			String decoded = new String(Base64.decode(str, Base64.DEFAULT));
			return decoded.trim();
		} catch (Exception ex) {
			return str.trim();
		}
	}
}
