package com.mogoo.market.utils;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 本地缓存 downloadId，apkId，和状态（是否正在下载）true是正在下载，false暂停，不用每次去查数据库
 * 当下载完成进，移除对应的值
 * @author lxr-motone
 *
 */
public class DownPrefsUtil {
    public static final String DOWN_PREFS_NAME = "download_info";
    public static final int INFO_APK_ID = 0;
    public static final int INFO_DOWN_LOAD_STATUS = 1;
    
	private Context mContext;
	private SharedPreferences mPrefs;
	
	public static DownPrefsUtil instance = null;
	
	public static DownPrefsUtil getInstance(Context context) 
	{
		if(instance==null)
		{
			instance = new DownPrefsUtil(context);
		}
		return instance;
	}
	
	public DownPrefsUtil(Context context) {
		this.mContext = context;
		this.mPrefs = mContext.getSharedPreferences(DOWN_PREFS_NAME, Context.MODE_PRIVATE);
	}
	
	public String getPrefsValue(String key, String defaultValue) {
		if(mPrefs == null)
			return "";
		return mPrefs.getString(key, defaultValue);
	}
	
	/**
	 * 获取所有的键值
	 */
	public static HashMap<String, String> getAll(Context context) 
	{
		SharedPreferences sp = context.getSharedPreferences(DOWN_PREFS_NAME, 0);
		@SuppressWarnings("unchecked")
		HashMap<String, String> all = (HashMap<String, String>) sp.getAll();
		return all;
	}
	
	/**
	 * 返回是否包含某一个值.
	 */
	public boolean containsValue(String apkId) {
		String downPauseFilter = apkId + "||false";
		String downIngFilter = apkId + "||true";
		HashMap<String, String> keyValues = getAll(mContext);
		if (keyValues != null
				&& (keyValues.containsValue(downPauseFilter) || keyValues
						.containsValue(downIngFilter))) {
			return true;
		}
		return false;
	}
	
	/**
	 * 开始下载时，保存
	 */
	public boolean savePresValue(String downloadId, String apkId, String isDownloading) {
		if(mPrefs == null)
			return false;
		String value = apkId + "||" + isDownloading;
		SharedPreferences.Editor editor = mPrefs.edit();
		editor.putString(downloadId, value);
		editor.commit();
		return true;
	}
	
	/**
	 * 1.接受到ACTION_DOWNLOAD_COMPLETE广播、2.取消下载,删除
	 */
	public void removePresValue(String key) {
		mPrefs.edit().remove(key).commit();
	}
	
	public String [] parseResponse(String response){
    	if(response == null){
    		throw new IllegalArgumentException("Parameter is null.") ;
    	}
    	String[] strs = response.split("\\|\\|");
    	return strs ;
    }
}
