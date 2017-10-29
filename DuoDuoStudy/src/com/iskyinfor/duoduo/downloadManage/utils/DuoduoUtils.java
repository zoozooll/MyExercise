package com.iskyinfor.duoduo.downloadManage.utils;

public class DuoduoUtils {

	
	public static String showNewText(String text , int length) {
		if (text.length() >= length) {
			return text.substring(0 , length) + "...";
		}
		return text;
	}
	
}
