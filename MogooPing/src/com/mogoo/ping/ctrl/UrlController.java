/**
 * 
 */
package com.mogoo.ping.ctrl;

import android.util.Log;

import com.mogoo.ping.utils.IBEManager;

/**
 * @author Aaron Lee
 * TODO
 * @Date ����9:29:25  2012-9-28
 */
public class UrlController {
	
	private final static String TAG = "UrlController";
	private final static String REMOTE_DOMAIN_NAME = "http://www.imogoo.cn/";
	/**测试服务器用的网址*/
	//private final static String REMOTE_DOMAIN_NAME = "http://test.htw.cc:9000/";
	/**路径前面部分*/
	private final static String REMOTE_PUBLIC_PATH = "MAS/Store/";
	private final static String LIST_APPLICATIONS_LASTED = "newappinfo.do?";
	private final static String LIST_APPLICATIONS_RECOMEND = "getapprank.do?";
	private final static String LIST_GAME_LASTED = "newgameinfo.do?";
	private final static String LIST_GAME_RECOMEND = "getgamerank.do?";
	private final static String LIST_ATTRIBUTES = "akey=%s&page=%s&pagesize=%s&uid=%s&ct=%s";
	
	private final static String REGISTER_ROOTPATH = "registermogoodesktop.do?";
	private final static String REGISTER_ATTRIBUTES = "akey=%s&uid=%s&appid=%s&ct=%s";
	
	private final static String REGISTERMOGOODESKTOP_ROOTPATH = "registermogoodesktopapp.do?";
	private final static String REGISTERMOGOODESKTOP_ATTRIBUTES = "akey=%s&uid=%s&appid=%s&ct=%s";

	private final static String DETAILED_ROOTPATH = "getAppInfo.do?";
	private final static String DETAILED_ATTRIBUTES = "akey=%s&uid=%s&apkid=%s&&ct=%s";

	
	private static IBEManager sIBEManager;
	
	static {
		sIBEManager = IBEManager.getInstance();
	}
	
	public static String getPathRemotePathApplicationsLast (String page, String pageSize) {
		String format = REMOTE_DOMAIN_NAME + REMOTE_PUBLIC_PATH
				+ LIST_APPLICATIONS_LASTED + LIST_ATTRIBUTES;
		return String.format(format, getAkey(), page, pageSize, getUid(), getCt());
	}

	public static String getPathRemotePathApplicationsRecomend (String page, String pageSize) {
		String format = REMOTE_DOMAIN_NAME + REMOTE_PUBLIC_PATH
				+ LIST_APPLICATIONS_RECOMEND + LIST_ATTRIBUTES;
		return String.format(format, getAkey(), page, pageSize, getUid(), getCt());
	}
	
	public static String getPathRemotePathGamesLast(String page, String pageSize) {
		String format = REMOTE_DOMAIN_NAME + REMOTE_PUBLIC_PATH
				+ LIST_GAME_LASTED + LIST_ATTRIBUTES;
		return String.format(format, getAkey(), page, pageSize, getUid(), getCt());
	}
	
	public static String getPathRemotePathGamesRecomend (String page, String pageSize) {
		String format = REMOTE_DOMAIN_NAME + REMOTE_PUBLIC_PATH
				+ LIST_GAME_RECOMEND + LIST_ATTRIBUTES;
		return String.format(format, getAkey(), page, pageSize, getUid(), getCt());
	}
	
	/**
	 * 
	 * @Author lizuokang
	 * get the url that the MogooPing first run to request server;
	 * @return
	 * @Date ����11:19:49  2012-9-28
	 */
	public static String getPathRegrestSelf() {
		String format = REMOTE_DOMAIN_NAME + REMOTE_PUBLIC_PATH
				+ REGISTER_ROOTPATH + REGISTER_ATTRIBUTES;
		return String.format(format, getAkey(), getUid(), getMyAppid(), getCt());
	}
	
	/**
	 * 
	 * @Title getPathRegisterMogooDesktop
	 * @Description get the url that the software first download with MogooPing;
	 * @Date 2012-10-16 上午9:54:30
	 * @Version 1.0
	 * @param appId
	 * @return
	 */
	public static String getPathRegisterMogooDesktop(String appId) {
		String format = REMOTE_DOMAIN_NAME + REMOTE_PUBLIC_PATH
				+ REGISTERMOGOODESKTOP_ROOTPATH + REGISTERMOGOODESKTOP_ATTRIBUTES;
		return String.format(format, getAkey(), getUid(), appId, getCt());
	}
	
	public static String getDetailed(String appId) {
		final String format = REMOTE_DOMAIN_NAME + REMOTE_PUBLIC_PATH
				+ DETAILED_ROOTPATH + DETAILED_ATTRIBUTES;
		return String.format(format, getAkey(), getUid(), appId, getCt());
	}

	private static String getMyAppid() {
		int key = sIBEManager.getAppId();
		return String.valueOf(key);
	}

	private static String getAkey() {
		String key = sIBEManager.getAKey();
		Log.d(TAG, "getAkey "+key);
		if (key == null || "".equals(key)){
			key = "123";
		}
		return key;
	}


	private static String getCt() {
		return "MGP";
	}


	private static String getUid() {
		String key = sIBEManager.getUid();
		Log.d(TAG, "getUid "+key);
		if (key == null || "".equals(key)){
			key = "123";
		}
		return key;
	}
	
}
