package com.tcl.manager.protocol;
 
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;

import com.google.gson.Gson;
import com.tcl.base.http.IProviderCallback;
import com.tcl.base.http.NewPostEntityProvider;
import com.tcl.manager.application.ManagerApplication;
import com.tcl.manager.protocol.data.VersionInfoPojo;
import com.tcl.manager.update.UpdateChecker;
import com.tcl.manager.util.AndroidUtil;
import com.tcl.manager.util.Constant; 
import com.tcl.manager.util.LogUtil;
import com.tcl.manager.util.SerUtil;
import com.tcl.manager.util.SharedStorekeeper;
import com.tcl.manager.util.StringUtils;   
import com.tcl.manager.util.UrlConfig;
/**
 * @Description:版本检查
 * @author pengcheng.zhang
 * @date 2014-12-23 上午11:21:15
 * @copyright TCL-MIE
 */
public class VersionInfoProtocol extends NewPostEntityProvider<VersionInfoPojo>
{
	private static final String TAG = "VersionInfoProtocol"; 

	/**
	 * 软件版本检查接口
	 * 
	 * @param callback 返回参数：系统请求完毕后回调接口
	 */
	public VersionInfoProtocol(IProviderCallback<VersionInfoPojo> callback)
	{
		super(callback);  
	}

	@Override
	public String getURL()
	{
		return UrlConfig.URL_GET_VERSION_INFO;  
	}

	@Override
	public Map<String, String> getParams()
	{
		Context context = ManagerApplication.sApplication.getApplicationContext();
		Map<String, String> params = SerUtil.fin(context); 
		params.put("inner_version_code", String.valueOf(AndroidUtil.getVersionCode(context)));
		params.put("inner_version_name", AndroidUtil.getVersionName(context));
		params.put("inner_package_name", AndroidUtil.getPackAgeName(context));

		return params;
	}

	@Override
	public void onResponse(String resp)
	{
//		resp = "{\"status\":\"0\",\"msg\":\"Success\",\"data\":{\"apkName\":\"AppManager2.0\",\"size\":0.94,\"forceUpdate\":0,\"versionCode\":2,\"versionName\":\"2.0\",\"downloadUrl\":\"http://182.254.246.89:8080/ostore-management/download/app/5e5ac418-0cf0-47b0-b311-d4ec5647e7f4.apk\",\"description\":\"新增功能：1. xxx；2. xxxx\"},\"compress\":0}";
		if (StringUtils.isEmpty(resp))
		{
			String s = "";
			return;
		}

		JSONObject jsonObj = null;
		try
		{
			jsonObj = new JSONObject(resp);
			SerUtil.unZip(jsonObj);
			if (!jsonObj.has(Constant.STATUS))
			{
				return;
			}
			LogUtil.v(TAG + jsonObj.toString());
			String status = jsonObj.getString(Constant.STATUS);
			if (StringUtils.isEmpty(status) || !Constant.STATUS_OK.equals(status))
			{
				return;
			}
			if (!jsonObj.has(Constant.DATA))
			{
				return;
			}
			String data = jsonObj.getString(Constant.DATA);
			if (StringUtils.isEmpty(data))
			{
				return;
			}

			setResult(new Gson().fromJson(jsonObj.toString(), VersionInfoPojo.class));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
