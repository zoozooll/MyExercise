package com.iskyinfor.duoduo.ui;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;

public class UiHelp {
	public static final String UserInfo = UIPublicConstant.UserInfo;

	/**
	 * 获取用户的id
	 * 
	 * @param context
	 *            上下文对象
	 * @return 用户的id
	 */
	public static String getUserShareID(Context context) {
		SharedPreferences preferences = null;
		preferences = context.getSharedPreferences(UserInfo, 0);
		return preferences.getString("account", "account");
	}

	/**
	 *  过滤特殊字符
	 * @param str
	 * @return
	 * @throws PatternSyntaxException
	 * @author admin
	 */
	public static String StringFilter(String str) throws PatternSyntaxException 
	{
		// 只允许字母和数字
		// String regEx = "[^a-zA-Z0-9]";
		// 清除掉所有特殊字符
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}    
	
	/**
	 * 判断SD卡的状态
	 * SDCardExist()
	 */

	public static boolean SDCardExist() 
	{
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			//表明对象是否存在并具有读/写权限
			return true;
		}else if(Environment.getExternalStorageState().equals(Environment.MEDIA_CHECKING))
		{
			//表明对象正在磁盘检查
			return true;
		}else if(Environment.getExternalStorageState().equals(Environment.MEDIA_BAD_REMOVAL))
		{
			//表明SDCard被卸载前己被移除
			return false;
		}else if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY))
		{
			//表明对象仅有只读权限
			return true;
		}else if(Environment.getExternalStorageState().equals(Environment.MEDIA_NOFS))
		{
			//表明对象为空白或正在使用不受支持的文件系统
			return true;
		}else if(Environment.getExternalStorageState().equals(Environment.MEDIA_REMOVED))
		{
			//如果不存在 SDCard 返回，移除SD卡
			return false;
		}else if(Environment.getExternalStorageState().equals(Environment.MEDIA_SHARED))
		{
			//如果 SDCard 未安装 ，并通过 USB大容量存储共享返回
			return true;
		}else if(Environment.getExternalStorageState().equals(Environment.MEDIA_UNMOUNTABLE))
		{
			//返回 SDCard 不可被安装 ,如果 SDCard是存在但不可以被安装
			return false;
		}else if(Environment.getExternalStorageState().equals(Environment.MEDIA_UNMOUNTED))
		{
			//返回 SDCard已卸掉，如果 SDCard是存在但是没有被安装
			return false;
		}else{
			return false;
		}
	}
	
	/**
	 * 一键到达首页功能；
	 * @param context
	 * @return
	 * @Author Aaron Lee
	 * @date 2011-7-14 下午06:57:46
	 */
	public static boolean turnHome(Activity context){
		Intent intent = new Intent();
		intent.setClass(context, IndexActivity.class);
		context.startActivity(intent);
		context.finish();
		return false;
		
	}

	/**
	 * 年份
	 * @return
	 */
	public static ArrayList<String> getAllyearData()
	{
		ArrayList<String> list = new ArrayList<String>();
		
		for(int i = 1900;i<3000;i++)
		{
			list.add("" + i);
		}
		
		return list;
	}
	
	/**
	 * 月份
	 * @return
	 */
	public static ArrayList<String> getAllMonthData()
	{
		ArrayList<String> list = new ArrayList<String>();
		
		for(int i = 1;i<=12;i++)
		{
			list.add("" + i);
		}
		
		return list;
	}
	
	/**
	 * 天
	 * @return
	 */
	public static ArrayList<String> getAllDayData()
	{
		ArrayList<String> list = new ArrayList<String>();
		
		for(int i = 1;i<= 31;i++)
		{
			list.add("" + i);
		}
		
		return list;
	}
}
