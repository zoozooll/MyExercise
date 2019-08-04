package com.tcl.manager.protocol.data;
 
import java.util.ArrayList;
import java.util.HashMap; 

import org.json.JSONArray;
import org.json.JSONObject; 

/**
 * @Description: 日志上报数据实体
 * 
 * @author pengcheng.zhang
 * @date 2014-12-25 上午10:17:48
 * @copyright TCL-MIE
 */
public class LogInfo
{
	/** 手机imei值 */
	public String imei;
	/** 用户账号 */
	public String user_account;
	/** 手机剩余可用内存，单位byte */
	public long available_ram;
	/** 手机内存总量,单位byte */
	public long total_ram;
	/** 按照算法得出的总分 */
	public int total_score;
	/** 当前手机的温度,单位℃ */
	public float temperature;
	/** 当前手机剩余电量单位% */
	public int battery_level;
	/** 记录时，手机系统的时间戳 */
	public long write_time;
	/** 记录当前时刻手机的时区例如“GMT +8:00” */
	public String write_timezone;
	/** 单个应用信息列表 */
	public ArrayList<AppInfo> list;

	public LogInfo()
	{
		list = new ArrayList<LogInfo.AppInfo>();
	} 

	/** 单个应用信息 */
	public static class AppInfo
	{
		/** 当天到某一时刻APP的wifi流量,单位byte */
		public long wifi_data;
		/** 当天到某一时刻APP的非wifi流量单位byte */
		public long mobile_data;
		/** 当天到某一时刻APP的前台启动次数 */
		public int usage_count;
		/** 当天到某一时刻APP的前台使用时间单位毫秒 */
		public long usage_time;
		/** 按指定算法得出单个app的分数 */
		public int apk_score;
		/** 本版本指cpu的占用时长，以后可能有其他信息，可以解析 */
		public String battery_consume;
		/** APK的包名 */
		public String package_name;
		/** Apk应用的大小，单位byte */
		public long apk_size;
		/** Apk应用数据data的大小，单位byte */
		public long apk_data;
		/** Apk应用数据cache的大小，单位byte */
		public long apk_cache;
	}

	public HashMap<String, String> toHashMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("imei", imei);
		map.put("user_account", user_account);
		map.put("available_ram", String.valueOf(available_ram));
		map.put("total_ram", String.valueOf(total_ram));
		map.put("total_score", String.valueOf(total_score));
		map.put("temperature", String.valueOf(temperature));
		map.put("battery_level", String.valueOf(battery_level));
		map.put("write_time", String.valueOf(write_time));
		map.put("write_timezone", write_timezone); 
		
		return map;
	}
	
	public String listToString()
	{
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObj = null; 
		for (AppInfo info : list)
		{
			jsonObj = new JSONObject();
			try
			{
				jsonObj.put("wifi_data", String.valueOf(info.wifi_data));
				jsonObj.put("mobile_data", String.valueOf(info.mobile_data));
				jsonObj.put("usage_count", String.valueOf(info.usage_count));
				jsonObj.put("usage_time", String.valueOf(info.usage_time));
				jsonObj.put("apk_score", String.valueOf(info.apk_score));
				jsonObj.put("battery_consume", info.battery_consume);
				jsonObj.put("package_name", info.package_name);
				jsonObj.put("apk_size", String.valueOf(info.apk_size));
				jsonObj.put("apk_data", String.valueOf(info.apk_data));
				jsonObj.put("apk_cache", String.valueOf(info.apk_cache));
				jsonArray.put(jsonObj);
			}
			catch (Exception e)
			{
				e.printStackTrace(); 
			}
		}  

		return jsonArray.toString();
	}
	
}
