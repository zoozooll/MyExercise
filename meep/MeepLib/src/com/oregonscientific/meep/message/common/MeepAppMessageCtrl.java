package com.oregonscientific.meep.message.common;

import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.oregonscientific.meep.global.Global;
import com.oregonscientific.meep.global.Global.AppType;

public class MeepAppMessageCtrl {
	
	String APP_TO_SERVICE_MESSAGE = "app to service msg";
	
	public MeepAppMessageCtrl()
	{
	}
	
	public static Intent getBroadcastIntent(MeepAppMessage message,AppType appType) {
		if (message != null) {
			Gson gson = new Gson();
			String jsonStr = gson.toJson(message);
			
			Intent i = new Intent();
			i.setAction(Global.INTENT_MSG_PREFIX + appType);
			i.putExtra(Global.STRING_MESSAGE, jsonStr);
			return i;
		}
		return null;
	}
	
}
