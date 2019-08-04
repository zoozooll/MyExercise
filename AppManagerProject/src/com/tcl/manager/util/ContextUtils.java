package com.tcl.manager.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;

import com.tcl.framework.log.NLog;

public class ContextUtils {
	
	public static boolean isMainThread() {
		long id = Thread.currentThread().getId();
		return id == Looper.getMainLooper().getThread().getId();
	}

	public static String getMetaData(Context context, String name) {
		PackageManager packageManager = context.getPackageManager();
		ApplicationInfo applicationInfo;
		Object value = null;
		try {

			applicationInfo = packageManager.getApplicationInfo(
					context.getPackageName(), 128);
			if (applicationInfo != null && applicationInfo.metaData != null) {
				value = applicationInfo.metaData.get(name);
			}

		} catch (Exception e) {
			NLog.printStackTrace(e);
			NLog.w("ContextUtils",
					"Could not read the name(%s) in the manifest file.", name);
			return null;
		}

		return value == null ? null : value.toString();
	}
	
	public static boolean isGlobal(Context context) {
		String global = ContextUtils.getMetaData(context, "GLOBAL");
		if (TextUtils.isEmpty(global))
			return false;
		
		return global.equalsIgnoreCase("YES");
	}
	
	public static String getVersionName(Context context) {
		PackageManager packageManager = context.getPackageManager();
		try {

			PackageInfo pi = packageManager.getPackageInfo(context.getPackageName(), 0);
			if (pi != null ) {
				return pi.versionName;
			}

		} catch (Exception e) {
			NLog.printStackTrace(e);
			
		}
		
		return null;
	}
	
	public static int getVersionCode(Context context) {
		PackageManager packageManager = context.getPackageManager();
		try {

			PackageInfo pi = packageManager.getPackageInfo(context.getPackageName(), 0);
			if (pi != null ) {
				return pi.versionCode;
			}

		} catch (Exception e) {
			NLog.printStackTrace(e);			
		}
		
		return 0;
	}

	/**
	 * Open the activity to let user allow wifi feature in Settings app.
	 * 
	 * @param context
	 *            from which invoke this method
	 */
	public static void openWIFISettings(Context context) {
		Intent intent = new Intent();
		intent.setAction(Settings.ACTION_WIFI_SETTINGS);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	public static String getCurrentProcessName(Context context) {
		int pid = android.os.Process.myPid();
		ActivityManager mActivityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		
		List<ActivityManager.RunningAppProcessInfo> processInfos = mActivityManager
				.getRunningAppProcesses();
		
		for (ActivityManager.RunningAppProcessInfo appProcess : processInfos) {
			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		
		return null;
	}

	public static boolean isChildProcess(Context context) {
		String process = getCurrentProcessName(context);
		String pkName = context.getPackageName();

		if (!pkName.equals(process))
			return true;
		
		return false;
	}
	
	public static boolean install(Context context, String apkPath) {
		if (TextUtils.isEmpty(apkPath)) {
			NLog.w("ContextUtils", "download complete intent has no path param");
			return false;
		}
		
		File file = new File(apkPath);
		if (!file.exists()) {
			NLog.w("ContextUtils", "file %s not exists", apkPath);
			return false;
		}
		
		if(isSystemApp(context)){
			return systemInstall(apkPath);
		}else{
			File apkFile = new File(apkPath);
			Intent intent = new Intent(Intent.ACTION_VIEW); 
	        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive"); 
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startActivity(intent);
	        return true;
		}
	}
	
	/**
	 * 是否为系统应用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isSystemApp(Context context) {
		return ((context.getApplicationInfo()).flags & ApplicationInfo.FLAG_SYSTEM) > 0;
	}
	
	public static boolean systemInstall(String apkPath) {
		String result = sysInstall(apkPath).trim();
		int lastIndex = result.lastIndexOf("/n");
		if (lastIndex == -1) {
			return false;
		}
		result = result.substring(lastIndex + 2).toLowerCase();
		return "success".equals(result);
	}
	
	/**
	 * 系统级自动安装
	 * 
	 * @param apkPath
	 * @return
	 */
	public static String sysInstall(String apkPath) {
		String[] args = { "pm", "install", "-r", apkPath };
		String result = "";
		ProcessBuilder processBuilder = new ProcessBuilder(args);
		Process process = null;
		InputStream errIs = null;
		InputStream inIs = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int read = -1;
			process = processBuilder.start();
			errIs = process.getErrorStream();
			while ((read = errIs.read()) != -1) {
				baos.write(read);
			}
			baos.write('\n');
			inIs = process.getInputStream();
			while ((read = inIs.read()) != -1) {
				baos.write(read);
			}
			byte[] data = baos.toByteArray();
			result = new String(data, Charset.defaultCharset());
		} catch (Exception e) {
			NLog.printStackTrace(e);			
		} finally {
			try {
				if (errIs != null) {
					errIs.close();
				}
				if (inIs != null) {
					inIs.close();
				}
			} catch (IOException e) {
				
			}
			if (process != null) {
				process.destroy();
			}
		}
		return result;
	}
	
	/**
	 * 静默安装APK， 需要ROOT权限
	 * 
	 * @param apkPath APK的文件路径
	 * @return
	 */
	public static boolean installSilent(String apkPath) {
		int result = -1;
		DataOutputStream dos = null;
		String cmd = "pm install -r " + apkPath;
		try {
			Process p = Runtime.getRuntime().exec("su");
			dos = new DataOutputStream(p.getOutputStream());
			dos.writeBytes(cmd + "\n");
			dos.flush();
			dos.writeBytes("exit\n");
			dos.flush();
			p.waitFor();
			result = p.exitValue();
		} catch (Exception e) {
			NLog.printStackTrace(e);
		} finally {
			if (dos != null) {
				try {
					dos.close();
				} catch (IOException e) {
					NLog.printStackTrace(e);
				}
			}
		}
		return result == 0;
	}
	
	public static boolean isHome(Context context) {
		ActivityManager mActivityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
		List<String> homePackageNames = getHomes(context);
		return homePackageNames.contains(rti.get(0).topActivity
				.getPackageName());
	}

	private static List<String> getHomes(Context context) {
		List<String> names = new ArrayList<String>();
		PackageManager packageManager = context.getPackageManager();
		// 属性
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(
				intent, PackageManager.MATCH_DEFAULT_ONLY);
		for (ResolveInfo ri : resolveInfo) {
			names.add(ri.activityInfo.packageName);
		}
		return names;
	}
	
	public static ComponentName topActivity(Context context) {
		ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE) ;
		try {
			List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1) ;
			 if(runningTaskInfos != null && runningTaskInfos.size() > 0) {
				 ComponentName component = runningTaskInfos.get(0).topActivity;
				 return component;
			}
		} catch (Exception e) {
			NLog.printStackTrace(e);			
		}
		
		return null;
	}

	private final static int kSystemRootStateUnknow = -1;
	private final static int kSystemRootStateDisable = 0;
	private final static int kSystemRootStateEnable = 1;
	private static int sRootState = kSystemRootStateUnknow;

	/**
	 * 判断系统是否已经ROOT
	 * @return
	 */
	public static boolean hasSystemRooted() {
		
		if (sRootState == kSystemRootStateEnable) {
			return true;
		} else if (sRootState == kSystemRootStateDisable) {
			return false;
		}
		
		File f = null;
		final String kSuSearchPaths[] = { "/system/bin/", "/system/xbin/",
				"/system/sbin/", "/sbin/", "/vendor/bin/" };
		try {
			for (int i = 0; i < kSuSearchPaths.length; i++) {
				f = new File(kSuSearchPaths[i] + "su");
				if (f != null && f.exists()) {
					sRootState = kSystemRootStateEnable;
					return true;
				}
			}
		} catch (Exception e) {
		}
		
		sRootState = kSystemRootStateDisable;
		return false;
	}
}
