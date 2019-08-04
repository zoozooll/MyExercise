package com.idthk.meep.ota.notification;

import java.lang.reflect.Constructor;

import org.json.JSONObject;

import android.content.ReceiverCallNotAllowedException;

import com.google.gson.Gson;
import com.idthk.meep.ota.rest.OtaUpdateFeedback;

public class RestRequestCheckOTA {
	private final String URL_OTA_UPDATE = "https://portal.meeptablet.com/1/store/ota";
	/**
	 * non-augment Constructor
	 */
	public RestRequestCheckOTA() {
	}
	
	/**
	 * Check OTA function
	 * @param version
	 */
	public void checkOTA(String versionCode,String serial)
	{
		RestClient.setSerial(serial);
		JSONObject json = RestClient.doGet(URL_OTA_UPDATE+"/"+versionCode);
		if(json!=null)
		{
			OtaUpdateFeedback feedback = new Gson().fromJson(json.toString(), OtaUpdateFeedback.class);
			if(feedback.getCode()>=200&& feedback.getCode()<400)
			{
				if(mCheckOtaListener!=null) mCheckOtaListener.onReceivedSuccess(feedback);
				return;
			}
		}
		if(mCheckOtaListener!=null) mCheckOtaListener.onReceivedFailued();
	}
	
	//listener callback
	public CheckOtaListener getmCheckOtaListener() {
		return mCheckOtaListener;
	}
	public void setmCheckOtaListener(CheckOtaListener mCheckOtaListener) {
		this.mCheckOtaListener = mCheckOtaListener;
	}

	public interface CheckOtaListener{
		public abstract void onReceivedSuccess(OtaUpdateFeedback otaUpdateFeedback);
		public abstract void onReceivedFailued();
	}
	public CheckOtaListener mCheckOtaListener;
}
