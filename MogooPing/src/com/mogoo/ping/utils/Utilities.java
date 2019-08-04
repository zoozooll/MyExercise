package com.mogoo.ping.utils;





import java.io.File;

import com.mogoo.ping.R;
import com.mogoo.ping.ctrl.RemoteApksManager;

import android.content.Context;
import android.content.Intent;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.net.Uri;
import android.os.Environment;
import android.util.TypedValue;

public class Utilities {
	
	 /**
	  * 
	  * @Author lizuokang
	  * Check whether the software is installed,and return the intent of the software
	  * @param context
	  * @param packageName
	  * @return the launched intent
	  * @Date 下午5:37:46  2012-10-13
	  */
	 public static Intent launcheredAble(Context context, String packageName) {
		Intent launchIntent = null;
		try {
			launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
		} catch (Exception e) {
			launchIntent = null;
		}
		return launchIntent;
	 }
	 
	 public static float dpiToPixle(Resources res, float dpi) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpi, res.getDisplayMetrics());
	 }
	 
	/**
	 * Java�ļ����� ��ȡ�ļ���չ��
	 * 
	 * Created on: 2011-8-2 Author: blueeagle
	 */
	public static String getExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return filename;
	}

	/**
	 * Java�ļ����� ��ȡ������չ����ļ���
	 * 
	 * Created on: 2011-8-2 Author: blueeagle
	 */
	public static String getFileNameNoEx(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length()))) {
				return filename.substring(0, dot);
			}
		}
		return filename;
	}
	
	/**
	 * Java�ļ����� ��ȡ������չ����ļ���
	 * 
	 * Created on: 2011-8-2 Author: blueeagle
	 */
	public static String getFileNameNoExFromFullPath(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			final int dot = filename.lastIndexOf('.');
			final int slash = filename.lastIndexOf('/');
			if ((dot > slash) && (dot < (filename.length()))) {
				return filename.substring(slash, dot);
			}
		}
		return filename;
	}
	
	/**
	 * 
	 * @Author lizuokang
	 * TODO
	 * @param flag
	 * @return
	 * @Date 下午5:25:04  2012-10-13
	 */
	public static int getSoftwareTypeStr(int flag) {
		switch (flag) {
		case RemoteApksManager.TAG_APPLICATIONS_LASTED:
			return R.string.apptype_showstring_appslasted;
		case RemoteApksManager.TAG_APPLICATIONS_RECOMEND:
			return R.string.apptype_showstring_appsrecommend;
		case RemoteApksManager.TAG_GAME_LASTED:
			return R.string.apptype_showstring_gameslasted;
		case RemoteApksManager.TAG_GAME_RECOMEND:
			return R.string.apptype_showstring_gamesrecomend;
		default:
			return 0;
		}
	}
	
	/**
	 * @Author lizuokang
	 * TODO
	 * @param context
	 * @param localApkUri
	 * @Date 下午5:24:36  2012-10-13
	 */
	public static void installSaveApk(Context context, Uri localApkUri) {
		Intent intent;
		intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(localApkUri, "application/vnd.android.package-archive");
		context.getApplicationContext().startActivity(intent);
	}
	
	/**
	 * 
	 * @Author lizuokang
	 * TODO Check if sdcard is writeable 
	 * @return
	 * @Date 下午5:47:51  2012-10-16
	 */
	public static boolean isCanUseSdCard() { 
	    try { 
	        return Environment.getExternalStorageState().equals( 
	                Environment.MEDIA_MOUNTED); 
	    } catch (Exception e) { 
	        e.printStackTrace(); 
	    } 
	    return false; 
	} 
	
	
	public static ApplicationInfo getApkCode(Context context, File file) throws Exception {
		String name_s = file.getName();
		String apk_path = null;
		// MimeTypeMap.getSingleton()
		if (name_s.toLowerCase().endsWith(".apk")) {
			apk_path = file.getAbsolutePath();// apk文件的绝对路劲
			// System.out.println("----" + file.getAbsolutePath() + "" +
			// name_s);
			PackageManager pm = context.getPackageManager();
			PackageInfo packageInfo = pm.getPackageArchiveInfo(apk_path, PackageManager.GET_ACTIVITIES);
			ApplicationInfo appInfo = packageInfo.applicationInfo;

			 
			/* *//**获取apk的图标 *//*
			appInfo.sourceDir = apk_path;
			appInfo.publicSourceDir = apk_path;
			Drawable apk_icon = appInfo.loadIcon(pm);
			*//** 得到包名 *//*
			String packageName = packageInfo.packageName;
			*//** apk的绝对路劲 *//*
			//myFile.setFilePath(file.getAbsolutePath());
			*//** apk的版本名称 String *//*
			String versionName = packageInfo.versionName;
			//myFile.setVersionName(versionName);
			*//** apk的版本号码 int *//*
			int versionCode = packageInfo.versionCode;
			//myFile.setVersionCode(versionCode);
			*//**安装处理类型*//*
			int type = doType(pm, packageName, versionCode);
			myFile.setInstalled(type);
			 
			Log.i("ok", "处理类型:"+String.valueOf(type)+"\n" + "------------------我是纯洁的分割线-------------------");
			myFiles.add(myFile);*/
			return appInfo;
		}
		return null;
	}
}
