/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.account;

import com.google.gson.annotations.SerializedName;

/**
 * An internal object for parsing uploaded resource response from server 
 */
class $Avatar {
	
	@SerializedName("status")
	String response;
	
	@SerializedName("url")
	String url;
	
	@SerializedName("prefix")
	String prefix;
	
	@SerializedName("code")
	int statusCode;

}
