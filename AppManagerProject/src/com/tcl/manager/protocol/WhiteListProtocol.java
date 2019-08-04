package com.tcl.manager.protocol;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
 
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tcl.base.http.IProviderCallback;
import com.tcl.base.http.NewPostEntityProvider;
import com.tcl.manager.application.ManagerApplication; 
import com.tcl.manager.protocol.data.WhiteListPojo;
import com.tcl.manager.protocol.data.WhiteListPojo.WhiteInfo;
import com.tcl.manager.util.AndroidUtil;
import com.tcl.manager.util.Constant; 
import com.tcl.manager.util.LogUtil;
import com.tcl.manager.util.SerUtil;
import com.tcl.manager.util.StringUtils; 
import com.tcl.manager.util.UrlConfig;


/** 
 * @Description: 获取白名单
 * @author pengcheng.zhang 
 * @date 2014-12-23 下午7:39:33 
 * @copyright TCL-MIE
 */
public class WhiteListProtocol extends NewPostEntityProvider<WhiteListPojo>
{

	private static final String TAG = "WhiteListProtocol";
	
	/**
	 * 获取白名单接口
	 * 
	 * @param callback 返回参数：系统请求完毕后回调接口
	 */
	public WhiteListProtocol(IProviderCallback<WhiteListPojo> callback)
	{
		super(callback); 
	}

	@Override
	public String getURL()
	{
		return UrlConfig.URL_FIND_WHITE_LIST;  
	}

	@Override
	public Map<String, String> getParams()
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("imei", AndroidUtil.getImei(ManagerApplication.sApplication.getApplicationContext()));
		
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
			if (!jsonObj.has(Constant.CODE))
			{
				return;
			}
			LogUtil.v(TAG + jsonObj.toString());
			setResult(parse(jsonObj)); 
		}
		catch (Exception e)
		{
			e.printStackTrace();
			setResult(null); 
		} 
	}
	
	private WhiteListPojo parse(JSONObject jsonObj)
	{
		try
		{
			String status = jsonObj.getString(Constant.CODE);
			if (StringUtils.isEmpty(status) || !Constant.CODE_OK.equals(status))
			{
				return null;
			}
			if (!jsonObj.has(Constant.DATA))
			{ 
				return null;
			}
			JSONArray jsonArray = jsonObj.getJSONArray(Constant.DATA);
			int len = 0;
			if ((len = jsonArray.length()) == 0)
			{
				return null;
			}     
			
			WhiteListPojo pojo = new WhiteListPojo();
			pojo.code = Constant.CODE_OK; 
			pojo.data = new WhiteListPojo.Data();
			pojo.data.infos = new ArrayList<WhiteListPojo.WhiteInfo>();
			WhiteInfo info = null;
			for (int i = 0; i < len; i++)
			{
				jsonObj = jsonArray.getJSONObject(i); 
				if (jsonObj == null)
				{ 
					continue;
				}
				
				info = new WhiteInfo();
				if (jsonObj.has("id"))
				{ 
					info.id = jsonObj.getInt("id");
				}
				if (jsonObj.has("pkn"))
				{
					info.pkn = jsonObj.getString("pkn");
				}
				if (jsonObj.has("createTime"))
				{
					info.createTime = jsonObj.getString("createTime");
				}
				if (jsonObj.has("modifyTime"))
				{
					info.modifyTime = jsonObj.getString("modifyTime");
				}
				
				pojo.data.infos.add(info);
			}
			
			return pojo;
		}
		catch (Exception exp)
		{
			exp.printStackTrace();
			return null;
		}
	}

}
