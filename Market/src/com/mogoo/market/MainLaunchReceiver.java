package com.mogoo.market;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mogoo.market.utils.LogUtils;

/**
 * @author dengliren
 * @date:2012-3-16
 * @description
 */
public class MainLaunchReceiver extends BroadcastReceiver {

	private final String MOGOO_MARKET_ID = "10000004";
	private final String APP_ID = "appId";
	private final String SOURCE_ID = "sourceId";
	private final String CT = "ct";

	private static String appId = null;
	private static String sourceId = null;
	private static String ct = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		appId = intent.getStringExtra(APP_ID);
		if (!appId.equals(MOGOO_MARKET_ID)) // 不是发送给商城的，返回
		{
			return;
		}
		sourceId = intent.getStringExtra(SOURCE_ID);
		ct = intent.getStringExtra(CT);
		LogUtils.debug(MainLaunchReceiver.class,
				"\n----> MainLaunchReceiver  sourceId = " + sourceId
						+ "  ct = " + ct);

		Intent intentInfo = new Intent("com.mogoo.market.ui.ExAppActivity");
		Bundle bundleInfo = new Bundle();
		bundleInfo.putString("apkid", sourceId);
		bundleInfo.putString("ct", ct);
		intentInfo.putExtras(bundleInfo);
		intentInfo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intentInfo);

	}

	public static String getSourceId() {
		return sourceId;
	}

	public static String getCt() {
		return ct;
	}

}
