/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.home.test;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Test {
	
	/** Test data **/
	public static final String DATA_ACCOUNT_MEEP_TAG = "andy#51";
	public static final String DATA_ACCOUNT_FIRST_NAME = "Andy";
	public static final String DATA_ACCOUNT_NICKNAME = "Andy Au";
	public static final String DATA_ACCOUNT_USER_ID = "51c4057a7d400344764c97c1"; // Larry
//	public static final String DATA_ACCOUNT_USER_ID = "meep00000000000000000000"; // Default user
	public static final String DATA_ACCOUNT_ICON = "file:///mnt/sdcard/Pictures/50c5cb801fd219f24b7e2fed0b977c58.0";
	
	public static final String DATA_COMPONENT_COMMUNICATOR = "com.oregonscientific.meep.communicator/.activity.CommunicatorActivity";
	
	public static final String DATA_BLACKLIST_SHORT_PASSAGE = "Passage with some fucking words";
	
	public static final String DATA_BLACKLIST_URL = "http://www.playboy.com";
	public static final String DATA_WHITELIST_URL = "www. sciencemadesimple.com";
	public static final String DATA_RECOMMENDED_WEB_URL = "http://eo.ucar.edu/webweather";
	public static final String DATA_NOT_RECOMMENDED_WEB_URL = "http://www.msn.com";
	public static final String DATA_RECOMMENDED_YOUTUBE_URL = "jHqL2Oy-yJw";
	public static final String DATA_NOT_RECOMMENDED_YOUTUBE_URL = "jHqL2Oy-yuw";
	
	/** A list of permission settings */
	public static final Map<String, JsonElement> PERMISSIONS = new HashMap<String, JsonElement>() {
		
		private static final long serialVersionUID = -5951571573490558219L;

		{
			put("securitylevel", getAccessLevel("high", 0));
			put("picture", getAccessLevel("allow", 1440));
			put("googleplay", getAccessLevel("deny", 0));
			put("purchase", getAccessLevel("deny", 0));
			put("movie", getAccessLevel("deny", 0));
			put("ebook", getAccessLevel("deny", 0));
			put("apps", getAccessLevel("deny", 0));
			put("youtube", getAccessLevel("deny", 0));
			put("game", getAccessLevel("allow", 1440));
			put("inapp", getAccessLevel("deny", 0));
			put("communicator", getAccessLevel("deny", 0));
			put("music", getAccessLevel("deny", 0));
			put("browser", getAccessLevel("deny", 0));
		}
	};
	
	public static JsonObject getAccessLevel(String accessLevel, long timeLimit) {
		JsonObject returnValue = new JsonObject();
		returnValue.addProperty("access", accessLevel);
		returnValue.addProperty("timelimit", timeLimit);
		return returnValue;
	}

}
