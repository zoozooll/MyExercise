package com.mogoo.market.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.mogoo.market.model.InstallousAppInfo;
import com.mogoo.market.utils.ScanSdCardUtils;

public class InstallousScanApk {

	 
	private static int INSTALLED = 0;
	private static int UNINSTALLED = 1; 
	private static int INSTALLED_UPDATE =2; 

	private Context mContext;
	private static ScanSdCardUtils instance=null;
	private ArrayList<InstallousAppInfo> myFiles = new ArrayList<InstallousAppInfo>();
	private  boolean isStartScan = false;
//	public static ScanSdCardUtils getInstance(){
//		if(instance==null){
//			instance=new ScanSdCardUtils();
//		}
//		return instance;
//	}

	public List<InstallousAppInfo> getMyFiles() {
		return myFiles;
	}

	public void setMyFiles(ArrayList<InstallousAppInfo> myFiles) {
		this.myFiles = myFiles;
	}

	public InstallousScanApk(Context context) {
		super();
		this.mContext = context;
	}

	public void FindAllAPKFile(File file) {

		if (file.isFile()) {
			if(!isStartScan){
				return;
			}
			String name_s = file.getName();
			InstallousAppInfo myFile;
			String apk_path = null;
			// MimeTypeMap.getSingleton()
			if (name_s.toLowerCase().endsWith(".apk")) {
				Log.d("######","apk name_s="+name_s);
				apk_path = file.getAbsolutePath();
				// System.out.println("----" + file.getAbsolutePath() + "" +
				// name_s);
				PackageManager pm = mContext.getPackageManager();
				PackageInfo packageInfo = pm.getPackageArchiveInfo(apk_path, PackageManager.GET_ACTIVITIES);
				if(packageInfo!=null){
				   ApplicationInfo appInfo = packageInfo.applicationInfo;
				   myFile = new InstallousAppInfo(mContext,packageInfo);
				
				   appInfo.sourceDir = apk_path;
				   appInfo.publicSourceDir = apk_path;
				   Drawable apk_icon = appInfo.loadIcon(pm);
				   myFile.setAppIcon(apk_icon);
				   String appName=null;
				   if(name_s.contains("_")){
				      appName=name_s.subSequence(0, name_s.lastIndexOf("_")).toString();
				   }else{
					  appName=name_s.subSequence(0, name_s.lastIndexOf(".")).toString();
				   }
				   myFile.setAppName(appName);
				
				  String packageName = packageInfo.packageName;
				  myFile.setAppPackageName(packageName);

				  myFile.setAppSavePath(file.getAbsolutePath());

				  String versionName = packageInfo.versionName;
				  myFile.setAppVersionName(versionName);

				  int versionCode = packageInfo.versionCode;
				  myFile.setAppVersionCode(versionCode);
			     
				  int type = doType(pm, packageName, versionCode);
				  myFile.setAppType(type);
				  myFiles.add(myFile);
				}
			}
			// String apk_app = name_s.substring(name_s.lastIndexOf("."));
		} else {
			File[] files = file.listFiles();
//			Log.d("######","files.length="+files.length);
			if (files != null && files.length > 0) {
				for (File file_str : files) {
					FindAllAPKFile(file_str);
				}
			}
		}
	}


	private int doType(PackageManager pm, String packageName, int versionCode) {
		List<PackageInfo> pakageinfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		for (PackageInfo pi : pakageinfos) {
			String pi_packageName = pi.packageName;
			int pi_versionCode = pi.versionCode;
		
			if(packageName.endsWith(pi_packageName)){
		
				if(versionCode==pi_versionCode){
				
					return INSTALLED;
				}else if(versionCode>pi_versionCode){
	
					return INSTALLED_UPDATE;
				}
			}
		}
		return UNINSTALLED;
	}

	public boolean isStartScan() {
		return isStartScan;
	}

	public void setStartScan(boolean isStartScan) {
		this.isStartScan = isStartScan;
	}

}
