/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.message.common;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.oregonscientific.meep.global.Global;
import com.oregonscientific.meep.global.Global.AppType;

/**
 * This is the base class sending log output
 * 
 * @author 
 */
public class MeepLogger {
	
	private Context mContext = null;
	private Gson mGson = null;
	private MeepAppMessage meepMsg = null;
	
	public MeepLogger(Context context) {
		this.mContext = context;
		this.mGson = new Gson();
		this.meepMsg = new MeepAppMessage(
				MeepAppMessage.Category.LOG, 
				"", 
				"", Global.INTENT_MSG_PREFIX + AppType.NotDefined);
	}

	public void s(String message) {
		meepMsg.setOpcode("SYSTEM");
		meepMsg.setMessage(message);

		Intent i = new Intent();
		i.setAction(Global.INTENT_MSG_PREFIX + Global.AppType.MeepMessage);
		i.putExtra(Global.STRING_MESSAGE, mGson.toJson(meepMsg));
		mContext.sendBroadcast(i);
	}

	public void p(String message) {
		meepMsg.setOpcode("PARENTAL");
		meepMsg.setMessage(message);

		Intent i = new Intent();
		i.setAction(Global.INTENT_MSG_PREFIX + Global.AppType.MeepMessage);
		i.putExtra(Global.STRING_MESSAGE, mGson.toJson(meepMsg));
		mContext.sendBroadcast(i);
	}
	
}
