package com.mogoo.ping.utils;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.UUID;

import com.mogoo.ping.MainActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * 主要与IBE打交道（除IM）
 * 
 * @author 张永辉
 * @date 2011-12-24
 */
public class IBEManager {
	
	public static final String TAG = "IBEManager";
	/**
	 * 应用ID
	 */
	private static final int APP_ID = 100111114;

	/**
	 * 服务器应用上下文名称
	 */
	private static final String SERVER_MAS = "MAS";

	private static IBEManager instance;

	private String akey;
	private String uid;
	private String masServer;
	private String serverAddr;

	private IBEManager() {
	}

	/**
	 * 取得实例
	 * 
	 * @author 张永辉
	 * @date 2011-10-25
	 * @return
	 */
	public static IBEManager getInstance() {
		if (instance == null) {
			instance = new IBEManager();
		}
		return instance;
	}

	/**
	 * 取得本应用ID
	 * 
	 * @author 张永辉
	 * @date 2011-12-24
	 * @return
	 */
	public static int getAppId() {
		return APP_ID;
	}

	/**
	 * 取得HTTP通信时要用的AKEY
	 * 
	 * @author 张永辉
	 * @date 2011-10-27
	 * @return
	 */
	public String getAKey() 
	{
		if (TextUtils.isEmpty(akey)) 
		{
			StringBuffer key = new StringBuffer();
			Class<?> appCM = null;
			try 
			{
				appCM = Class.forName("android.ibe.acp.AppConnectManager");
			} 
			catch (Exception e) 
			{
				Log.w(TAG, "call getAKey() in no IBE evironment");
			}
			
			if(appCM!=null)
			{
				/*AppConnectManager appConnectManager = AppConnectManager
						.getInstance();
				akey = appConnectManager.getAkey(getAppId());*/
				
				Method m;
				try 
				{
					Method methodGetInstance = appCM.getMethod("getInstance");
					Object appCMInstance = methodGetInstance.invoke(appCM);
					m = appCM.getMethod("getAkey",int.class);
					String kk = (String) m.invoke(appCMInstance, getAppId());
					key.append(kk);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
			akey = key.toString();
		}
		return akey;
	}
	
	public static boolean isIBEExist()
	{
//		Class<?> appCM = null;
//		try {
//			appCM = Class.forName("android.ibe.acp.AppConnectManager");
//		} catch (Exception e) {
//			Log.w(TAG, "Market is in no IBE evironment.");
//		}
//		return appCM != null;
		//return android.os.ServiceManager.getService("app_connect_service") != null;
		return false;
	}

	/**
	 * 取得UID
	 * 
	 * @author 张永辉
	 * @date 2011-10-27
	 * @return
	 */
	public String getUid() 
	{
		if (TextUtils.isEmpty(uid)) 
		{
			String bUid = null;
			Class<?> appCM = null;
			try 
			{
				appCM = Class.forName("android.ibe.acp.AppConnectManager");
			} 
			catch (Exception e) 
			{
				Log.w(TAG, "call getUid() in no IBE evironment");
			}
			
			if(appCM!=null && isIBEExist())
			{
				/*AppConnectManager appConnectManager = AppConnectManager
					.getInstance();
				uid = appConnectManager.getUID(getAppId());*/
				
				Method m;
				try 
				{
					Method methodGetInstance = appCM.getMethod("getInstance");
					Object appCMInstance = methodGetInstance.invoke(appCM);
					m = appCM.getMethod("getUID",int.class);
					String kk = (String) m.invoke(appCMInstance, getAppId());
					bUid = kk;
					Log.d(TAG, "bUid kk "+bUid);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
			if(TextUtils.isEmpty(uid))
			{
				bUid = getUID(MainActivity.getApplicationContext(null));
				Log.d(TAG, "bUid getUID "+bUid);
			}
			
			uid = bUid;
		}

		return uid;
	}

	/**
	 * 取得AID
	 * 
	 * @author 张永辉
	 * @date 2011-10-27
	 * @return
	 */
	public String getAid() 
	{
		StringBuffer bAid = new StringBuffer();
		Class<?> appCM = null;
		try 
		{
			appCM = Class.forName("android.ibe.acp.AppConnectManager");
		} 
		catch (Exception e) 
		{
			Log.w(TAG, "call getAid() in no IBE evironment");
		}
		
		if (appCM != null) 
		{
			/*
			 * AppConnectManager appConnectManager =
			 * AppConnectManager.getInstance();
			 * appConnectManager.getAccountID(getAppId());
			 */

			Method m;
			try {
				Method methodGetInstance = appCM.getMethod("getInstance");
				Object appCMInstance = methodGetInstance.invoke(appCM);
				m = appCM.getMethod("getAccountID", int.class);
				String kk = (String) m.invoke(appCMInstance,
						getAppId());
				bAid.append(kk);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else
		{
			// add by fdl 根据尹红宽的结论 AID公共的用store@motone.net
			return "store@motone.net";
		}
		
		return bAid.toString();
	}

	/**
	 * 取得MAS服务器地址
	 * 
	 * @author 张永辉
	 * @date 2011-12-20
	 * @return
	 */
	public String getMasServer() {
		if (TextUtils.isEmpty(masServer)) {
			//masServer = getServerAddr() + "/"+SERVER_MAS ;
			String ServerUlr;
			ServerUlr = getServerAddr();
			if(ServerUlr.contains("/MAS")){
				masServer=ServerUlr;
			}else{
				masServer=ServerUlr+"/"+SERVER_MAS ;
			}
		}
		return masServer;
	}

	/**
	 * 取得服务器地址 广告服务器和MAS服务器共用一个服务器地址,但属于服务器中不同的应用,
	 * 例如：服务器地址为http://192.168.0.177:8088,
	 * 则MAS服务器的地址为：http://192.168.0.177:8088/MAS，
	 * 广告服务器的地址为：http://192.168.0.177:8088/AD.
	 * 
	 * @author 张永辉
	 * @date 2012-2-10
	 * @return
	 */
	public String getServerAddr() 
	{
		if (TextUtils.isEmpty(serverAddr)) 
		{
			String bServerAddr = null;
			Class<?> appCM = null;
			try 
			{
				appCM = Class.forName("android.ibe.acp.AppConnectManager");
				
				/*AppConnectManager appConnectManager = AppConnectManager.getInstance();
				serverAddr = appConnectManager.getMasServer(getAppId());*/
				Method methodGetInstance = appCM.getMethod("getInstance");
				Method m = appCM.getMethod("getMasServer",int.class);
				Object appCMInstance = methodGetInstance.invoke(appCM);
				bServerAddr = (String) m.invoke(appCMInstance, getAppId());
			} 
			catch (Exception e) 
			{
				Log.w(TAG, "call getServerAddr() in no IBE evironment");
			}
			
			serverAddr = bServerAddr;
		}
		return serverAddr;
	}

	/**
	 * 是否用户已经登录
	 * 
	 * @author 张永辉
	 * @date 2011-10-28
	 * @return
	 */
	public boolean isLogin() {
		return getAid() != null && !"".equals(getAid().trim());
	}
	
	// 获取设备ID,从IBE里面拷贝来的代码
	private static String getUID(Context context) {
		String uid = null;
		uid = getMacAddress(context);
		if(TextUtils.isEmpty(uid))
		{
		    if (getSim(context) == null || getImei(context) == null) 
		    {
		        uid = getGUID();
		        if (uid == null)
		        {
                    return "";

                }
		    }
		    else
		    {
		        uid = getSim(context) + getImei(context);
		    }
		}
//			if (getSim(context) == null || getImei(context) == null) {
//
//				if (uid == null) {
//					uid = MogooUtils.getGUID();
//					if (uid == null) {
//						return "";
//
//					}
//				}
//			} else {
//				uid = getSim(context) + getImei(context);
//			}

		uid = MD5(uid);
		return uid;
	}
	
	/**
	 * MD5加密方法
	 *
	 * @param inStr
	 * @return
	 */
	public static String MD5(String inStr)
	{
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];

		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();

		for (int i = 0; i < md5Bytes.length; i++)
		{
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}
	
	/**
	 * 获取wifi mac地址
	 * 
	 * @return
	 */
	public  static String getMacAddress(Context context) {

		boolean bWifiOpen = false;
		String mWifiAddr = null;
		 WifiManager wifi =
		 (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		if (wifi == null) {
			return null;
		}
		if(!getPersistedWifiEnabled(context)){
		    Log.e(TAG,"---getPersistedWifiEnabled = FALSE");
		    bWifiOpen = true; 
		}
		if(!wifi.isWifiEnabled()){
		    Log.d(TAG, "---lyl---" + "wifi is closed");
		    wifi.setWifiEnabled(true);
		    //bWifiOpen = true;
		}
		WifiInfo info = wifi.getConnectionInfo();
		mWifiAddr = info.getMacAddress();
		if(bWifiOpen){
		    wifi.setWifiEnabled(false);
            bWifiOpen = false; 
		}
			Log.d(TAG, "---wifi mac地址---" + mWifiAddr);
		return mWifiAddr;
	}
	
	public static String getSim(Context context) {
		TelephonyManager tm = getTelephonyManager(context);
		String strResult = tm.getSimSerialNumber();


			Log.d(TAG, "Mogoo getSimLocation()===" + strResult);
		return strResult;

	}
	
	/**
	 * 获取电话服务
	 * 
	 * @param context
	 * @return
	 */
	private static TelephonyManager getTelephonyManager(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm;
	}
	
	private static boolean getPersistedWifiEnabled(Context context) {
        final ContentResolver cr = context.getContentResolver();
        try {
            return Settings.Secure.getInt(cr, Settings.Secure.WIFI_ON) == 1;
        } catch (Settings.SettingNotFoundException e) {
            Settings.Secure.putInt(cr, Settings.Secure.WIFI_ON, 0);
            return false;
        }
    }
	
	public static String getImei(Context context) {
		// if(!TextUtils.isEmpty(tmpIMEI))
		// {
		// return tmpIMEI;
		// }
		TelephonyManager tm = getTelephonyManager(context);
		String strResult = tm.getDeviceId();

			Log.d(TAG, "Mogoo getImeiLocation()===" + strResult);
		return strResult;
	}
	
	public static String getGUID()
	{
		UUID uuid = UUID.randomUUID();
		if (uuid == null)
		{

			Log.d(TAG, "getLocalGUID() uid is null");
			return null;
		}
		Log.d(TAG, "Mogoo getLocalGUID() uid ====" + uuid.toString());

		return uuid.toString();
	}
}
