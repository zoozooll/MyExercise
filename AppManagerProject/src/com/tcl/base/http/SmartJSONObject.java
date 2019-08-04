package com.tcl.base.http;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class SmartJSONObject extends JSONObject {

	public SmartJSONObject() {
	}

	public SmartJSONObject(@SuppressWarnings("rawtypes") Map copyFrom) {
		super(copyFrom);
	}

	public SmartJSONObject(JSONTokener readFrom) throws JSONException {
		super(readFrom);
	}

	public SmartJSONObject(String json) throws JSONException {
		super(json);
	}

	public SmartJSONObject(JSONObject copyFrom, String[] names)
			throws JSONException {
		super(copyFrom, names);
	}

	@Override
	public String getString(String name) throws JSONException {
		String s = super.getString(name);
		if (s == null || s.equals("null"))
			return null;
		return s;
	}

	@Override
	public String optString(String name, String fallback) {
		String s = super.optString(name, fallback);
		if (s == null || s.equals("null"))
			return null;
		return s; 
	}

	@Override
	public JSONArray optJSONArray(String name) {
		Object object = opt(name);
		if (object == JSONObject.NULL)
			return null;
		
		return object instanceof JSONArray ? (JSONArray) object : null;
	}

	@Override
	public JSONArray getJSONArray(String name) throws JSONException {
		 Object object = get(name);
		 if (object == JSONObject.NULL)
				return null;
		 
		return super.getJSONArray(name);
	}
	
	

}
