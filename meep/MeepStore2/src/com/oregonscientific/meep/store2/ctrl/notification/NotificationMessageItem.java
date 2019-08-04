package com.oregonscientific.meep.store2.ctrl.notification;

import com.oregonscientific.meep.store2.R;

import android.content.Context;

public class NotificationMessageItem {
	public static final int CONTENT_APPROVED = 0;
	public static final int CONTENT_REJECTED = 1;
	public static final int COINS_ALLOCATED = 2;
	public static final int COINS_REJECTED = 3;

	protected int type;
	protected String title;
	protected String message;
	
	protected Context mContext;
	
	public NotificationMessageItem(int type,Context context) {
		setType(type);
		mContext = context;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public Context getContext() {
		return mContext;
	}
	
	public String getResourceString(int resId)
	{
		String string = mContext.getResources().getString(resId);
		return string;
	}
	

}
