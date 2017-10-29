package com.iskyinfor.duoduo.ui;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

public class ReadSdcardResource {
	private static Context mContext = null;
	private static File file = null;
	 

	public ReadSdcardResource(Context con) 
	{
		mContext = con;
	}

	public static ArrayList<String> getFromSdcardVideo()
	{
		ArrayList<String> videoList = new ArrayList<String>();
		
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) 
		{
			file = new File(Environment.getExternalStorageDirectory().toString() + "/video");
		} 
		else 
		{
			Toast.makeText(mContext, "SD卡不存在或者安装有误，请查看", Toast.LENGTH_SHORT).show();
		}

		File[] files = file.listFiles();
		
		if (files == null || files.length == 0)  return videoList;
		
		for(int i = 0;i<files.length;i++)
		{
			File fill = files[i];
			if(isVideoFile(fill.getPath()))
			{
				String path = fill.getPath();
				videoList.add(path);
			}
		}
		return videoList;
	}

	// 判断是否是视频文件
	private static boolean isVideoFile(String fName) {
		boolean flag;

		String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();

		if (end.equals("3gp") || end.equals("mp4") || end.equals("flv")|| end.equals("swf")) 
		{
			flag = true;
		} 
		else
		{
			flag = false;
		}
		return flag;
	}

	
	//判断是否是flash文件
	public static boolean isVideoFlashFile(String flashName) {
		boolean flag=false;
		
		File file=new File(flashName);
		
		if(file != null && file.exists())
		{
			if(file.getName().endsWith(".pdf")||file.getName().endsWith(".pdf"))
			{
				flag = true;
			}
		}
		
		return flag;
	}
	

	
}
