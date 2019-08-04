package com.mogoo.market.utils;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author csq -- 软件更新保存读取工具：key:packageName;value:downloadId。
 * 只是保存软件更新下载的包名和下载id，其他非更新的软件下载不保存。
 * 主要是为了判断是否在更新中
 */
public class UpdatesUtils {
	private static String prefName = "updates_id";

	/**
	 * 软件更新时保存
	 */
	public static void save(Context context, String packageName,
			String downloadId) {
		SharedPreferences sp = context.getSharedPreferences(prefName, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(packageName, downloadId);
		editor.commit();
	}

	public static String get(Context context, String packageName) {
		SharedPreferences sp = context.getSharedPreferences(prefName, 0);
		return sp.getString(packageName, null);
	}

	public static HashMap<String, String> getAll(Context context) {
		SharedPreferences sp = context.getSharedPreferences(prefName, 0);
		@SuppressWarnings("unchecked")
		HashMap<String, String> all = (HashMap<String, String>) sp.getAll();
		return all;
	}

	/**
	 * 更新下载失败、软件包变更时删除
	 */
	public static void delete(Context context, String packageName) {
		SharedPreferences sp = context.getSharedPreferences(prefName, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(packageName);
		editor.commit();
	}
	
	/**
	 * 更新下载失败、软件包变更时删除所有
	 */
	public static void deleteAll(Context context) {
		SharedPreferences sp = context.getSharedPreferences(prefName, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear();
		editor.commit();
	}
}
