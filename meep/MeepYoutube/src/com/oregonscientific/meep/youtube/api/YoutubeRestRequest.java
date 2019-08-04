package com.oregonscientific.meep.youtube.api;

import org.json.JSONObject;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class YoutubeRestRequest {
	private final String URL_YOUTUBE_SEARCH = "https://portal.meeptablet.com/1/youtube/search";
	private static AsyncHttpClient mHttpClient;
	private Fragment mFragment;
	private String TAG = "YoutubeRestRequest";
	YoutubeRestCallBackHandler youtubeRestCallBackHandler;

	public YoutubeRestRequest(Fragment fragment) {
		youtubeRestCallBackHandler = (YoutubeRestCallBackHandler) fragment;
		mFragment = fragment;
	}
	
	public interface YoutubeRestCallBackHandler{
		public abstract void youtubeRestCallback_success(YoutubeDataFeedback feedback);
		public abstract void youtubeRestCallback_fail(String errorContent);
	}
	
	public void searchYoutube(String keyword){
		
		mHttpClient = new AsyncHttpClient(); 	
		
		mHttpClient.addHeader("Content-Type", "application/json");
		//mHttpClient.addHeader("MEEP-SERIAL", serial);
		//mHttpClient.addHeader("Accept-Language", getLanguage());
		String url = URL_YOUTUBE_SEARCH+"/"+keyword;
		Log.e(TAG,"searchYoutube: url="+url);
		
		mHttpClient.get(url, new JsonHttpResponseHandler()
		{
			@Override
			public void onFailure(Throwable error, String content) {
				Log.d(TAG,"onFailure->Error: Content="+content);
				super.onFailure(error, content);	
				if(youtubeRestCallBackHandler!=null){
					youtubeRestCallBackHandler.youtubeRestCallback_fail(content);
				}							
			}
			
			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				super.onFailure(arg0, arg1);
				Log.d(TAG,"TAG,onFailure->Error: JSONObject="+arg1.toString());
				if(youtubeRestCallBackHandler!=null){
					youtubeRestCallBackHandler.youtubeRestCallback_fail(arg1.toString());
				}
			}
			
			@Override
			public void onSuccess(JSONObject json) {
				super.onSuccess(json);				
				Gson gson = new Gson();
				String jsonData = json.toString();
				//Log.e(TAG,"onSuccess-> jsonData="+jsonData);
				YoutubeDataFeedback feedback = gson.fromJson(jsonData,YoutubeDataFeedback.class);
				if(youtubeRestCallBackHandler!=null){
					youtubeRestCallBackHandler.youtubeRestCallback_success(feedback);
				}				
			}
		});
	}


}
