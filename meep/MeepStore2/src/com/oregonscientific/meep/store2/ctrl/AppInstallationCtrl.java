package com.oregonscientific.meep.store2.ctrl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import com.oregonscientific.meep.store2.global.MeepStoreApplication;
import com.oregonscientific.meep.store2.global.MeepStoreLog;

public class AppInstallationCtrl {


	
//	private PakageListener mpackageListener = null;
//	public void setPackageListener (PakageListener listener){
//		mpackageListener = listener;
//	}
	
	private List<PakageListener> mPackageListeners = null;
	
//	public PakageListener getPakageListener(){
//		return mpackageListener;
//	}
	
	public interface PakageListener{
		public abstract void onpackageAdded(String packageName);
		public abstract void onpackageReplaced(String packageName);
		public abstract void onpackageRemoved(String packageName);
	}

	public List<PakageListener> getPackageListeners(){
		return mPackageListeners;
	}
	
	public AppInstallationCtrl(){
//		mPackageListeners = new ArrayList<AppInstallationCtrl.PakageListener>();
		mPackageListeners = new Vector<AppInstallationCtrl.PakageListener>();
	}

	public void addPackageListener(PakageListener listener){
		mPackageListeners.add(listener);
	}
	
	public void removePackageListener(PakageListener listener){
		mPackageListeners.remove(listener);
	}
	
	public static boolean installApp(Context context, String path) {
		
		try {
			MeepStoreApplication app = (MeepStoreApplication) context.getApplicationContext();
			app.setInstallingApk(path);
			MeepStoreLog.logcatMessage("installApp", "installing app " + path);
			Intent installIntent = new Intent(Intent.ACTION_VIEW);
			
			installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			installIntent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
			installIntent.putExtra("MEEP_INSTALL", true);
			context.startActivity(installIntent);
		} catch (Exception ex) {
			new File(path).deleteOnExit();
			Log.e("run_command", "install-app exception:" + ex.toString());
			return false;
		}
		return true;
	}
	
	
	public static boolean uninstallApp(Context context, String packageName) {
		try {
			Intent intent = new Intent(Intent.ACTION_DELETE, Uri.fromParts("package", packageName, null));
			context.startActivity(intent);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	
	public static String getPackageName(Context context, String filePath) {
		PackageManager pm = context.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);

		if (info != null) {
			ApplicationInfo appInfo = info.applicationInfo;
			return appInfo.packageName;
			// appName = appInfo.name;
		}
		return null;
	}
	
	public static List<ApplicationInfo> getAllIntalledPackageInfos(Context context) {
		PackageManager packageManager = context.getPackageManager();
		return packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
	}
	
	public static boolean isPackageInstalled(Context context, String packageName) {
		try {
			ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, 0);
			return true;
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
	}
	
	public static void removeApk(String packageName){
		MeepStoreLog.logcatMessage("storedownload", "remove apk file:" + packageName);
		String path = "/data/home/app/data/" + packageName + ".apk";
		File file = new File(path);
		file.deleteOnExit();
		
		path = "/data/home/game/data/" + packageName + ".apk";
		file = new File(path);
		file.deleteOnExit();
	}

}
