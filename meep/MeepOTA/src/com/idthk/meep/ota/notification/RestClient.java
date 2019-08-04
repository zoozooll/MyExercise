package com.idthk.meep.ota.notification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.idthk.meep.ota.rest.OtaUpdateFeedback;
import com.idthk.meep.ota.rest.RestRequest.OtaUpdateListener;

public class RestClient {

	private static String serial;

	public static String getSerial() {
		return serial;
	}

	public static void setSerial(String serial) {
		RestClient.serial = serial;
	}

	public static JSONObject doGet(String url) {
	    JSONObject json = null;
	    HttpClient httpclient = new DefaultHttpClient();
	    // Prepare a request object
	    HttpGet httpget = new HttpGet(url);
	    // Accept JSON
	    httpget.addHeader("Content-Type", "application/json");
	    if(serial!=null)
	    {
	    	httpget.addHeader("X-MEEP-SERIAL", serial);
	    }
	    // Execute the request
	    HttpResponse response;
	    try {
	        response = httpclient.execute(httpget);
	        // Get the response entity
	        HttpEntity entity = response.getEntity();
	        // If response entity is not null
	        if (entity != null) {
	            // get entity contents and convert it to string
	            InputStream instream = entity.getContent();
	            String result= convertStreamToString(instream);
	            // construct a JSON object with result
	            json=new JSONObject(result);
	            // Closing the input stream will trigger connection release
	            instream.close();
	        }
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    } catch (JSONException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	    // Return the json
	    return json;
	}

	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	
}
