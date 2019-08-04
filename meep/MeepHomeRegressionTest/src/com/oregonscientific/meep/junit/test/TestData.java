package com.oregonscientific.meep.junit.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.oregonscientific.meep.msm.Message;

public class TestData {

	public static final String PACKAGE_NAME = "com.oregonscientific.meep.home";
	public static final String FRIEND_ACCOUNT_ID = "50e4174204f77a340484a251";
	public static final String USER_ACCOUNT_ID = "50e5825c04f77a1c945f0354";
	public static final String DUMMY_MESSAGE_ID = "01234567890abcdefabcdefa";
	public static final String TEST_USER_NAME = "TestUser-A";
	public static final String MESSAGE_SENT = "Message Sent.";
	public static final String VALID_FRIEND_MEEPTAG ="pac#2";
	public static final String INVALID_FRIEND_MEEPTAG ="dummy";
	public static final String BLOCKED_FRIEND_MEEPTAG = "fuck";
	
	public static String mockChatMessage() {
		
		JsonObject json = new JsonObject();
		json.addProperty("received", false);
		json.addProperty("sender", FRIEND_ACCOUNT_ID);
		json.addProperty("messageid", DUMMY_MESSAGE_ID);
		json.addProperty("message", USER_ACCOUNT_ID);
		json.addProperty("recipient", USER_ACCOUNT_ID);
		json.addProperty("proc", Message.PROCESS_INSTANT_MESSAGING);
		
		Gson gson = new GsonBuilder().create();
		return gson.toJson(json);
	}

}
