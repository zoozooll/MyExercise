package com.tcl.manager.util;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.tcl.framework.crypt.Base64;
import com.tcl.framework.log.NLog;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * @Description:
 * @author pengcheng.zhang
 * @date 2014-12-22 下午7:46:01
 * @copyright TCL-MIE
 */
public class SerUtil
{
	private final static String TAG = "SerUtil";
	public static boolean unZip(JSONObject obj) throws JSONException, IOException
	{
		if (!obj.has(Constant.COMPRESS))
			return false;

		int value = obj.optInt(Constant.COMPRESS, 0);
		if (value == 0)
			return false;

		String zippedText = obj.getString(Constant.DATA);
		if (TextUtils.isEmpty(zippedText))
		{
			NLog.i(TAG, "no compress data text");
			return false;
		}

		byte[] zippedBytes = Base64.decode(zippedText.getBytes());
		if (zippedBytes == null || zippedBytes.length == 0)
		{
			NLog.i(TAG, "  decode base 64 text failed!");
			return false;
		}

		byte[] src = ZlibUtil.uncompress(zippedBytes);
		if (src == null || src.length == 0)
		{
			NLog.i(TAG, "   uncompress failed");
			return false;
		}
		String text = new String(src, "utf-8");
		JSONObject data = new JSONObject(text);
		obj.put(Constant.DATA, data);
		obj.put(Constant.COMPRESS, 0);

		return true;
	}
	
	public static HashMap<String, String> fin(Context context)
	{
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("imsi", AndroidUtil.getImsi(context));// "724556789123456789",
		// "46000342424234324"
		params.put("imei", AndroidUtil.getImei(context));
		params.put("model", Build.MODEL);// Constant.MODEL"RAV4_EMEA");
		params.put("language", context.getResources().getConfiguration().locale.toString());
		params.put("region", AndroidUtil.getMetaData(context, "REGION"));
		params.put("channel", AndroidUtil.getMetaData(context, "CHANNEL"));
		params.put("req_from", AndroidUtil.getMetaData(context, "TAG"));
		params.put("version_name", AndroidUtil.getVersionName(context));
		params.put("version_code", String.valueOf(AndroidUtil.getVersionCode(context)));
		// ----start add
		params.put("screen_size", AndroidUtil.getDisplayMetricsHeight(context) + "#" + AndroidUtil.getDisplayMetricsWidth(context));// 手机屏幕分辨率
		params.put("network", AndroidUtil.getnetworkInfoName(context));// 当前使用网络
		params.put("os_version_code", String.valueOf(AndroidUtil.getVersionSDKINT()));// android系统版本
		params.put("os_version", AndroidUtil.getVersionRelease());// android系统版本
		params.put("user_account", "");

		return params;
	}
}
