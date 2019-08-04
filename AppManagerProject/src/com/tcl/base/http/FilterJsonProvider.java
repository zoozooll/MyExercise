package com.tcl.base.http;

import java.util.Map;

import org.json.JSONObject;

public class FilterJsonProvider implements JsonProvider {
	
	protected JsonProvider mProvider;
	
	public FilterJsonProvider(JsonProvider provider) {
		mProvider = provider;
	}

	@Override
	public String getURL() {
		return mProvider.getURL();
	}

	@Override
	public Map<String, String> getParams() {
		return mProvider.getParams();
	}

	@Override
	public int parse(JSONObject obj) {
		return mProvider.parse(obj);
	}

	@Override
	public void onSuccess() {
		mProvider.onSuccess();
	}

	@Override
	public void onCancel() {
		mProvider.onCancel();
	}

	@Override
	public void onError(int err) {
		mProvider.onError(err);		
	}

	@Override
	public boolean supportPost() {
		return mProvider.supportPost();
	}

}
