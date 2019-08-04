package com.mogoo.commons;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.ContentResolver;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.mogoo.market.network.IBEManager;

/**
 * 为来让商城不依赖IBE也能上传销量，这个类的代码是
 * 直接从android.ibe.common.PhoneInfo拷贝而来
 * @author fdl
 *
 */
public class PhoneInfo {

	private static final String TAG = "PhoneInfo";

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

	// 获取设备ID
	public static String getUID(Context context) {
		String uid = null;
		uid = getMacAddress(context);
		if(TextUtils.isEmpty(uid))
		{
		    if (getSim(context) == null || getImei(context) == null) 
		    {
		        uid = IBEManager.getGUID();
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
//		if (getSim(context) == null || getImei(context) == null) {
//
//			if (uid == null) {
//				uid = MogooUtils.getGUID();
//				if (uid == null) {
//					return "";
//
//				}
//			}
//		} else {
//			uid = getSim(context) + getImei(context);
//		}

			Log.d(TAG, "getUIDLocation() uid1:" + uid);
		uid = IBEManager.MD5(uid);

			Log.d(TAG, "getUIDLocation() uid2:" + uid);
		return uid;
	}

	public static String getSim(Context context) {
		TelephonyManager tm = getTelephonyManager(context);
		String strResult = tm.getSimSerialNumber();


			Log.d(TAG, "Mogoo getSimLocation()===" + strResult);
		return strResult;

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

	public static String getSmsc() {
		// return smsc;
		String smsc = "";
		//SmsMessage sms = new SmsMessage();
		//smsc = sms.getServiceCenterAddress();

			Log.d(TAG, "----------SMSC--------------=" + smsc);
		return smsc;
	}

	public static String getPhoneNumber(Context context) {
		TelephonyManager tm = getTelephonyManager(context);
		String strResult = tm.getLine1Number();


			Log.d(TAG, "Mogoo getPhoneNumberLocation()===" + strResult);
		return strResult;
	}

	/**
	 * 
	 * @return
	 */
	public static String getResolution(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager WM = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		WM.getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		String resolution = (width + "x" + height).toString();

			Log.d(TAG, "resolution: " + resolution);
		return resolution;
	}

	/**
	 * 获取手机cpu串号
	 * 
	 * @return
	 */
	public static String getCPUSerial() {

		String str = "", strCPU = "", cpuAddress = "0000000000000000";
		try {
			Process pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			for (int i = 1; i < 100; i++) {
				str = input.readLine();
				if (str != null) {
					if (str.indexOf("Serial") > -1) {
						strCPU = str.substring(str.indexOf(":") + 1,
								str.length());
						cpuAddress = strCPU.trim();
						break;
					}
				} else {
					break;
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return cpuAddress;

	}

	/**
	 * 获取手机ip地址
	 * 
	 * @return
	 */
	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
	
				Log.e("WifiPreference IpAddress", ex.toString());
		}
		return null;
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
	 private static boolean getPersistedWifiEnabled(Context context) {
	        final ContentResolver cr = context.getContentResolver();
	        try {
	            return Settings.Secure.getInt(cr, Settings.Secure.WIFI_ON) == 1;
	        } catch (Settings.SettingNotFoundException e) {
	            Settings.Secure.putInt(cr, Settings.Secure.WIFI_ON, 0);
	            return false;
	        }
	    }
}
