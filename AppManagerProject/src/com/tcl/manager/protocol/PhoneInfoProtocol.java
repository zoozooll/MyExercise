package com.tcl.manager.protocol;
 
import java.util.Map;

import org.json.JSONObject;
import com.tcl.base.http.IProviderCallback;
import com.tcl.base.http.NewPostEntityProvider;
import com.tcl.manager.application.ManagerApplication;
import com.tcl.manager.util.Constant;
import com.tcl.manager.util.LogUtil;
import com.tcl.manager.util.SerUtil;
import com.tcl.manager.util.StringUtils; 
import com.tcl.manager.util.UrlConfig;

/**
 * @Description:手机信息上传
 * @author pengcheng.zhang
 * @date 2014-12-23 上午10:45:36
 * @copyright TCL-MIE
 */
public class PhoneInfoProtocol extends NewPostEntityProvider<Boolean>
{

	private static final String TAG = "PhoneInfoProtocol";

	/**
	 * 手机信息上传接口
	 * 
	 * @param callback 返回参数：系统请求完毕后回调接口
	 */
	public PhoneInfoProtocol(IProviderCallback<Boolean> callback)
	{
		super(callback);
	}

	@Override
	public String getURL()
	{
		return UrlConfig.URL_SAVE_DEVICE_INFO;
	}

	@Override
	public Map<String, String> getParams()
	{ 
		return SerUtil.fin(ManagerApplication.sApplication.getApplicationContext());
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
			if (!jsonObj.has(Constant.CODE))
			{
				return;
			}
			LogUtil.v(TAG + jsonObj.toString());   
			setResult(Constant.CODE_OK.equals(jsonObj.getString(Constant.CODE)));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
