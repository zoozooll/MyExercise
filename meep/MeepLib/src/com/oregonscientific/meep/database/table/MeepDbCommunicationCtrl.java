package com.oregonscientific.meep.database.table;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.oregonscientific.meep.global.Global;
import com.oregonscientific.meep.global.Global.AppType;
import com.oregonscientific.meep.message.common.MeepAppMessage;
import com.oregonscientific.meep.message.common.MeepAppMessage.Category;

public class MeepDbCommunicationCtrl {
	/*
	 * NOTE:
	 * THIS CLASS DO ALL COMMUNICATION BETWEEN OTHER APPS AND MEEPHOME, NOT ONLY DATABASE
	 * 
	 */

	private Gson mGson = null;
	private Context mContext = null;
	private AppType mAppType = AppType.NotDefined;
	
	public MeepDbCommunicationCtrl(Context context, AppType appType)
	{
		mContext = context;
		mAppType = appType;
		mGson = new Gson();
	}
	
	private String getToActionString() {
		return Global.INTENT_MSG_PREFIX + Global.AppType.MeepMessage;
	}
	
	private String getFromActionString()
	{
		return Global.INTENT_MSG_PREFIX + mAppType;
	}
	
	public void sendMessage(Category category, String message, String opcode)
	{
		MeepAppMessage meepMsg = new MeepAppMessage(category, opcode, message, getFromActionString());
		
		String meepMsgStr = mGson.toJson(meepMsg);
		
		Intent i = new Intent();
		i.setAction(getToActionString());
		i.putExtra(Global.STRING_MESSAGE, meepMsgStr);
		mContext.sendBroadcast(i);
		Log.d("MeepDbCommunicationCtrl", "Message sent");
	}
	
	public void sendDBQuery(String sql, String opcode)
	{
		Log.i("ST", "sendDBQuery");
		
		MeepAppMessage meepMsg = new MeepAppMessage(Category.DATABASE_QUERY, opcode, sql, getFromActionString());
		
		String meepMsgStr = mGson.toJson(meepMsg);
		
		Intent i = new Intent();
		i.setAction(getToActionString());
		i.putExtra(Global.STRING_MESSAGE, meepMsgStr);
		mContext.sendBroadcast(i);
		Log.d("database", "database request sent");
	}
	
	public void sendDbAlterSql(String sql, String opcode)
	{
		MeepAppMessage appMsg = new MeepAppMessage(Category.DATABASE_ALTER, opcode, sql, getFromActionString());
		String meepMsgStr = mGson.toJson(appMsg);
		
		Intent i = new Intent();
		i.setAction(getToActionString());
		i.putExtra(Global.STRING_MESSAGE, meepMsgStr);
		mContext.sendBroadcast(i);
		Log.i("ST", "sendDbAlterSql");
	}
}
