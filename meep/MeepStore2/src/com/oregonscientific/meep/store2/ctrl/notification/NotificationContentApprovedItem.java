package com.oregonscientific.meep.store2.ctrl.notification;

import com.oregonscientific.meep.store2.R;

import android.content.Context;

public class NotificationContentApprovedItem extends NotificationMessageItem{

	protected String parentName;
	protected String applicationName;
	
	public NotificationContentApprovedItem(Context context,int type,String parentName,String applicationName) {
		super(type,context);
		this.setParentName(parentName);
		this.setApplicationName(applicationName);
		
		int messageResId = R.string.notification_message_content_approved;
		String messageString = String.format(getResourceString(messageResId), parentName,applicationName);
		this.setMessage(messageString);
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

}
