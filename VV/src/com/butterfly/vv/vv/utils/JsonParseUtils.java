package com.butterfly.vv.vv.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.beem.project.btf.constant.Constants;

/**
 * @func json万能解析成Map
 * @author yuedong bao
 * @time 2014-12-29 下午12:17:47
 */
public class JsonParseUtils {
	public static class FindParam {
		final public String key;
		final public int postion;
		public final static int INVALIDKEY = -1;

		public FindParam(String key, int... postion) {
			this.key = key;
			this.postion = postion.length > 0 ? postion[0] : INVALIDKEY;
		}
	}

	// 将字符串解析成Map
	public static Map<String, Object> parseToMap(String jsonStr) {
		Map<String, Object> result = null;
		if (null != jsonStr) {
			try {
				JSONObject jsonObject = new JSONObject(jsonStr);
				result = parseJSONObject(jsonObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	/**
	 * 根据key获取值
	 * @param params
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T, D extends T> D getParseValue(Map<String, Object> result,
			Class<T> retCls, FindParam... params) {
		if (result == null || params.length == 0)
			return null;
		Object srcMap = result;
		for (FindParam param : params) {
			if (srcMap instanceof Map) {
				srcMap = ((Map) srcMap).get(param.key);
			}
			if (param.postion != FindParam.INVALIDKEY && srcMap instanceof List) {
				List list = (List) srcMap;
				srcMap = list.get(param.postion);
			}
		}
		return (D) srcMap;
	}
	public static boolean getResult(Map<String, Object> result) {
		return String.valueOf(Constants.RESULT_OK).equals(
				getParseValue(result, String.class, new FindParam("result")));
	}
	public static List<Map<String, Object>> getData(Map<String, Object> result) {
		return getParseValue(result, List.class, new FindParam("data"));
	}
	public static String getDescription(Map<String, Object> result) {
		return getParseValue(result, String.class, new FindParam("description"));
	}
	public static String getNext(Map<String, Object> result) {
		return getParseValue(result, String.class, new FindParam("next"));
	}
	/**
	 * JsonObject和JSONArray内容中存在三种情况：(1)JSONArray (2)JSONObject (3)Object
	 * @param inputObject
	 * @return
	 * @throws JSONException
	 */
	private static Object parseValue(Object inputObject) throws JSONException {
		Object outputObject = null;
		if (null != inputObject) {
			if (inputObject instanceof JSONArray) {
				outputObject = parseJSONArray((JSONArray) inputObject);
			} else if (inputObject instanceof JSONObject) {
				outputObject = parseJSONObject((JSONObject) inputObject);
			} else {
				outputObject = inputObject;
			}
		}
		return outputObject;
	}
	private static List<Object> parseJSONArray(JSONArray jsonArray)
			throws JSONException {
		List<Object> valueList = null;
		if (null != jsonArray) {
			valueList = new ArrayList<Object>();
			for (int i = 0; i < jsonArray.length(); i++) {
				Object itemObject = jsonArray.get(i);
				if (null != itemObject) {
					valueList.add(parseValue(itemObject));
				}
			}
		}
		return valueList;
	}
	private static Map<String, Object> parseJSONObject(JSONObject jsonObject)
			throws JSONException {
		Map<String, Object> valueObject = null;
		if (null != jsonObject) {
			valueObject = new HashMap<String, Object>();
			@SuppressWarnings("unchecked")
			Iterator<String> keyIter = jsonObject.keys();
			while (keyIter.hasNext()) {
				String keyStr = keyIter.next();
				Object itemObject = jsonObject.opt(keyStr);
				if (null != itemObject) {
					valueObject.put(keyStr, parseValue(itemObject));
				}
			}
		}
		return valueObject;
	}
}
