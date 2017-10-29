package com.iskyinfor.duoduo.downloadManage;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.util.Log;

import com.iskyinfor.duoduo.downloadManage.DownloadTask.NetState;
import com.iskyinfor.duoduo.downloadManage.utils.NetworkUtil;

public class DownloadUtil {
	/**
     * 
     */
	private static final int MAX_PROGRESS = 1000;
	public static Long getTotalSize(String url) {
		Long totalSize = null;
		try {
			totalSize = NetworkUtil.getContentSize(url);
		} catch (Exception e) {
			totalSize = 0L;
			e.printStackTrace();
		}
		Log.i("peng", "getTotalSize=====:" + totalSize);
		return totalSize;
	}

	public static Long getCurrentSize(File tempFile) {
		if (tempFile != null && tempFile.isFile() && tempFile.exists()) {
			return tempFile.length();
		} else {
			return 0L;
		}
	}

	public static int getProgress(Long totalSize, Long currentSize) {
		int progress = 0;
		if (currentSize < totalSize) {
			progress = (int) (currentSize * MAX_PROGRESS / totalSize);
		} else {
			progress = MAX_PROGRESS;
		}
		return progress;
	}

	/**
	 * 构建 1123KB/4567KB 样式的已下载大小样式.
	 * 
	 * @param item
	 * @return
	 */
	public static String setCurrentSizeStyle(long currentSize, long totalSize) {
		StringBuffer sb = new StringBuffer();
		sb.append(currentSize / 1024);
		sb.append("KB/");
		sb.append(totalSize / 1024);
		sb.append("KB");
		return sb.toString();
	}

	/**
	 * 非法字符过滤
	 * 
	 * @param str
	 * @return
	 */
	public static String filterSpecialString(String str) {
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\n\r]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		String result = m.replaceAll("-").trim();
		return result;
	}
	
	public static int getCurrentNetState(Context context) {
		String activeNetType = NetworkUtil.getActiveNetworkType(context);
		int netState = NetState.MOBILE | NetState.WIFI;
		if (activeNetType.equalsIgnoreCase("MOBILE")) {
			netState = NetState.MOBILE;
		} else if (activeNetType.equalsIgnoreCase("WIFI")) {
			netState = NetState.WIFI;
		}
		return netState;
	}
	
	

}
