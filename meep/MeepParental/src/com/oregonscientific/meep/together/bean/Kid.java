package com.oregonscientific.meep.together.bean;

public class Kid {
	
	private String userid;
	private Supervisor[] supervisors;
	private String type;
	private String avatar;
	private long coins;
	private String name;
	private int unread_notifications;
	
	public static  final String S_TYPE_PARENT = "parent";
	
	public int getUnread_notifications() {
		return unread_notifications;
	}
	public void setUnread_notifications(int unread_notifications) {
		this.unread_notifications = unread_notifications;
	}
	public String getUserId() {
		return userid;
	}
	public void setUserId(String userId) {
		this.userid = userId;
	}
	public Supervisor[] getSupervisors() {
		return supervisors;
	}
	public void setSupervisors(Supervisor[] supervisors) {
		this.supervisors = supervisors;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public long getCoins() {
		return coins;
	}
	public void setCoins(long coins) {
		this.coins = coins;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
