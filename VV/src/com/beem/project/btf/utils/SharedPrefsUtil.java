package com.beem.project.btf.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.beem.project.btf.constant.SettingKey;

/**
 * SharedPreferences存储数据方式工具类
 * @author yangle(网上抄的)
 */
public class SharedPrefsUtil {
	public static void putValue(Context context, String key, int value) {
		Editor sp = PreferenceManager.getDefaultSharedPreferences(context)
				.edit();
		sp.putInt(key, value);
		sp.commit();
	}
	public static void putValue(Context context, String key, boolean value) {
		Editor sp = PreferenceManager.getDefaultSharedPreferences(context)
				.edit();
		sp.putBoolean(key, value);
		sp.commit();
	}
	public static void putValue(Context context, String key, long value) {
		Editor sp = PreferenceManager.getDefaultSharedPreferences(context)
				.edit();
		sp.putLong(key, value);
		sp.commit();
	}
	public static void putValue(Context context, String key, String value) {
		Editor sp = PreferenceManager.getDefaultSharedPreferences(context)
				.edit();
		sp.putString(key, value);
		sp.commit();
	}
	public static int getValue(Context context, String key, int defValue) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		int value = sp.getInt(key, defValue);
		return value;
	}
	public static long getValue(Context context, String key, long defValue) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		long value = sp.getLong(key, defValue);
		return value;
	}
	public static boolean getValue(Context context, String key, boolean defValue) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean value = sp.getBoolean(key, defValue);
		return value;
	}
	public static String getValue(Context context, String key, String defValue) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		String value = sp.getString(key, defValue);
		return value;
	}
	public static void putValue(Context context, SettingKey key, int value) {
		putValue(context, key.toString(), value);
	}
	public static void putValue(Context context, SettingKey key, boolean value) {
		putValue(context, key.toString(), value);
	}
	public static void putValue(Context context, SettingKey key, String value) {
		putValue(context, key.toString(), value);
	}
	public static void putValue(Context context, SettingKey key, long value) {
		putValue(context, key.toString(), value);
	}
	public static int getValue(Context context, SettingKey key, int value) {
		return getValue(context, key.toString(), value);
	}
	public static long getValue(Context context, SettingKey key, long value) {
		return getValue(context, key.toString(), value);
	}
	public static boolean getValue(Context context, SettingKey key,
			boolean value) {
		return getValue(context, key.toString(), value);
	}
	public static String getValue(Context context, SettingKey key, String value) {
		return getValue(context, key.toString(), value);
	}
	//储存int数组
	public static void putValue(Context context, String key, int[] value) {
		Editor sp = PreferenceManager.getDefaultSharedPreferences(context)
				.edit();
		JSONArray jsonArray = new JSONArray();
		for (int b : value) {
			jsonArray.put(b);
		}
		sp.putString(key, jsonArray.toString());
		sp.commit();
	}
	//获取int数组
	public static Integer[] getValue(Context context, String key) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		List<Integer> list = new ArrayList<Integer>();
		int size = 0;
		try {
			JSONArray jsonArray = new JSONArray(sp.getString(key, "[]"));
			for (int i = 0; i < jsonArray.length(); i++) {
				list.add(jsonArray.getInt(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Integer[] arr = list.toArray(new Integer[size]);
		return arr;
	}
}
