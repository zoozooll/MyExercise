package com.idthk.meep.ota.rest;

import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class RestRequest {
	private final String URL_OTA_UPDATE = "https://portal.meeptablet.com/1/store/ota";
	private final String TAG = "OTA_REST";
	private static AsyncHttpClient mHttpClient;
	private Context mContext;
	
	public RestRequest(Context context) {
		mContext = context;
	}
	
	//****OTA LISTENER****//
	public interface OtaUpdateListener{
		public abstract void onReceivedSuccess(OtaUpdateFeedback otaUpdateFeedback);
		public abstract void onReceivedFailued(OtaUpdateFeedback otaUpdateFeedback);
	}
	public OtaUpdateListener mOtaUpdateListener;
	public OtaUpdateListener getmOtaUpdateListener() {
		return mOtaUpdateListener;
	}
	public void setmOtaUpdateListener(OtaUpdateListener mOtaUpdateListener) {
		this.mOtaUpdateListener = mOtaUpdateListener;
	}
	//****OTA LISTENER****//
	
	
	public void otaUpdate(String versionCode,String serial){
		
		mHttpClient = new AsyncHttpClient(); 
		
		mHttpClient.addHeader("Content-Type", "application/json");
		mHttpClient.addHeader("X-MEEP-SERIAL", serial);
		//mHttpClient.addHeader("Accept-Language", getLanguage());
		String url = URL_OTA_UPDATE;
		
		mHttpClient.get(url+"/"+versionCode, new JsonHttpResponseHandler()
		{
			@Override
			public void onFailure(Throwable error, String content) {
				Log.d("onFailure", "ota update FAILED FAILED FAILED FAILED FAILED FAILED");
				super.onFailure(error, content);
				//TODO:tiemout
//				Log.d(TAG,content);
				OtaUpdateFeedback feedback = new OtaUpdateFeedback(999, "timeout");
				if(mOtaUpdateListener!=null)
					mOtaUpdateListener.onReceivedFailued(feedback);
			}
			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				super.onFailure(arg0, arg1);
				//TODO:fail get
				Log.d(TAG,arg1.toString());
				Gson gson = new Gson();
				OtaUpdateFeedback feedback = gson.fromJson(arg1.toString(),OtaUpdateFeedback.class);
				if(mOtaUpdateListener!=null)
					mOtaUpdateListener.onReceivedFailued(feedback);
			}
			
			
			@Override
			public void onSuccess(JSONObject json) {
				super.onSuccess(json);
				//TODO:start ota
				Log.d(TAG,json.toString());
				Gson gson = new Gson();
				OtaUpdateFeedback feedback = gson.fromJson(json.toString(),OtaUpdateFeedback.class);
				if(mOtaUpdateListener!=null)
					mOtaUpdateListener.onReceivedSuccess(feedback);
			}
		});
	}
	
	public static  boolean isWifiAvailable(Context context) {
		ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conMan != null) {
			// wifi
			State wifi = conMan.getNetworkInfo(1).getState();
			if (wifi != null) {
				return wifi == NetworkInfo.State.CONNECTED;
			}
		}
		return false;
	}
	public boolean isWifiAvailable() {
		return isWifiAvailable(mContext);
	}
	

}
