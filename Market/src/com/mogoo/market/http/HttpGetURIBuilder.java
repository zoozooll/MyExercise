package com.mogoo.market.http;

import java.net.URI;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 构建get请求 URL
 * 
 * @author Administrator
 * 
 */
public abstract class HttpGetURIBuilder {
	public abstract Map<String, String> getParams();

	public abstract String getURIString();

	public final URI generateURI() {
		String uriStr = getURIString();
//		if (!uriStr.endsWith("?")) {
//			uriStr = uriStr + "?";
//		}
		if (!uriStr.endsWith("&")) {
			uriStr = uriStr + "&";
		}
		StringBuffer uriTmp = new StringBuffer(uriStr);
		Map<String, String> map = getParams();
		for (String key : map.keySet()) {
			uriTmp.append(key).append("=")
					.append(URLEncoder.encode(map.get(key))).append("&");
		}
		int lastIndex = uriTmp.lastIndexOf("&");
		uriTmp.deleteCharAt(lastIndex);
		return URI.create(uriTmp.toString());
	}
}
