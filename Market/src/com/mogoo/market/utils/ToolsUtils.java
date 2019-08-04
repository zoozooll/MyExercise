package com.mogoo.market.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.mogoo.components.download.MogooDownloadManager;
import com.mogoo.market.MarketApplication;
import com.mogoo.market.R;
import com.mogoo.market.database.DaoFactory;
import com.mogoo.market.network.IBEManager;
import com.mogoo.market.ui.ManagerActivity;
import com.mogoo.market.uicomponent.MyToast;

public class ToolsUtils {

	/**
	 * 检查网络、SD卡，再下载
	 */
	public static boolean checkBeforeDownload(Context context,long size) 
	{
		if(AppUtils.getSDKpath() != null && !AppUtils.getSDKpath().equals("")) 
		{
			// 网络是否可用
			if (ToolsUtils.isAvailableNetwork(context)) 
			{
				if (size < ToolsUtils.getSdcardAvailableSize()) 
				{
					return true;
				}
				else
				{
					MyToast.makeText(context,
							context.getResources().getText(R.string.sdcard_full),
							Toast.LENGTH_SHORT).show();
					return false;
				}
			}
			else 
			{
				MyToast.makeText(context,context.getResources().getText(R.string.tip_network_inactive),Toast.LENGTH_SHORT).show();
			}
		}
		else
		{
			MyToast.makeText(context,context.getResources().getText(R.string.install_no_sdcard),Toast.LENGTH_SHORT).show();
		}
		
		return false;
	}
	
	/**
	 * 下载文件
	 * @param manager
	 * @param savePath 保存路径
	 * @param apkFullName 保存全名:name_version.apk
	 * @param url 下载地址
	 * @return
	 */
	public static long downloadApk(MogooDownloadManager manager,String savePath,String apkFullName,String url)
    {
    	// 创建下载目录(在SDCARD中)
		Environment.getExternalStoragePublicDirectory(savePath).mkdirs();
		// 保存到SDCARD的name目录中，并把保存的文件名设为rrr.mp3
		apkFullName.replace(" ", "");
		manager.setDestinationInExternalPublicDir(savePath, apkFullName);
		manager.setTitle(apkFullName);
		String uid = IBEManager.getInstance().getUid();
		String newUrl=url+"?uid="+uid;
		//****************************测试*************************************
		if(MarketApplication.debug)
		{
			newUrl = newUrl.replace("test.htw.cc", "192.168.10.5");
		}
		//****************************测试*************************************
		
		return manager.startDownload(newUrl);
    }
	
	/**
	 * 取消下载,稍微统一下接口
	 */
	public static void cancelDownload(Context context,MogooDownloadManager mogooDownloadManager,long downloadId) 
	{
		mogooDownloadManager.cancelDownload(downloadId);                //系统取消下载
		DaoFactory.getDownloadInfoDao(context).delete(downloadId+"");   //下载管理删除记录
		DownPrefsUtil.getInstance(context).removePresValue(String.valueOf(downloadId));   //SharePreference是否下载，删除记录
	}
	
	/**
	 * 暂停下载
	 * @param downloadId
	 * @param apkId
	 */
	public static void pauseDownload(Context context,MogooDownloadManager manager,String downloadId,String apkId)
	{
		manager.pauseDownload(Long.parseLong(downloadId));
		DownPrefsUtil.getInstance(context).savePresValue(downloadId, apkId, String.valueOf(false));
	}
	
	/**
	 * 重新下载
	 * @param downloadId
	 * @param apkId
	 */
	public static void restartDownload(Context context,MogooDownloadManager manager,String downloadId,String apkId)
	{
		manager.restartDownload(Long.parseLong(downloadId));
		DownPrefsUtil.getInstance(context).savePresValue(downloadId, apkId, String.valueOf(true));
	}
	
	/**
	 * 流转字符串
	 * 
	 * @param is
	 * @return
	 */
	public static String streamToString(InputStream is) {
		if (is == null)
			return "";
		String line = null;
		StringBuffer sb = new StringBuffer("");
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 获取指定APK的资源对象
	 * 
	 * @param context
	 * @param apkPath
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Resources getResources(Context context, String apkPath)
			throws Exception {

		String PATH_AssetManager = "android.content.res.AssetManager";
		Class assetMagCls = Class.forName(PATH_AssetManager);
		Constructor assetMagCt = assetMagCls.getConstructor((Class[]) null);
		Object assetMag = assetMagCt.newInstance((Object[]) null);
		Class[] typeArgs = new Class[] { String.class };
		Method assetMag_addAssetPathMtd = assetMagCls.getDeclaredMethod(
				"addAssetPath", typeArgs);
		Object[] valueArgs = new Object[] { apkPath };
		assetMag_addAssetPathMtd.invoke(assetMag, valueArgs);

		Resources resources = context.getResources();
		typeArgs = new Class[] { assetMag.getClass(),
				resources.getDisplayMetrics().getClass(),
				resources.getConfiguration().getClass() };
		Constructor resourcesCt = Resources.class.getConstructor(typeArgs);
		valueArgs = new Object[] { assetMag, resources.getDisplayMetrics(),
				resources.getConfiguration() };

		resources = (Resources) resourcesCt.newInstance(valueArgs);
		return resources;
	}

	/**
	 * 获取apk图标
	 * 
	 * @param context
	 * @param apkPath
	 * @return
	 */
	public static Drawable getDrawable(Context context, String apkPath) {
		Drawable drawable = null;
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo packageInfo = pm.getPackageArchiveInfo(apkPath,
					PackageManager.GET_ACTIVITIES);
			Resources res = getResources(context, apkPath);
			if (packageInfo != null) {
				ApplicationInfo appInfo = packageInfo.applicationInfo;
				drawable = res.getDrawable(appInfo.icon);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return drawable;
	}

	/**
	 * 获取apk图标
	 * 
	 * @param resources
	 * @param packageInfo
	 * @return
	 */
	public static Drawable getDrawable(Resources resources,
			PackageInfo packageInfo) {
		Drawable drawable = null;
		try {
			if (resources != null && packageInfo != null) {
				ApplicationInfo appInfo = packageInfo.applicationInfo;
				drawable = resources.getDrawable(appInfo.icon);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return drawable;
	}

	/**
	 * 获取apk名称
	 * 
	 * @param resources
	 * @param packageInfo
	 * @return
	 */
	public static CharSequence getLabel(Resources resources,
			PackageInfo packageInfo) {
		CharSequence label = null;
		try {
			if (resources != null && packageInfo != null) {
				ApplicationInfo appInfo = packageInfo.applicationInfo;
				label = resources.getText(appInfo.labelRes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return label;
	}

	/**
	 * 安装apk
	 * 
	 * @param context
	 * @param apkPath
	 * @return
	 */
	public static void onInstallApk(Context context, String apkPath) {
		Intent intentInstall = new Intent(Intent.ACTION_VIEW);
		intentInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intentInstall.setDataAndType(Uri.fromFile(new File(apkPath)),
				"application/vnd.android.package-archive");
		context.startActivity(intentInstall);
	}

	public static void onUninsatllApk(Context context, String packageName,
			int requestCode) {
		Uri packageUri = Uri.fromParts("package", packageName, null);
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageUri);
		// context.startActivity(uninstallIntent);
		((Activity) context).startActivityForResult(uninstallIntent,
				requestCode);
	}

	// 判断应用是否安
	public static boolean isPkgInstalled(Context context, String packageName) {
		PackageManager pm = context.getPackageManager();
		try {
			pm.getPackageInfo(packageName, 0);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 获取包信息
	public static PackageInfo getPackageInfo(Context context, String packageName) {
		PackageManager pm = context.getPackageManager();
		try {
			return pm.getPackageInfo(packageName, 0);
		} catch (Exception e) {
			return null;
		}
	}

	// 判断是否也有可用的网络
	public static boolean isAvailableNetwork(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		if (networkInfo != null) {
			return networkInfo.isAvailable();
		} else {
			return false;
		}

	}

	// 判断APK是否已经下载
	public static boolean isDownAPk(Context context, String path, String apkName) {
		boolean tag = false;
		File file = new File(path);
		File[] files = file.listFiles();
		if (files == null)
			return tag;
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile() && files[i].exists()) {
				String apkPath = files[i].getPath();
				if (apkPath.toLowerCase().endsWith(".apk")) {
					try {
						if (apkPath.equals(path + apkName)) {
							tag = true;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return tag;
	}
	
	/**
	 * 返回apkName的完整名称
	 */
	public static String getFullApkName(String apkName,String versionCode)
	{
		return apkName+"_"+versionCode+".apk";
	}
	
	/**
	 * 返回apkName的完整路径
	 */
	public static String getFullApkPath(String apkName,String versionCode)
	{
		return Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+MarketApplication.SavaPath+getFullApkName(apkName,versionCode);
	}
	
	//判断APK是否已经下载
	public static boolean isDownloadedAPk(Context context, String path, String apkName) {
		boolean result = false;		
		String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		File file = new File(rootPath+"/"+path+apkName);		
		if(file.exists())
			result = true;
		return result;
	}

	// 解析APK是否完整
	public static boolean checkApk(Context context, String apkPath, String apkId) {
		boolean flag = false;
		// 如果apk正在下载或暂停下载认为apk不完整.
		if(DownPrefsUtil.getInstance(context).containsValue(apkId)) {
			return flag;
		}
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageArchiveInfo(apkPath,
							PackageManager.GET_ACTIVITIES);
			if (packageInfo != null) {
				Resources resources = getResources(context, apkPath);
				if (resources != null) {
					flag = true;
				} else {
					deleteFile(apkPath);
				}
			} else {
				deleteFile(apkPath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	private static void deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.exists()) {
			boolean b = file.delete();
			Log.i("test", "delete file: " + fileName + " : " + b);
		}
	}
	
	public static void deleteFile(Context context,String filepath)
	{
		File file = new File(filepath);
		if(file.exists())
		{
			file.delete();
			
			Intent i = new Intent(ManagerActivity.BROADCAST_ACTION_FILE_DELETE);
			context.sendBroadcast(i);
		}
	}

	// 文件大小
	public static String getSizeStr(String sizeStr) {
		String strSize = "";
		try {
			float size = Float.valueOf(sizeStr);
			if (size >= 1024 * 1024 * 1024) {
				strSize = (float) Math.round(10 * size / (1024 * 1024 * 1024))
						/ 10 + " GB";
			} else if (size >= 1024 * 1024) {
				strSize = (float) Math.round(10 * size / (1024 * 1024 * 1.0))
						/ 10 + " MB";
			} else if (size >= 1024) {
				strSize = (float) Math.round(10 * size / (1024)) / 10 + " KB";
			} else if (size >= 0) {
				strSize = size + " B";
			} else {
				strSize = "0 B";
			}
		} catch (Exception e) {
			e.printStackTrace();
			strSize = "0 B";
		}
		return strSize;
	}
	
	public static String getSizeStr(int totalSize) {
		String strSize = "";
		float size = totalSize;
		if (size >= 1024 * 1024 * 1024) {
			strSize = (float) Math.round(10 * size / (1024 * 1024 * 1024))
					/ 10 + " G";
		} else if (size >= 1024 * 1024) {
			strSize = (float) Math.round(10 * size / (1024 * 1024 * 1.0))
					/ 10 + " M";
		} else if (size >= 1024) {
			strSize = (float) Math.round(10 * size / (1024)) / 10 + " K";
		} else if (size >= 0) {
			strSize = size + " B";
		} else {
			strSize = "0 B";
		}
		return strSize;
	}

	// 获得sdcard剩余空间
	public static long getSdcardAvailableSize() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			StatFs sf = new StatFs(sdcardDir.getPath());
			long blockSize = sf.getBlockSize();
			// long blockCount = sf.getBlockCount();
			long availableCount = sf.getAvailableBlocks();
			return availableCount * blockSize;
		} else {
			return 0;
		}
	}

	/** 隐藏软键盘 */
	public static void onHiddenInput(Context context, EditText t) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(t.getWindowToken(), 0);
	}

	/** 提示网络不可用 */
	public static void Toast_network(Context context) {
		if (!isAvailableNetwork(context)) {
//			Toast.makeText(
//					context,
//					context.getResources().getText(
//							R.string.tip_network_inactive), Toast.LENGTH_SHORT)
//					.show();
			LayoutInflater inflater=LayoutInflater.from(context);
	        View toastview=inflater.inflate(R.layout.network_inactive_layout,null).
	        findViewById(R.id.network_inactive_tip);
	        Toast toast=new Toast(context);
	        toast.setDuration(Toast.LENGTH_LONG);
	        toast.setView(toastview);
	        toast.show();

		}
	}
	
	/** 提示“再按一次返回键退出蘑菇市场” */
	public static void showExitToast(Context context) {
		MyToast.makeText(context, R.string.exit_toast_text, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 反射机制来处理状态栏
	 * */
	public static void setStatusBarIphoneStyle(Activity activity) {
		Object[] args = new Object[2];

		final String setStyleMethod = "setStatusBarStyle";
		final String style = "IPHONE_STYLE_BG";
		final String className = "android.iphone.app.MogooActivity";

		Class ownerClass;
		try {
			ownerClass = Class.forName(className);
			Field field = ownerClass.getField(style);
			Object property = field.get(null);
			args[0] = property;
			args[1] = new Boolean(false);

			Method method = Activity.class.getDeclaredMethod(setStyleMethod,
					new Class[] { int.class, boolean.class });
			method.setAccessible(true);
			method.invoke(activity, args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
