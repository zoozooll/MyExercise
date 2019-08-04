/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.home.test;

import java.util.Date;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.oregonscientific.meep.http.Status;
import com.oregonscientific.meep.msm.Message;
import com.oregonscientific.meep.msm.MessageFilter;
import com.oregonscientific.meep.recommendation.RecommendationManager;

public final class MessageResponders {
	
	public static final MessageResponder SIGN_IN = 
		new MessageResponder(new MessageFilter(Message.PROCESS_ACCOUNT, Message.OPERATION_CODE_SIGN_IN)) {

			@Override
			public String onRespond(Message message) {
				JsonObject json = new JsonObject();
				json.addProperty("received", true);
				json.addProperty("is_guest", false);
				json.addProperty("messageid", message == null ? "" : message.getMessageID().toString());
				json.addProperty("meeptag", Test.DATA_ACCOUNT_MEEP_TAG);
				json.addProperty("first_name", Test.DATA_ACCOUNT_FIRST_NAME);
				json.addProperty("userid", Test.DATA_ACCOUNT_USER_ID);
				json.addProperty("proc", Message.PROCESS_ACCOUNT);
				
				if (message == null || !Message.OPERATION_CODE_SIGN_IN.equals(message.getOperation())) {
					json.addProperty("code", Status.CLIENT_ERROR_UNAUTHORIZED);
				} else {
					json.addProperty("status", "Authenticated successfully");
					json.addProperty("code", Status.SUCCESS_OK);
				}
				
				Gson gson = new GsonBuilder().create();
				return gson.toJson(json);
			}
		
	};
	
	public static final MessageResponder UPDATE_ACCOUNT = 
		new MessageResponder(new MessageFilter(Message.PROCESS_ACCOUNT, Message.OPERATION_CODE_SET_USER_NICKNAME)) {

		@Override
		public String onRespond(Message message) {
			JsonObject json = new JsonObject();
			json.addProperty("received", true);
			json.addProperty("proc", Message.PROCESS_ACCOUNT);
			json.addProperty("opcode", Message.OPERATION_CODE_SET_USER_NICKNAME);
			json.addProperty("messageid", message == null ? "" : message.getMessageID().toString());
			
			if (message == null || !Message.OPERATION_CODE_SET_USER_NICKNAME.equals(message.getOperation())) {
				json.addProperty("code", Status.CLIENT_ERROR_NOT_ACCEPTABLE);
			} else {
				json.addProperty("status", "Information updated.");
				json.addProperty("code", Status.SUCCESS_OK);
			}
			
			Gson gson = new GsonBuilder().create();
			return gson.toJson(json);
		}
	};
	
	public static final MessageResponder GET_PERMISSIONS = 
		new MessageResponder(new MessageFilter(Message.PROCESS_PARENTAL, Message.OPERATION_CODE_GET_PERMISSION)) {

			@Override
			public String onRespond(Message message) {
				JsonObject json = new JsonObject();
				json.addProperty("received", true);
				json.addProperty("code", Status.SUCCESS_OK);
				json.addProperty("proc", Message.PROCESS_PARENTAL);
				json.addProperty("opcode", Message.OPERATION_CODE_GET_PERMISSION);
				json.addProperty("messageid", message == null ? "" : message.getMessageID().toString());
				Date now = new Date();
				json.addProperty("last_update", now.getTime());
				
				Set<String> keys = Test.PERMISSIONS.keySet();
				for (String key : keys) {
					JsonElement value = Test.PERMISSIONS.get(key);
					json.add(key, value);
				}
				
				Gson gson = new GsonBuilder().create();
				return gson.toJson(json);
			}
		
	};
	
	public static final MessageResponder GET_RECOMMENDATIONS =
		new MessageResponder(new MessageFilter(Message.PROCESS_PARENTAL, Message.OPERATION_CODE_GET_RECOMMENDATIONS)) {

			@Override
			public String onRespond(Message message) {
				JsonObject json = new JsonObject();
				json.addProperty("received", true);
				json.addProperty("proc", Message.PROCESS_PARENTAL);
				json.addProperty("opcode", Message.OPERATION_CODE_GET_RECOMMENDATIONS);
				json.addProperty("messageid", message == null ? "" : message.getMessageID().toString());
				json.addProperty("thumbnail", "https://static.oregonscientific.com/meepuser/thumbnails/");
				
				JsonArray list = new JsonArray();
				String type = (String) message.getProperty("type");
				if (type != null) {
					json.addProperty("type", type);
					
					if (RecommendationManager.TYPE_WEB_FROM_ADMIN.equals(type)) {
						list.add(new JsonPrimitive("http://littlekids.nationalgeographic.com"));
						list.add(new JsonPrimitive("http://www.dogonews.com"));
						list.add(new JsonPrimitive("http://pbskids.org"));
						list.add(new JsonPrimitive("http://eo.ucar.edu/webweather"));
						list.add(new JsonPrimitive("http://oregonscientific.com/products_learningAndFun.asp"));
						list.add(new JsonPrimitive("http://kickinkitchen.tv"));
						list.add(new JsonPrimitive("http://www.timeforkids.com/news"));
						list.add(new JsonPrimitive("http://cartoonnetwork.com"));
						list.add(new JsonPrimitive("http://nick.com"));
						list.add(new JsonPrimitive("http://www.factmonster.com"));
						list.add(new JsonPrimitive("http://www.sciencemadesimple.com"));
					} else if (RecommendationManager.TYPE_WEB_FROM_PARENT.equals(type)) {
						list.add(new JsonPrimitive("http://www.sc-engei.co.jp/plant/green/cultivate/81.html"));
						list.add(new JsonPrimitive("http://www.takaratomy.co.jp/products/tomica/"));
						list.add(new JsonPrimitive("http://ww.dm5.com"));
						list.add(new JsonPrimitive("http://hk.yahoo.com"));
						list.add(new JsonPrimitive("http://www.nba.com"));
						list.add(new JsonPrimitive("http://www.lds.org"));
					} else if (RecommendationManager.TYPE_YOUTUBE_FROM_ADMIN.equals(type)) {
						list.add(new JsonPrimitive("FBznEFw_eJU"));
						list.add(new JsonPrimitive("oIlIVFBBbNw"));
						list.add(new JsonPrimitive("yihq8BIhL9c"));
						list.add(new JsonPrimitive("jHqL2Oy-yJw"));
						list.add(new JsonPrimitive("wbW9K1gbXAQ"));
						list.add(new JsonPrimitive("12x_vUd1nYo"));
						list.add(new JsonPrimitive("DbzS3uRdbwg"));
						list.add(new JsonPrimitive("bLdDKNxrL68"));
						list.add(new JsonPrimitive("YvR8LGOUpNA"));
						list.add(new JsonPrimitive("YgVlXOyU10I"));
						list.add(new JsonPrimitive("iRhdDs91aas"));
						list.add(new JsonPrimitive("SYawa4piO4k"));
						list.add(new JsonPrimitive("v2IeRJI3KDM"));
					} else if (RecommendationManager.TYPE_YOUTUBE_FROM_PARENT.equals(type)) {
						list.add(new JsonPrimitive("xXU_ylBSpbw"));
						list.add(new JsonPrimitive("BD9TAfFEp9A"));
						list.add(new JsonPrimitive("Rny7Mtxd9Fc"));
						list.add(new JsonPrimitive("Qn4kU5nvWAQ"));
						list.add(new JsonPrimitive("p-cWtkU2ueE"));
						list.add(new JsonPrimitive("Gz48nTuJRVQ"));
						list.add(new JsonPrimitive("EtNOIUZyhQI"));
					}
				}
				json.add("list", list);
				
				Gson gson = new GsonBuilder().create();
				return gson.toJson(json);
			}
		
	};

}
