package com.oregonscientific.meep.store2.ctrl.notification;

import com.oregonscientific.meep.store2.R;

import android.content.Context;

public class NotificationCoinsRejectedItem extends NotificationMessageItem{

	private String parentName;
	
	public NotificationCoinsRejectedItem(Context context,int type,String parentName) {
		super(type,context);
		this.setParentName(parentName);
		int messageResId = R.string.notification_message_coins_allocated;
		String messageString = String.format(getResourceString(messageResId), parentName);
		this.setMessage(messageString);
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

}
