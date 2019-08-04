package com.oregonscientific.meep.together.bean;

public class ResponseLoadNotification extends ResponseBasic{
	
	//reload logs
	private Notification[] notifications;

	public Notification[] getNotifi() {
		return notifications;
	}
	public void setNotifi(Notification[] notifications) {
		this.notifications = notifications;
	}
	
	
	
	
}
