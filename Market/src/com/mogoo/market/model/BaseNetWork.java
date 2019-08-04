package com.mogoo.market.model;

import com.mogoo.market.network.IBEManager;
import com.mogoo.market.network.http.HttpUrls;
import android.content.Context;
import com.mogoo.parser.XmlResultCallback;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseNetWork {

	// 上下文
	protected Context mContext;

	public BaseNetWork(Context ctx) {
		mContext = ctx;
	}

	/**
	 * 请求参数
	 * 
	 * @return
	 */
	public Map<String, String> getRequestParams(Map<String, String> paramMap) {
		if (paramMap == null)
			paramMap = new HashMap<String, String>();
		paramMap.put(HttpUrls.PARAM_APPID, IBEManager.getAppId() + "");
		return paramMap;
	}

	/**
	 * 是否是后台任务
	 * 
	 * @return
	 */
	public abstract boolean isBackgroundTask();

	/**
	 * 请求的action URL
	 * 
	 * @return
	 */
	public abstract String getActionUrl();

	/**
	 * 请求方式
	 * 
	 * @return
	 */
	public abstract int getRequestType();

	/**
	 * 结果解析的实体类
	 * 
	 * @return
	 */
	public abstract Object getResultCallback();

}
