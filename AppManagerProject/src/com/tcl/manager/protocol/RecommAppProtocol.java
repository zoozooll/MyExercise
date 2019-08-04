package com.tcl.manager.protocol;
 
import java.util.HashMap;
import java.util.Map;
 
import org.json.JSONObject; 

import android.content.Context;

import com.google.gson.Gson;
import com.tcl.base.http.IProviderCallback;
import com.tcl.base.http.NewPostEntityProvider; 
import com.tcl.manager.application.ManagerApplication;
import com.tcl.manager.protocol.data.RecommAppPojo;  
import com.tcl.manager.util.Constant; 
import com.tcl.manager.util.LogUtil;
import com.tcl.manager.util.SerUtil;
import com.tcl.manager.util.StringUtils; 
import com.tcl.manager.util.UrlConfig;

/**
 * @Description: 推荐列表
 * @author pengcheng.zhang
 * @date 2014-12-23 下午4:17:13
 * @copyright TCL-MIE
 */
public class RecommAppProtocol extends NewPostEntityProvider<RecommAppPojo>
{

	private static final String TAG = "RecomAppProtocol"; 
	private String mPackageName; 
	
	/**
	 * 推荐列表接口
	 * 
	 * @param packageName 请求参数：应用的包名 
	 * @param callback 返回参数：系统请求完毕后回调接口
	 */
	public RecommAppProtocol(String packageName, IProviderCallback<RecommAppPojo> callback)
	{
		super(callback); 
		mPackageName = packageName; 
	} 

	@Override
	public String getURL()
	{  
		return UrlConfig.URL_GET_RECOMMEND_LIST;
	}

	@Override
	public Map<String, String> getParams()
	{
		Context context = ManagerApplication.sApplication.getApplicationContext();
		HashMap<String, String> params = SerUtil.fin(context);

		params.put("app_package_name", mPackageName); 

		return params;
	}

	@Override
	public void onResponse(String resp)
	{
		
		if (StringUtils.isEmpty(resp))
		{
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

			setResult(new Gson().fromJson(jsonObj.toString(), RecommAppPojo.class));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		} 
	}

}
