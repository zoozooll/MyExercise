package com.oregonscientific.meep.message.common;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.oregonscientific.meep.global.Global;
import com.oregonscientific.meep.global.Global.AppType;

public class ServiceToAppMessangeCtrl {
	
	private String mActionString = null;
	
	
	public ServiceToAppMessangeCtrl(String recvMessage)
	{
		try {
			JSONObject recvObj = new JSONObject(recvMessage);
			String cmd = recvObj.getString(Global.STRING_CMD);
			mActionString = getActionStringByCmd(cmd);
			recvObj = null;
		} catch (JSONException e) {
			Log.e("MessageClientToAppDitributor", "websocket2 decode json err:" + e.toString());
		}
	}
	
	private String getActionStringByCmd(String cmd)
	{
		AppType appType = AppType.NotDefined;
		if (cmd == Global.CMD_SIGNIN || cmd == Global.CMD_SIGNOUT || cmd == Global.CMD_SET_NICK_NAME) {
			appType = AppType.Home;
		} else if (cmd == Global.CMD_GET_FRIEND_LIST || cmd == Global.CMD_NOTIFY || cmd == Global.CMD_SEND_TEXT) {
			appType = AppType.IM;
		} else if (cmd == Global.CMD_GET_VIDEOS_LIST) {
			appType = AppType.Movie;
		} else {
			Log.w("getActionStringByCmd", "websocket2 decode: app type is not defined");
		}
		
		if (appType != AppType.NotDefined) {
			return Global.INTENT_MSG_PREFIX + appType.toString();
		} else {
			return Global.INTENT_MSG_PREFIX + AppType.Home.toString();
		}
	}
	
	public String getActionString()
	{
		return mActionString;
	}
	
}
