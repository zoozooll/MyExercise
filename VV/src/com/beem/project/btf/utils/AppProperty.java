package com.beem.project.btf.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import android.util.Log;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.BuildConfig;

/**
 * @ClassName: AppProperty
 * @Description: 应用的配置类
 * @author: yuedong bao
 * @date: 2015-10-26 下午5:08:21
 */
public class AppProperty {
	//LogUtil类日志级别
	//	public int curLogLevel = Log.VERBOSE;
	//是否开启smack的日志
	//	public boolean debuggerEnabled = false;
	//开启xmpp压缩
	public boolean compressionEnable = false;
	//xmpp主机地址
	public String XMPPSERVER_HOST = "vv.evieo.com";//"192.168.12.41"
	//xmpp端口地址
	public int XMPPSERVER_PORT = 15222;
	//xmpp服务名
	public String XMPPSERVER_SERVENAME = "vv";
	//xmpp资源
	public String XMPPSERVER_RESOUCE = "android";
	// 端口
	public int VVPORT = 8888;
	//http地址
	public String VVAPI = "http://" + XMPPSERVER_HOST + ":" + VVPORT;
	
	//ImageLoder是否开启日志
	//	public boolean writeLogs = false;
	//是否开启日志服务
	public boolean logServiceEnable = false;
	//是否Roster登录时自动下载
	public boolean isAutoRosterLoaded = false;
	//========================================================================
	//聊天上传 
	public final String UPLOAD_CHAT = "/upload_chat";
	//获取验证码
	public final String GET_CHECKNUM = "/get_sign_up_vcode?mobile=";
	//检验验证码
	public final String REGIST_SEND = "/verify_vcode_sms";
	//获取版本号
	public final String UPDATE_URL = "/get_android_ver";
	//注册，找回密码等的验证码
	public final String VERITY_CODE = "&verity_code=";
	//忘记密码（手机号）
	public final String FORGET_PASSWORD_MOBILE = "/forget_password_sms?mobile=";
	//忘记密码（时光号）
	public final String FORGET_PASSWORD_TMID = "/forget_password_tm?tm_id=";
	//更新的apk名字
	public final String UPDATE_FILENAME = "vv1.0.apk";
	//软件介绍的url
	public final String INTRODUCTION_URL = "http://portal.vv.evieo.com/";
	// 获得附近的人
	public final static String GET_NEIGHBORS = "get_neighbors";
	
	public final static String GET_STARTUP = "/get_startup";
	/**
	 * 上传头像图片类型
	 */
	public final String UPLOAD_PORTRAIT = "/upload_portrait";
	//运行版本是否内网版本 ture-内网，false-外网
	public boolean isInlineVer = false;
	//配置map，数组中第一个值对应内网版本，第二个值对应外网版本
	private Map<String, Object[]> configMap = new HashMap<String, Object[]>();
	{
		configMap.put("curLogLevel", new Object[] { Log.VERBOSE, Log.INFO });
		configMap.put("debuggerEnabled", new Object[] { true, false });
		configMap.put("compressionEnable", new Object[] { true, true });
		configMap.put("XMPPSERVER_HOST", new Object[] { XMPPSERVER_HOST,
				XMPPSERVER_HOST});
		configMap.put("XMPPSERVER_PORT", new Object[] { 15222, 15222 });
		configMap.put("XMPPSERVER_SERVENAME", new Object[] { "vv", "vv" });
		configMap.put("XMPPSERVER_RESOUCE",
				new Object[] { "android", "android" });
		configMap.put("writeLogs", new Object[] { true, false });
		configMap.put("logServiceEnable", new Object[] { true, false });
		configMap.put("isAutoRosterLoaded", new Object[] { false, false });
		configMap.put("httpport", new Object[] { 8894, VVPORT });
	}

	public void initConfig() {
		//从sd卡读取配置文件
		switchEditVersion(false, true);
	}
	public void switchEditVersion(boolean isInline, boolean... isFirstInits) {
		boolean isFirstInit = isFirstInits.length > 0 ? isFirstInits[0] : false;
		final Properties properties = new Properties();
		File file = BBSUtils.getAppCacheDir(BeemApplication.getContext(),
				"property");
		final File properFile = new File(file, "property.cfg");
		boolean isPropertyInit = false;
		if (properFile.exists()) {
			//无配置文件
			FileInputStream s;
			try {
				s = new FileInputStream(properFile);
				properties.load(s);
				isPropertyInit = true;
				if (isFirstInit) {
					//如果是第一次初始化配置，是否是内线版本依靠配置中的主机地址来判定
					String host = properties.getProperty("XMPPSERVER_HOST",
							String.valueOf(XMPPSERVER_HOST));
					String hostMap = (String) configMap.get("XMPPSERVER_HOST")[0];
					isInline = hostMap.equals(host);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.isInlineVer = isInline;
		//没有配置文件和配置文件错误
		if (!isPropertyInit) {
			for (String key : configMap.keySet()) {
				Object[] val = configMap.get(key);
				properties.setProperty(key, (isInline) ? String.valueOf(val[0])
						: String.valueOf(val[1]));
			}
			//调试版，将日志级别设置与内部版本同
			if (BuildConfig.DEBUG) {
				properties.setProperty("curLogLevel",
						String.valueOf(configMap.get("curLogLevel")[0]));
				properties.setProperty("compressionEnable",
						String.valueOf(false));
				properties.setProperty("debuggerEnabled",
						String.valueOf(configMap.get("debuggerEnabled")[0]));
				properties.setProperty("writeLogs",
						String.valueOf(configMap.get("writeLogs")[0]));
			}
			if (isInline) {
				//配置文件不存在且是内线版本是写文件
				saveConfig(properFile.getPath(), properties);
			}
		}
		String host = properties.getProperty("XMPPSERVER_HOST",
				String.valueOf(XMPPSERVER_HOST));
		String hostMap = (String) configMap.get("XMPPSERVER_HOST")[isInline ? 0
				: 1];
		if (!hostMap.equals(host)) {
			properties.setProperty("XMPPSERVER_HOST", hostMap);
			saveConfig(properFile.getPath(), properties);
		}
		//		curLogLevel = Integer.parseInt(properties.getProperty("curLogLevel", String.valueOf(curLogLevel)));
		//		debuggerEnabled = Boolean.parseBoolean(properties.getProperty("debuggerEnabled",
		//				String.valueOf(debuggerEnabled)));
		compressionEnable = Boolean.parseBoolean(properties.getProperty(
				"compressionEnable", String.valueOf(compressionEnable)));
		XMPPSERVER_HOST = (properties.getProperty("XMPPSERVER_HOST",
				String.valueOf(XMPPSERVER_HOST)));
		XMPPSERVER_PORT = Integer.parseInt((properties.getProperty(
				"XMPPSERVER_PORT", String.valueOf(XMPPSERVER_PORT))));
		//		writeLogs = Boolean.parseBoolean(properties.getProperty("writeLogs", String.valueOf(writeLogs)));
		logServiceEnable = Boolean.parseBoolean(properties.getProperty(
				"logServiceEnable", String.valueOf(logServiceEnable)));
		isAutoRosterLoaded = Boolean.parseBoolean(properties.getProperty(
				"isAutoRosterLoaded", String.valueOf(isAutoRosterLoaded)));
		VVPORT = Integer.parseInt((properties.getProperty(
				"httpport", String.valueOf(VVPORT))));
		VVAPI = "http://" + XMPPSERVER_HOST + ":" + VVPORT;
		//LogUtils.setLogLevel(curLogLevel);
	}
	public void saveConfig(String strPath, Properties properties) {
		try {
			FileOutputStream s = new FileOutputStream(strPath, false);
			properties.store(s, new Date().toGMTString());
		} catch (Exception e) {
			try {
				FileOutputStream s = new FileOutputStream(strPath, false);
				properties.store(s, new Date().toGMTString());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	private AppProperty() {
		initConfig();
	}
	public static AppProperty getInstance() {
		return instance;
	}

	private static AppProperty instance = new AppProperty();

	@Override
	public String toString() {
		return "AppProperty [" + " XMPPSERVER_HOST=" + XMPPSERVER_HOST
				+ ", XMPPSERVER_PORT=" + XMPPSERVER_PORT
				+ ", XMPPSERVER_SERVENAME=" + XMPPSERVER_SERVENAME
				+ ", XMPPSERVER_RESOUCE=" + XMPPSERVER_RESOUCE + ", VVAPI="
				+ VVAPI + ", logServiceEnable=" + logServiceEnable
				+ ", UPLOAD_CHAT=" + UPLOAD_CHAT + ", GET_CHECKNUM="
				+ GET_CHECKNUM + ", REGIST_SEND=" + REGIST_SEND
				+ ", UPDATE_URL=" + UPDATE_URL + ", VERITY_CODE=" + VERITY_CODE
				+ ", FORGET_PASSWORD_MOBILE=" + FORGET_PASSWORD_MOBILE
				+ ", FORGET_PASSWORD_TMID=" + FORGET_PASSWORD_TMID
				+ ", UPDATE_FILENAME=" + UPDATE_FILENAME
				+ ", INTRODUCTION_URL=" + INTRODUCTION_URL
				+ ", UPLOAD_PORTRAIT=" + UPLOAD_PORTRAIT + "]";
	}
}
