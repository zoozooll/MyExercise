package com.butterfly.vv.service.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.beem.project.btf.BeemApplication;

/**
 * @ClassName: HttpResponseParser
 * @Description: http响应解析
 * @author: yuedong bao
 * @date: 2015-4-8 下午4:17:10
 */
public abstract class HttpResponseParser {
	public Map<String, Object> parse() {
		if (BeemApplication.isNetworkOk()) {
			return getDataFromDB();
		} else {
			return getDataFromNetWork(requestNetWork());
		}
	}
	private Map<String, Object> getDataFromNetWork(String response) {
		Map<String, Object> parseMap = new HashMap<String, Object>();
		@SuppressWarnings("rawtypes")
		List data = null;
		try {
			JSONObject data_json = new JSONObject(response);
			parseOther(data_json, parseMap);
			data = parseNetworkData(data_json.optJSONArray("data"));
			parseMap.put("data", data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return parseMap;
	};
	private void parseOther(JSONObject data_json, Map<String, Object> map) {
		try {
			map.put("next", data_json.get("next"));
			map.put("result", data_json.get("result"));
			map.put("description", data_json.get("description"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("rawtypes")
	public abstract List parseNetworkData(JSONArray dataArr_json);
	public abstract String requestNetWork();
	public abstract Map<String, Object> getDataFromDB();
}
