package com.mogoo.market.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.mogoo.market.model.SoftItem;

public class MogooLocalSoftManager {
	public static final String TAG = "MogooLocalSoftManager";
	private static MogooLocalSoftManager mMoSManager;
	private Context mContext;
	/** 内置软件包名列表 */
	private ArrayList<String> mNetList;
	/** 第三方软件列表,即已安装软件列表 */
	private static ArrayList<SoftItem> mLocalSoftList = new ArrayList<SoftItem>();
	/** 本地已安装应用的包名 */
	private static ArrayList<String> mLocalPackage = new ArrayList<String>();
	/** 第三方软件列表映射,即已安装软件列表映射，以包名映射 */
	private HashMap<String, SoftItem> mLocalSoftMap = new HashMap<String, SoftItem>();;
	/** 系统所有安装软件 */
	private List<PackageInfo> mPacks;
	/** 包管理器 */
	private PackageManager mPm;

	private MogooLocalSoftManager(Context context) {
		mContext = context;
		mPm = mContext.getPackageManager();
		mPacks = mPm.getInstalledPackages(0);
		initSoft();
	}

	public static MogooLocalSoftManager getInstance(Context context) {
		if (mMoSManager == null)
			return new MogooLocalSoftManager(context);
		return mMoSManager;

	}

	public static MogooLocalSoftManager init(Context context) {
		return getInstance(context);
	}

	private void initSoft() {
		for (PackageInfo pInfo : mPacks) {
			ApplicationInfo appInfo = pInfo.applicationInfo;
			if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				// 第3方应用
				addLocalSoft(appInfo, pInfo.versionCode);
			} else {
				// 系统程序
				// addLocalList(appInfo, p.versionCode, p.versionName);
			}
		}
//		for (String a : mLocalPackage) {
//			System.out.println("lcq:init:" + a);
//		}
	}

	/**
	 * add loal soft to the list
	 * 
	 * @param appInfo
	 * @param versionCode
	 */
	private void addLocalSoft(ApplicationInfo appInfo, int versionCode) {
		// 过滤 自身
		if (mContext.getPackageName().equals(appInfo.packageName)) {
			return;
		}
		// OK
		SoftItem item = new SoftItem();
		item.app_name = appInfo.loadLabel(mPm).toString();
		item.package_name = appInfo.packageName;
		item.versionCode = versionCode;
		item.softFlag = SoftItem.FLAG_INSTALL;

		mLocalSoftList.add(item);
		mLocalPackage.add(appInfo.packageName);
		mLocalSoftMap.put(appInfo.packageName, item);
	}

	private void removeLocalSoft(String pkgName) {
		if (mLocalSoftList != null && mLocalPackage != null) {
			SoftItem item = mLocalSoftMap.get(pkgName);
			//System.out.println("lcq:before remove:" + mLocalPackage.size());
			if (item != null) {
				mLocalSoftMap.remove(item);
				mLocalSoftList.remove(item);
				mLocalPackage.remove(pkgName);
			}
//			for (String a : mLocalPackage) {
//				System.out.println("lcq:" + a);
//			}
//			System.out.println("lcq:after remove:" + mLocalPackage.size());

		}
	}

	public boolean isInstalled(String packageName) {
		boolean result = false;
		if (mLocalSoftList != null) {
			result = mLocalPackage.contains(packageName);
		}
		return result;
	}

	/**
	 * 
	 * @param pkgName
	 * @return
	 */
	public boolean addToInstalled(String pkgName) {
		boolean result = false;
		if (!mLocalPackage.contains(pkgName)) {// 如果包名不存在
			PackageInfo info;
			try {
				info = mPm.getPackageInfo(pkgName, 0);
				addLocalSoft(info.applicationInfo, info.versionCode);
				result = true;
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				Log.e(TAG,
						"when get the packageInfo in the method addToInstalled(MogooLocalSoftManager) , the error occured,");
				e.printStackTrace();
			}
		}
//		for (String pkg : mLocalPackage) {
//			System.out.println("lcq:add:" + pkg);
//		}
//		System.out.println("lcq:add:" + result);
		return result;
	}

	/**
	 * 软件卸载后删除mLocalSoftList和mLocalPackage中的对应项
	 * 
	 * @param pkgName
	 *            包名
	 * @return 删除功能返回true,否则返回false
	 */
	public boolean removeInstalled(String pkgName) {
		boolean result = false;
		if (mLocalPackage.contains(pkgName)) {// 如果包含已安装的包名
			removeLocalSoft(pkgName);
			result = true;
		}
		// for(String pkg : mLocalPackage) {
		// System.out.println("lcq:remove:"+pkg);
		// }
		// System.out.println("lcq:remove:"+result);
		return result;
	}

	public ArrayList<SoftItem> getLocalSoft() {
		return mLocalSoftList;
	}

	public ArrayList<String> getLocalPackage() {
		return mLocalPackage;
	}

	public HashMap<String, SoftItem> getLocalSoftMap() {
		return mLocalSoftMap;
	}

	public void destory() {
		mLocalSoftList.clear();
		mLocalSoftMap.clear();
		mPacks.clear();
	}

}
