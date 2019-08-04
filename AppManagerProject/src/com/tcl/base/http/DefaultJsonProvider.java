package com.tcl.base.http;

import java.util.Map;

import org.json.JSONObject;

import com.tcl.framework.network.http.NetworkError;

/** 
 * @Description: jsonprovider的默认实现
 * @author wenbiao.xie 
 * @date 2014年9月28日 下午7:35:45 
 * @copyright TCL-MIE
 */

public class DefaultJsonProvider implements PostJsonProvider {

	public DefaultJsonProvider() {
	}

	@Override
	public String getURL() {
		return null;
	}

	@Override
	public boolean supportPost() {
		return false;
	}

	@Override
	public Map<String, String> getParams() {
		return null;
	}

	@Override
	public int parse(JSONObject obj) {
		return NetworkError.SUCCESS;
	}

	@Override
	public void onSuccess() {

	}

	@Override
	public void onCancel() {

	}

	@Override
	public void onError(int err) {

	}

	@Override
	public Map<String, byte[]> getPostEntities() {
		return null;
	}

}
