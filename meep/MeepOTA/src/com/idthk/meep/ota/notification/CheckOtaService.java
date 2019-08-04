package com.idthk.meep.ota.notification;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.idthk.meep.ota.notification.RestRequestCheckOTA.CheckOtaListener;
import com.idthk.meep.ota.rest.OtaUpdateFeedback;
import com.idthk.meep.ota.rest.RestRequest;
import com.idthk.meep.ota.ui.Utils;
import com.idthk.meep.ota.utility.Constants;
import com.idthk.meep.ota.utility.OtaUpgradeUtility;

public class CheckOtaService extends IntentService {
	RestRequestCheckOTA rest;

	public CheckOtaService() {
		super("CheckOtaService");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		rest = new RestRequestCheckOTA();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Log.d(getClass().getSimpleName(), "start check ota available");
		setNewUpdateListener();
		getNewUpdate();
	}

	private void getNewUpdate() {
		String current = OtaUpgradeUtility.getVersionCode();
		String serial = OtaUpgradeUtility.getSerialNumber();
		if (RestRequest.isWifiAvailable(this)) {
			rest.checkOTA(current,serial);
		}
	}
	private void setNewUpdateListener() {
		rest.setmCheckOtaListener(new CheckOtaListener() {
			
			@Override
			public void onReceivedSuccess(OtaUpdateFeedback feedback) {
				SharedPreferences sharedPreferences = getSharedPreferences("last_notification", Context.MODE_PRIVATE);
				int last_notification = sharedPreferences.getInt("last_notification", 0);
				Utils.printLogcatDebugMessage(last_notification+"");
				int timestamp = (int) (System.currentTimeMillis() / 86400000);
				Utils.printLogcatDebugMessage(timestamp+"");
				
				if (last_notification < timestamp) {
					Editor editor = sharedPreferences.edit();
					editor.putInt("last_notification",timestamp);
					editor.commit();
					
					//pop up "ota available"
					Intent intent = new Intent(CheckOtaService.this,NotificationOtaActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra(Constants.KEY_TITLE, feedback.getVersionName());
					intent.putExtra(Constants.KEY_MESSAGE, feedback.getChangelog());
					startActivity(intent);
				}
				
				
			}
			
			@Override
			public void onReceivedFailued() {
				
			}
		});
				
	}
}
