package com.oregonscientific.meep.store2.ctrl;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.oregonscientific.meep.store2.global.MeepStoreLog;

public class RestRequestClient {
	private static String BASE_URL = "";
//	private static final String BASE_URL_1 = "https://api.meeptablet.com/1/";
//	private static final String BASE_URL_2 = "https://api.meeptablet.com/2/";
	private static final String BASE_URL_1 = "https://portal.meeptablet.com/1/";
	private static final String BASE_URL_2 = "https://portal.meeptablet.com/2/";
	public static final int HTTP_DEFAULT_TIMEOUT = 30000;
	private AsyncHttpClient client = new AsyncHttpClient();

	public RestRequestClient() {
		client = new AsyncHttpClient();
		client.setTimeout(HTTP_DEFAULT_TIMEOUT);
	}

	public void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	public void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}

	public void getJsonAuth(String url, String authToken,
			AsyncHttpResponseHandler responseHandler) {
		client.addHeader("Content-Type", "application/json");
		client.addHeader("Authorization", "OST " + authToken);
		client.addHeader("X-LANGUAGE", Locale.getDefault().getLanguage());
		client.get(getAbsoluteUrl(url), responseHandler);
		MeepStoreLog.logcatMessage(MeepStoreLog.IAP_REST_REQUEST,getAbsoluteUrl(url));
		MeepStoreLog.logcatMessage(MeepStoreLog.IAP_REST_REQUEST,"OST " + authToken);
	}

	public void postJsonAuth(Context context,String url, String json, String authToken,
			AsyncHttpResponseHandler responseHandler) {
		StringEntity se = null;
		if (json != null) {
			try {
				se = new StringEntity(json, HTTP.UTF_8);
				se.setContentType("application/json");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		client.addHeader("Authorization", "OST " + authToken);
		client.post(context, getAbsoluteUrl(url), se, "application/json", responseHandler);
		MeepStoreLog.logcatMessage(MeepStoreLog.IAP_REST_REQUEST,getAbsoluteUrl(url));
		MeepStoreLog.logcatMessage(MeepStoreLog.IAP_REST_REQUEST,"OST " + authToken);
		MeepStoreLog.logcatMessage(MeepStoreLog.IAP_REST_REQUEST,"json:"+json);
	}

	public void getJson(String url, AsyncHttpResponseHandler responseHandler) {
		client.addHeader("Content-Type", "application/json");
		client.get(getAbsoluteUrl(url), responseHandler);
	}

	// get absolute url
	private String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}

	public void initVersion(int x) {
		if (x == 1) {
			BASE_URL = BASE_URL_1;
		} else {
			BASE_URL = BASE_URL_2;
		}
	}
}
