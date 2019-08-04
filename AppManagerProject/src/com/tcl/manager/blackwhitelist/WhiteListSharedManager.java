/**
 * 
 */
package com.tcl.manager.blackwhitelist;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tcl.manager.application.ManagerApplication;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author zuokang.li
 *
 */
public class WhiteListSharedManager {

	private static final String WHITE_LIST = "white_list";
	private SharedPreferences sp;
	
	private WhiteListSharedManager() {
		sp = ManagerApplication.sApplication.getSharedPreferences(WHITE_LIST, Context.MODE_PRIVATE);
	}
	
	private static class ClassHolder {
		private static final WhiteListSharedManager INSTANCE = new WhiteListSharedManager();
	}
	
	public static WhiteListSharedManager getSingleInstance() {
		return ClassHolder.INSTANCE;
	}
	
	public synchronized void appendWhiteList(String pkgName) {
		sp.edit().putInt(pkgName, 1).commit();
	}
	
	public synchronized void removeWhiteList(String pkgName) {
		if (sp.contains(pkgName)) {
			sp.edit().remove(pkgName).commit();
		}
	}
	
	public Set<String> getAllWhiteList() {
		Map<String, ?> map = sp.getAll();
		if (map == null) {
			return null;
		}
		Set<String> result = new HashSet<String>();
		for (Map.Entry<String, ?> entry : map.entrySet()) {
			Object value = entry.getValue();
			if (value instanceof Integer && ((Integer) value).compareTo(0) > 0) {
				result.add(entry.getKey());
			} 
		}
		return result;
	}
	
	public void clearWhiteList() {
		sp.edit().clear().commit();
	}
	
	public boolean isInWhiteList (String pkgName) {
		return sp.getInt(pkgName, 0) > 0;
	}
}
