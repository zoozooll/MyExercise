package com.iskyinfor.duoduo.downloadManage.utils;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

import com.iskyinfor.duoduo.downloadManage.setting.SettingUtils;

public class UrlAnalysisTool {

	public UrlAnalysisTool() {

	}

	/**
	 *检测网络连接是否可以下载
	 */

	public static synchronized boolean urlSniffer(String url) {
		boolean result = false;
		int counts = 0;
		Log.i("liu", "urlStr ===:"+url);
		URL urlStr;
		HttpURLConnection connection;
		if (checkUrl(url)) {
			while (counts < SettingUtils.DEFAULT_SNIFFER_COUNT) {
				try {
					urlStr = new URL(url);
					connection = (HttpURLConnection) urlStr.openConnection();
					int state = connection.getResponseCode();
					Log.i("liu", "urlStr ===:"+urlStr);
					Log.i("liu", "connection state===:"+state);
					if (state >= 200 && state < 300) {
						result = true;
						break;
					}else{
						counts++;
					}
				} catch (Exception ex) {
					counts++;
					Log.i("liu", "Exception net===:");
					ex.printStackTrace();
					continue;
				}
			}
		}
		
		Log.i("liu", "HttpURLConnection===:"+result);
		return result;
	}

	/**
	 * ���򵥵ļ�����ص�ַ�Ƿ����
	 * 
	 * @param url
	 * @return
	 */
	private static boolean checkUrl(String url) {
		boolean result = false;
		if (url == null || url.length() <= 0) {
			result = false;
		} else {
			result = true;
		}
		return result;
	}
	
}
