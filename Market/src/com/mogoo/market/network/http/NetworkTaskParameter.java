package com.mogoo.market.network.http;

import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

/**
 * 网络链接的参数类
 * 
 * @author xjx-motone
 */
public class NetworkTaskParameter {
	/** 使用POST 方式 */
	public static final int REQUEST_TYPE_POST = 0;

	/** 使用GET 方式 */
	public static final int REQUEST_TYPE_GET = 1;

	/**
	 * 请求的URL
	 */
	protected String url;
	/**
	 * 传给服务器的参数列表
	 */
	protected Map<String, String> paramsMap;
	/**
	 * 请求方式:1--GET, 0--POST
	 */
	protected int requestType;
	protected boolean gzip = true;

	/**
	 * 正在处理的等待提示
	 */
	protected int progressTip = -1;

	/**
	 * 是否为后台任务，如果为后台任务，UI操作将被静止
	 */
	protected boolean isBackgroundTask = true;

	/**
	 * 设置网络的请求参数
	 * 
	 * @param url
	 *            --请求的地址
	 * @param requestType
	 *            :REQUEST_TYPE_POST,REQUEST_TYPE_GET
	 * @param progressTip
	 *            --等待提示语
	 * @param paramsMap
	 *            --请求参数
	 * @param gzip
	 *            --是否压缩
	 */
	public void setNetworkParams(String url, int requestType, int progressTip,
			Map<String, String> paramsMap, boolean gzip) {
		this.url = url;
		this.requestType = requestType;
		this.progressTip = progressTip;
		this.paramsMap = paramsMap;
		this.gzip = gzip;
	}

	/**
	 * 设置网络的请求参数
	 * 
	 * @param url
	 *            --请求的地址
	 * @param requestType
	 *            :REQUEST_TYPE_POST,REQUEST_TYPE_GET
	 * @param paramsMap
	 *            --请求参数
	 */
	public void setNetworkParams(String url, int requestType,
			Map<String, String> paramsMap) {
		this.url = url;
		this.requestType = requestType;
		this.paramsMap = paramsMap;
	}

	public String getUrl() {
		if (requestType == REQUEST_TYPE_GET) {
			Map<String, String> map = HttpUrls.getBaseParamPair();
			if (this.paramsMap != null) {
				map.putAll(this.paramsMap);
			}
			return this.url + HttpUrls.createParamPair(map);
		} else {
			return url;
		}

	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getParamsMap() {
		return paramsMap;
	}

	public void setParamsMap(Map<String, String> paramsMap) {
		this.paramsMap = paramsMap;
	}

	public List<NameValuePair> getParamPair() {
		Map<String, String> map = HttpUrls.getBaseParamPair();
		if (this.paramsMap != null) {
			this.paramsMap.putAll(map);
		} else {
			paramsMap = map;
		}
		return HttpUrls.createNameValuePairs(paramsMap);
	}

	public int getProgressTip() {
		return progressTip;
	}

	public void setProgressTip(int progressTip) {
		this.progressTip = progressTip;
	}

	public int getRequestType() {
		return requestType;
	}

	public void setRequestType(int requestType) {
		this.requestType = requestType;
	}

	public boolean isGzip() {
		return gzip;
	}

	public void setGzip(boolean gzip) {
		this.gzip = gzip;
	}

	public boolean isBackgroundTask() {
		return isBackgroundTask;
	}

	public void setBackgroundTask(boolean isBackgroundTask) {
		this.isBackgroundTask = isBackgroundTask;
	}
}
