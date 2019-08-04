package com.mogoo.commons;
import android.content.Context;
import android.content.SharedPreferences;

import com.mogoo.commons.FileUtils;

public class SharedPrefUtils {

	/**
	 * 取得偏好文件中指定字段的值
	 */
	public static String getFiledString(String prefFileName,String filedName,String defaultValue){
		 SharedPreferences sp = FileUtils.getSharedPreferencesEx(prefFileName,Context.MODE_WORLD_READABLE);
		 String result = sp.getString(filedName, defaultValue);
		 
        return result;       
	}
	
	/**
	 * 取得偏好文件中指定字段的值
	 */
	public static int getFiledInt(String prefFileName,String filedName,int defaultValue){
		 SharedPreferences sp = FileUtils.getSharedPreferencesEx(prefFileName,Context.MODE_WORLD_READABLE);
		 int result = sp.getInt(filedName, defaultValue);
		
		 return result;       
	}
	
	/**
     * 取得偏好文件中指定字段的值
     */
    public static boolean getFiledBoolean(String prefFileName,String filedName,boolean defaultValue){
    	 SharedPreferences sp = FileUtils.getSharedPreferencesEx(prefFileName,Context.MODE_WORLD_READABLE);
         boolean result = sp.getBoolean(filedName, defaultValue);
        
         return result;       
    }
    
    /**
     * 取得偏好文件中指定字段的值
     */
    public static float getFiledFloat(String prefFileName,String filedName,float defaultValue){
    	SharedPreferences sp = FileUtils.getSharedPreferencesEx(prefFileName,Context.MODE_WORLD_READABLE);
        float result = sp.getFloat(filedName, defaultValue);
       
        return result;       
   }
    
    /**
     * 取得偏好文件中指定字段的值
     */
    public static long getFiledLong(String prefFileName,String filedName,long defaultValue){
    	SharedPreferences sp = FileUtils.getSharedPreferencesEx(prefFileName,Context.MODE_WORLD_READABLE);
        long result = sp.getLong(filedName, defaultValue);
       
        return result;       
   }
    
    /**
     * 设置偏好文件中指定字段的值
     */
    public static boolean setFiled(String prefFileName,String filedName,String filedValue){
    	SharedPreferences sp = FileUtils.getSharedPreferencesEx(prefFileName,
                Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
    	SharedPreferences.Editor editor = sp.edit();
    	editor.putString(filedName, filedValue) ;
    	
    	boolean success = editor.commit() ;
    	
    	return success ;
    }
    
    /**
     * 设置偏好文件中指定字段的值
     */
    public static boolean setFiled(String prefFileName,String filedName,boolean filedValue){
    	SharedPreferences sp = FileUtils.getSharedPreferencesEx(prefFileName,
                Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
    	SharedPreferences.Editor editor = sp.edit();
    	editor.putBoolean(filedName, filedValue) ;
    	
    	boolean success = editor.commit() ;
    	
    	return success ;
    }
    
    /**
     * 设置偏好文件中指定字段的值
     */
    public static boolean setFiled(String prefFileName,String filedName,int filedValue){
    	SharedPreferences sp = FileUtils.getSharedPreferencesEx(prefFileName,
                Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
    	SharedPreferences.Editor editor = sp.edit();
    	editor.putInt(filedName, filedValue) ;
    	
    	boolean success = editor.commit() ;
    	
    	return success ;
    }
    
    /**
     * 设置偏好文件中指定字段的值
     */
    public static boolean setFiled(String prefFileName,String filedName,float filedValue){
    	SharedPreferences sp = FileUtils.getSharedPreferencesEx(prefFileName,
                Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
    	SharedPreferences.Editor editor = sp.edit();
    	editor.putFloat(filedName, filedValue) ;
    	
    	boolean success = editor.commit() ;
    	
    	return success ;
    }
    
    /**
     * 设置偏好文件中指定字段的值
     */
    public static boolean setFiled(String prefFileName,String filedName,long filedValue){
    	SharedPreferences sp = FileUtils.getSharedPreferencesEx(prefFileName,
                Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
    	SharedPreferences.Editor editor = sp.edit();
    	editor.putLong(filedName, filedValue) ;
    	
    	boolean success = editor.commit() ;
    	
    	return success ;
    }
    
    /**
     * 从偏好文件中移除某个键值
     */
    public static boolean deleteFiled(String prefFileName,String filedName){
    	SharedPreferences sp = FileUtils.getSharedPreferencesEx(prefFileName,
                Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
    	SharedPreferences.Editor editor = sp.edit();
    	
    	editor.remove(filedName) ;
    	
    	boolean success = editor.commit() ;
    	
    	return success ;
    }
}
