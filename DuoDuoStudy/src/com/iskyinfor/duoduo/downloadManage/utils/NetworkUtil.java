package com.iskyinfor.duoduo.downloadManage.utils;

import java.net.HttpURLConnection;

import org.apache.commons.pool.KeyedObjectPool;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.dcfs.esb.client.config.Config;
import com.iskyinfor.duoduo.downloadManage.pool.HttpURLConnectionPool;

public class NetworkUtil {

	/**
	 * 判断是否有可用网络链接 不管是GPRS 还是 WIFI
	 */
	public static boolean hasActiveNetwork(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = manager.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isAvailable()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否有可用网络链接 不管是GPRS 还是 WIFI
	 */
	public static String getActiveNetworkType(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = manager.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isAvailable()) {
			return netInfo.getTypeName();
		} else {
			return "";
		}
	}

	/**
	 * 获得下载URL的文件大小
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static long getContentSize(String url) throws Exception {
		long size = 0L;
		HttpURLConnection conn = null;
		KeyedObjectPool pool = HttpURLConnectionPool.getInstance();
		conn = (HttpURLConnection) pool.borrowObject(url);
//		conn.setRequestProperty("RANGE", "bytes=0-");
		conn.setRequestProperty("uid", Config.getLogProperty(Config.UID));
		//conn.setRequestProperty("series", strDes);
		conn.setRequestProperty("workDay", null);
		conn.setRequestProperty("flag", "A");
		conn.connect();
		size = conn.getContentLength();
		pool.returnObject(null, conn);
		return size;
	}

	/**
	 * 通过URL获取文件名
	 * 
	 * @param url
	 * @return 获取文件名
	 */
	public static String getFileName(String url) {
		String fileName = "";
		if (url == null) {
			return null;
		}

		int start = url.lastIndexOf("/");
		int end = url.lastIndexOf("?");

		if (start >= 0) {
			fileName = url.substring(start + 1);
			if (end >= 0) {
				fileName = url.substring(start + 1, end);
			}
		}
		return fileName;
	}

	/**
	 * 通过URL获取扩展名
	 * 
	 * @param url
	 * @return 扩展名
	 */
	public static String getExtension(String url) {
		String extension = "";
		if (url == null) {
			return null;
		}

		int start = url.lastIndexOf(".");
		int end = url.lastIndexOf("?");

		if (start >= 0) {
			extension = url.substring(start);
			if (end >= 0) {
				extension = url.substring(start, end);
			}
		}
		return extension;
	}
}
