package com.iskyinfor.duoduo.downloadManage.utils;

public class LangUtil {
	
	/**
	 * 判断字符串 是否为空
	 */
	public static boolean isNull(String string) {
		if (string != null) {
			string = string.trim();
			if (string.length() != 0) {
				return false;
			}
		}
		return true;
	}
}
