package com.mogoo.log;

import java.util.logging.Logger;

import android.util.Log;



public class MyLogger
{	

	
	public  static void debug(Class cs,String module,String message) 
	{
		if(cs==null) return;
		
		String[] classinfo = LogUtil.getInvokeClassInfo(cs.getName());

		Log.d(classinfo[1], "[DEBUG]" + LogUtil.formatLog(module,classinfo, message));
	}
	
	public static void info(Class cs,String module,String message)  
	{
		if(cs==null) return;
		String[] classinfo = LogUtil.getInvokeClassInfo(cs.getName());
		
		Log.i(classinfo[1], "[INFO]" + LogUtil.formatLog(module,classinfo, message));
	}
	
	public static void warn(Class cs,String module,String message)  
	{
		if(cs==null) return;
		String[] classinfo = LogUtil.getInvokeClassInfo(cs.getName());
		
		Log.w(classinfo[1], "[WARN]" + LogUtil.formatLog(module,classinfo, message));
	}
	public static void error(Class cs,String module,String message)  
	{
		if(cs==null) return;
		String[] classinfo = LogUtil.getInvokeClassInfo(cs.getName());
		
		Log.e(classinfo[1], "[ERROR]" + LogUtil.formatLog(module,classinfo, message));
	}

	public  static void debug(Class cs,String module,String message, Throwable t) 
	{
		if(cs==null) return;
		String[] classinfo = LogUtil.getInvokeClassInfo(cs.getName());

		Log.d(classinfo[1], "[DEBUG]" + LogUtil.formatLog(module,classinfo, message,t));
		//t.printStackTrace();
	}
	
	public static void info(Class cs,String module,String message, Throwable t) 
	{
		if(cs==null) return;
		String[] classinfo = LogUtil.getInvokeClassInfo(cs.getName());
		
		Log.i(classinfo[1], "[INFO]" + LogUtil.formatLog(module,classinfo, message,t));
		//t.printStackTrace();
	}
	
	public static void warn(Class cs,String module,String message, Throwable t) 
	{
		String[] classinfo = LogUtil.getInvokeClassInfo(cs.getName());
		
		Log.w(classinfo[1], "[WARN]" + LogUtil.formatLog(module,classinfo, message,t));
		//t.printStackTrace();
	}
	
	
	public static void error(Class cs,String module,String message, Throwable t) 
	{
		if(cs==null) return;
		String[] classinfo = LogUtil.getInvokeClassInfo(cs.getName());
		
		Log.e(classinfo[1], "[ERROR]" + LogUtil.formatLog(module,classinfo, message,t));
		//t.printStackTrace();
	}	
}
