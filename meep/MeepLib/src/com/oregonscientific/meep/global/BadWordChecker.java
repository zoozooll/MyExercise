package com.oregonscientific.meep.global;

import android.util.Log;

public class BadWordChecker {

	private String[] mBlacklist = null;
	private String[] mDefaultList = null;
	private String[] mBypassList = null;

	private boolean shouldUsePermissionManagerOnly=true;
	
	public BadWordChecker()
	{
		mDefaultList = DefaultBadwords.badwords;
		
	}

	public String[] getBlacklist() {
		return mBlacklist;
	}

	public void setBlacklist(String[] blacklist) {
		
		//2013-03-15 - raymond - always use default list
		//this.mBlacklist = blacklist;
		this.mBlacklist = mDefaultList;
		
		 //Log.d("BadWordChecker", "Checking lsit count = " + mBlacklist.length);
	}
	
	public boolean isStringSafe(String str)
	{
		
		if(shouldUsePermissionManagerOnly){
			return true;
		}
				
		//2013-03-26 - raymond - fine tune checking with trim() and filter H,69,666
		
		if(mBlacklist!= null){
		    Log.d("BadWordChecker", "Checking '" + str + "' against: " + mBlacklist.length);
		    String rewordStr = removeStringSymbols(str.trim().toLowerCase().replaceAll(" ","")).trim();
			for(int i =0; i<mBlacklist.length; i++)
			{
				
				String word = "" + mBlacklist[i].trim().toLowerCase() + "";
				if(rewordStr.indexOf("%")>=0 || rewordStr.indexOf("^")>=0 || rewordStr.indexOf("&")>=0 || rewordStr.indexOf("*")>=0){
					return false;
				}
				if(rewordStr.indexOf("!")>=0 || rewordStr.indexOf("@")>=0 || rewordStr.indexOf("#")>=0 || rewordStr.indexOf("$")>=0){
					return false;
				}
				if(rewordStr.indexOf("(")>=0 || rewordStr.indexOf(")")>=0 || rewordStr.indexOf(";")>=0 || rewordStr.indexOf(":")>=0){
					return false;
				}
				if(rewordStr.indexOf(",")>=0 || rewordStr.indexOf(".")>=0 || rewordStr.indexOf("/")>=0 || rewordStr.indexOf("\\")>=0){
					return false;
				}
				if(rewordStr.indexOf("'")>=0 || rewordStr.indexOf("\"")>=0){
					return false;
				}	
				if(rewordStr.equals("h") || rewordStr.equals("69") || rewordStr.equals("666")){
					return false;
				}
				//2013-03-28 - raymond - adhoc fix the invalid logic
				if(!rewordStr.equals("h") && !rewordStr.equals("69") && !rewordStr.equals("666") && !rewordStr.equals("hello") && rewordStr.equals(word))
				{
					return false;
				}

				
			}
		}
//		if(mDefaultList!= null)
//		{
//			String rewordStr = removeStringSymbols(str.trim().toLowerCase( ));
//			for(int i =0; i<mDefaultList.length; i++)
//			{
//				String word = " " + mDefaultList[i].trim().toLowerCase() + " ";
//				
//				if(rewordStr.contains(word))
//				{
//					return false;
//				}
//			}
//		}
		
		return true;
	}
	
	public static String removeStringSymbols(String str)
	{
		char[] list = new char[]{'!','@','#','$','%','^','&','*','(',')','-','+','{','}','[','}','\\',',','.','/',':',';','|','=','_','\'','\"' };
		
		for(int i=0; i<list.length; i++)
		{
			str = str.replace(list[i], ' ');
		}
		return " " + str + " ";
	}
	
	public String getSafeString(String oriStr)
	{
		String replacement = "***********************";
		String noSymbol = removeStringSymbols(oriStr).toLowerCase();
		for(int i =0; i<mDefaultList.length; i++)
		{
			String word = " " + mDefaultList[i].toLowerCase() + " ";
			if(noSymbol.contains(word))
			{
				int startIdx = noSymbol.indexOf(word);
				String firstPart = oriStr.substring(0, startIdx);
				String mid = replacement.substring(0, mDefaultList[i].length());
				String lastPart = oriStr.substring(startIdx + mDefaultList[i].length());
				oriStr = firstPart + mid + lastPart;//oriStr.replace(mDefaultList[i], replacement.substring(0, mDefaultList[i].length()));
			}
		}
		return oriStr;
	}

	public String[] getBypassList() {
		return mBypassList;
	}

	public void setBypassList(String[] bypassList) {
		this.mBypassList = bypassList;
	}
}
