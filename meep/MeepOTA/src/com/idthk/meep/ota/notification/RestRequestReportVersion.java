package com.idthk.meep.ota.notification;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class RestRequestReportVersion {

	Context mContext;
	public RestRequestReportVersion(Context context) {
		mContext = context;
	}
	
	private final String URL_REPORT_VERSION = "https://portal.meeptablet.com/2/store/update";
	public void reportVersionToServer(String jsonStr,String serial){
		AsyncHttpClient client = new AsyncHttpClient(); 
		String url = URL_REPORT_VERSION;
		client.addHeader("Content-Type", "application/json");
		client.addHeader("X-MEEP-SERIAL", serial);
		HttpEntity httpEntity;
        StringEntity stringEntity;
		try {
			stringEntity = new StringEntity(jsonStr);
			stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			httpEntity = stringEntity;
			client.post(mContext, url, httpEntity, "application/json", new JsonHttpResponseHandler() {
				@Override
				public void onFailure(Throwable arg0, JSONObject arg1) {
					super.onFailure(arg0, arg1);
				}
				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					Log.d("received", "report version:" + content);
				}
				@Override
				public void onSuccess(JSONObject json) {
					super.onSuccess(json);
					Log.d("received", "report version: " + json.toString());
				}
			});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
      
	}
}
