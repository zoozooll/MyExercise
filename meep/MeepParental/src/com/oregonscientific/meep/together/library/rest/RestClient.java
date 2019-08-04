package com.oregonscientific.meep.together.library.rest;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.oregonscientific.meep.together.activity.Utils;
import com.oregonscientific.meep.together.bean.ResponseBasic;

public class RestClient {
	private static String BASE_URL = "";
	private static final String BASE_URL_1 = "https://portal.meeptablet.com/1/";
	private static final String BASE_URL_2 = "https://portal.meeptablet.com/2/";

	private static AsyncHttpClient client = new AsyncHttpClient();

	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void getJsonAuth(String url, String authToken,
			AsyncHttpResponseHandler responseHandler) {
		Utils.printLogcatDebugMessage("url:" + getAbsoluteUrl(url));
		Utils.printLogcatDebugMessage("Authorization: OSA " + authToken);
		client.addHeader("Content-Type", "application/json");
		client.addHeader("Authorization", "OSA " + authToken);
		client.get(getAbsoluteUrl(url), responseHandler);
	}

	public static void getJsonAuth2(String url, String authToken,
			AsyncHttpResponseHandler responseHandler) {
		client.addHeader("Content-Type", "application/json");
		client.addHeader("Authorization", "OST " + authToken);
		client.get(getAbsoluteUrl(url), responseHandler);
	}

	public static void postJsonAuth(String url, String json, String authToken,
			AsyncHttpResponseHandler responseHandler) {
		Utils.printLogcatDebugMessage("url:" + getAbsoluteUrl(url));
		Utils.printLogcatDebugMessage("json:" + json);
		Utils.printLogcatDebugMessage("Authorization: OSA " + authToken);
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
		client.addHeader("Authorization", "OSA " + authToken);
		client.post(null, getAbsoluteUrl(url), se, "application/json", responseHandler);

	}

	public static void postJson(String url, String json,
			AsyncHttpResponseHandler responseHandler) {
		StringEntity se = null;
		if (json != null) {
			try {
				// 2013-03-28 - raymond - enable utf8 in StringEntity
				se = new StringEntity(json, HTTP.UTF_8);
				se.setContentType("application/json");

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		client.post(null, getAbsoluteUrl(url), se, "application/json", responseHandler);
	}

	public static void getJson(String url,
			AsyncHttpResponseHandler responseHandler) {
		client.addHeader("Content-Type", "application/json");
		client.get(getAbsoluteUrl(url), responseHandler);
	}

	// get absolute url
	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}

	public static void initVersion(int x) {
		if (x == 1) {
			BASE_URL = BASE_URL_1;
		} else {
			BASE_URL = BASE_URL_2;
		}
	}
	private static Gson mGson = new Gson();
	public static void uploadImage(String url,Object upload,JsonHttpResponseHandler responseHandler) {
		
//		if(upload instanceof Bitmap)
//		{
//			byte[] bytesToSend =Bitmap2Bytes((Bitmap)upload);
//			MultipartEntity entity = new MultipartEntity();
//			entity.addPart("file",new ByteArrayBody (bytesToSend,"image/png", "image"));
//			HttpPost post = new HttpPost(url);
//			post.setEntity(entity);
//			
//			client.post(null, getAbsoluteUrl(url), entity, null, responseHandler);
//		}
		try {
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(getAbsoluteUrl(url));

			if (upload instanceof Bitmap) {
				// Add your data
				MultipartEntity entity = new MultipartEntity();
				byte[] bytesToSend = Bitmap2Bytes((Bitmap) upload);
				entity.addPart("file", new ByteArrayBody(bytesToSend, "image/png", "image"));
				httppost.setEntity(entity);
			}

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			StringBuilder sb = inputStreamToString(response.getEntity().getContent());
			String message = sb.toString();
			response.getEntity().consumeContent();
			if (message != null) {
				try{
					ResponseBasic r = mGson.fromJson(message, ResponseBasic.class);
					if(r.getCode()>=200&& r.getCode()<400)
					{
						responseHandler.onSuccess(message);
					}
					else
					{
						JSONObject object = new JSONObject(message);
						responseHandler.onFailure(new Throwable(),object);
					}
				}catch(Exception e)
				{
					responseHandler.onFailure(new Throwable(),message);
				}
				
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}

	}

	private static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] imageBytes = baos.toByteArray();
		return imageBytes;
	}

	private static StringBuilder inputStreamToString(InputStream is) {
		String line = "";
		StringBuilder total = new StringBuilder();

		// Wrap a BufferedReader around the InputStream
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		// Read response until the end
		try {
			while ((line = rd.readLine()) != null) {
				total.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Return full string
		return total;
	}

}
