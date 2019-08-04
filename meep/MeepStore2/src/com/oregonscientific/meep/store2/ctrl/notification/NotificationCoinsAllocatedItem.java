package com.oregonscientific.meep.store2.ctrl.notification;

import android.content.Context;

import com.oregonscientific.meep.store2.R;

public class NotificationCoinsAllocatedItem extends NotificationMessageItem{

	protected String parentName;
	protected double coins;
	
	public NotificationCoinsAllocatedItem(Context context,int type,String parentName,double coins) {
		super(type,context);
		this.setParentName(parentName);
		this.setCoins(coins);
		
		int messageResId = R.string.notification_message_coins_allocated;
		String messageString = String.format(getResourceString(messageResId), parentName,coins);
		this.setMessage(messageString);
		
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public double getCoins() {
		return coins;
	}

	public void setCoins(double coins) {
		this.coins = coins;
	}
	
}
