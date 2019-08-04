package com.oregonscientific.meep.together.bean;

public class Logs {
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getEvent_time() {
		return event_time;
	}
	public void setEvent_time(String event_time) {
		this.event_time = event_time;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	private String id;
	private String message;
	private String event_time;
	private String category;
	
	public static String CATEGORY_APPS = "apps";
	public static String CATEGORY_SYSTEM = "system";
	public static String CATEGORY_GAME = "game";
	public static String CATEGORY_EBOOK = "ebook";
	public static String CATEGORY_INTERNET = "internet";
	public static String CATEGORY_MUSIC = "music";
	public static String CATEGORY_MOVIE = "movie";
	
}
