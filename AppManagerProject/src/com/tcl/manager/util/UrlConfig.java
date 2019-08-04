package com.tcl.manager.util;

public class UrlConfig
{
	private static final String	URL_HOST1				= "http://api.appmanager.tclclouds.com";
	private static final String	URL_HOST2				= "http://idol3-1.tclclouds.com/ostore-api";
	private static final String URL_HOST3               = "http://10.128.175.248:38080/appmgr-webapp";
	
	/** 设备信息 */
	public static final String	URL_SAVE_DEVICE_INFO	= URL_HOST1 + "/deviceinfo/saveDeviceInfo.json";
	/** 数据上报 */
	public static final String	URL_SAVE_DATA_LOG		= URL_HOST1 + "/datalog/saveDatalog.json";
	/** 获取白名单 */
	public static final String	URL_FIND_WHITE_LIST		= URL_HOST3 + "/whitelist/findWhiteLists.json";
	/** 获取版本信息 */
	public static final String	URL_GET_VERSION_INFO	= URL_HOST2 + "/api/screen1update";
	/** 获取推荐列表 */
	public static final String	URL_GET_RECOMMEND_LIST	= URL_HOST2 + "/api/applist/samecategory";
	
}