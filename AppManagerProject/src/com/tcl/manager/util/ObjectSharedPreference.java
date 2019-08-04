package com.tcl.manager.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @author zhejun.zhu
 * @Description:面向对象的SharedPreference存储辅助类
 * @date 2014年11月7日
 * @copyright TCL-MIE
 * 
 *            适合应用内信息的存储，当数据量很大的时候效率会很低
 */
public class ObjectSharedPreference {
    public static final String SHAREDPREFERENCES_NAME = "ObjectSharedPreference";

    SharedPreferences mPreferences;

    public ObjectSharedPreference(Context context) {
        mPreferences = context.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public ObjectSharedPreference(Context context, String sharedpreferencesName) {
        mPreferences = context.getSharedPreferences(sharedpreferencesName, Context.MODE_PRIVATE);
    }

    public void save(Object object) {
        Gson gson = new Gson();
        Editor editor = mPreferences.edit();
        editor.putString(object.getClass().getSimpleName(), gson.toJson(object));
        editor.commit();
    }

    public void save(String key, Object object) {
        Gson gson = new Gson();
        Editor editor = mPreferences.edit();
        editor.putString(key, gson.toJson(object));
        editor.commit();
    }

    public <T> T get(Class<T> T) {
        String value = mPreferences.getString(T.getSimpleName(), "");
        Gson gson = new Gson();
        T t = gson.fromJson(value, T);
        return t;
    }

    public <T> T get(String key, Class<T> T) {
        String value = mPreferences.getString(key, "");
        Gson gson = new Gson();
        T t = gson.fromJson(value, T);
        return t;
    }

    public <T> void saveList(String key, List<T> list) {
        if (list == null)
            return;
        Gson gson = new Gson();

        List<String> strlist = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            strlist.add(gson.toJson(list.get(i)));
        }

        Editor editor = mPreferences.edit();
        editor.putString(key, gson.toJson(strlist));
        editor.commit();
    }

    public <T> List<T> getList(String key, Class<T> T) {
        String value = mPreferences.getString(key, "");
        Gson gson = new Gson();

        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        List<String> strList = gson.fromJson(value, type);
        if (strList == null)
            return null;

        List<T> list = new ArrayList<T>();
        for (int i = 0; i < strList.size(); i++) {
            list.add(gson.fromJson(strList.get(i), T));
        }
        return list;
    }
}
