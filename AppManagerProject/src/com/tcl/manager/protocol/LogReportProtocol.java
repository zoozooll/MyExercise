package com.tcl.manager.protocol;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import com.tcl.base.http.IProviderCallback;
import com.tcl.base.http.NewPostEntityProvider;
import com.tcl.manager.protocol.data.LogInfo;
import com.tcl.manager.util.Constant;
import com.tcl.manager.util.LogUtil;
import com.tcl.manager.util.StringUtils; 
import com.tcl.manager.util.UrlConfig;
import com.tcl.manager.util.ZlibUtil;

/**
 * @Description:日志上传
 * 
 * @author pengcheng.zhang
 * @date 2014-12-23 上午10:41:28
 * @copyright TCL-MIE
 */
public class LogReportProtocol extends NewPostEntityProvider<Boolean>
{
	
	private static final String		TAG			= "LogReportProtocol";
	private static final boolean	ZLIB_FLAG	= false;
	private HashMap<String, String>	mParams;
	private HashMap<String, byte[]>	mEntities;
	
	/**
	 * 日志上报接口
	 * 
	 * @param logoInfo 输入参数：需要上传的信息实体
	 * @param callback 返回参数：系统请求完毕后回调接口
	 */
	public LogReportProtocol(LogInfo logoInfo, IProviderCallback<Boolean> callback)
	{
		super(callback);
		init(logoInfo); 
	}
	
	@Override
	public String getURL()
	{
		return UrlConfig.URL_SAVE_DATA_LOG;
	}
	
	@Override
	public Map<String, String> getParams()
	{
		return mParams;
	}
	
	@Override
	public Map<String, byte[]> getPostEntities()
	{
		if (mEntities == null || mEntities.isEmpty())
		{
			return null;
		}
		
		return mEntities;
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
	
	private void init(LogInfo logInfo)
	{
		mParams = logInfo.toHashMap();
		String text = logInfo.listToString();
		if (!ZLIB_FLAG)
		{
			mParams.put("list", text);
			mParams.put("c", "0");
			return;
		}
		
		byte[] datas = text.getBytes();
		try
		{
			mEntities = new HashMap<String, byte[]>();
			mEntities.put("list", ZlibUtil.compress(datas, 0, datas.length));
			mParams.put("c", "1");
		}
		catch (Exception e)
		{
			mParams.put("list", text);
			mParams.put("c", "0");
			mEntities.clear();
			mEntities = null;
		}
	} 
}
