package com.beem.project.btf.utils;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.constant.SettingKey;
import com.beem.project.btf.ui.activity.MySettings.CacheType;
import com.beem.project.btf.ui.entity.TimeCameraImageInfo;
import com.butterfly.vv.db.ormhelper.DBHelper;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.nostra13.universalimageloader.core.ImageLoader;

/** * 本应用数据清除管理器 */
public class DataCleanManager {
	/**
	 * * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * *
	 * @param context
	 */
	public static void cleanInternalCache(Context context) {
		if (context.getCacheDir() != null) {
			deleteFolderFile(context.getCacheDir().getAbsolutePath(), true);
		}
	}
	/**
	 * * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * *
	 * @param context
	 */
	public static void cleanInternalDatabases(Context context) {
		if (context.getCacheDir() != null) {
			deleteFolderFile(new File(context.getCacheDir().getParentFile(),
					"databases").getAbsolutePath(), true);
		}
	}
	/**
	 * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) *
	 * @param context
	 */
	public static void cleanInternalSharedPreference(Context context) {
		if (context.getCacheDir() != null) {
			deleteFolderFile(new File(context.getCacheDir().getParentFile(),
					"shared_prefs").getAbsolutePath(), true);
		}
	}
	/**
	 * * 按名字清除本应用数据库 * *
	 * @param context
	 * @param dbName
	 */
	public static void cleanDatabaseByName(Context context, String dbName) {
		context.deleteDatabase(dbName);
	}
	/**
	 * * 清除/data/data/com.xxx.xxx/files下的内容 * *
	 * @param context
	 */
	public static void cleanInternalFiles(Context context) {
		if (context.getFilesDir() != null) {
			deleteFolderFile(context.getFilesDir().getAbsolutePath(), true);
		}
	}
	/**
	 * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
	 * @param context
	 */
	public static void cleanExternalCache(Context context) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			if (context.getExternalCacheDir() != null) {
				deleteFolderFile(context.getExternalCacheDir()
						.getAbsolutePath(), true);
			}
		}
	}
	/**
	 * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/files)
	 * @param context
	 */
	public static void cleanExternalFiles(Context context) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			if (context.getExternalFilesDir(null) != null) {
				deleteFolderFile(context.getExternalFilesDir(null)
						.getAbsolutePath(), true);
			}
		}
	}
	/**
	 * * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 * *
	 * @param filePath
	 */
	public static void cleanCustomCache(String filePath) {
		deleteFolderFile(filePath, true);
	}
	//清除缓存
	public static void clearCache() {
		long cacheMills = SharedPrefsUtil.getValue(
				BeemApplication.getContext(), SettingKey.cacheMills,
				CacheType._3MonthMills.val);
		long clearNowMills = System.currentTimeMillis();
		long lastClearMills = SharedPrefsUtil.getValue(
				BeemApplication.getContext(), SettingKey.lastClearMills, 0L);
		if (lastClearMills == 0) {
			//如果sp没有此值，则用现在的值，并保存到sp中
			lastClearMills = clearNowMills;
			SharedPrefsUtil.putValue(BeemApplication.getContext(),
					SettingKey.lastClearMills, lastClearMills);
		}
		if (clearNowMills - lastClearMills >= cacheMills) {
			// TODO 清除缓存
			ThreadUtils.executeTask(new Runnable() {
				@Override
				public void run() {
					// 清除数据中表的数据
					DataCleanManager.cleanApplicationData(
							BeemApplication.getContext(), false);
				}
			});
			lastClearMills = clearNowMills;
			// 保存本次清除缓存清理时间
			SharedPrefsUtil.putValue(BeemApplication.getContext(),
					SettingKey.lastClearMills, lastClearMills);
		} else {
			long nextClearTime = -(clearNowMills - lastClearMills - cacheMills);
			StringBuffer sb = new StringBuffer();
			long secondMills = 1000L;
			long minuteMills = secondMills * 60;
			long hourMills = minuteMills * 60;
			long dayMills = hourMills * 24;
			long monthMills = dayMills * 30;
			long yearMills = dayMills * 365;
			if (nextClearTime > yearMills) {
				sb.append(nextClearTime / yearMills).append("years");
				nextClearTime -= yearMills * (nextClearTime / yearMills);
			}
			if (nextClearTime > monthMills) {
				sb.append(nextClearTime / monthMills).append("months");
				nextClearTime -= monthMills * (nextClearTime / monthMills);
			}
			if (nextClearTime > dayMills) {
				sb.append(nextClearTime / dayMills).append("days");
				nextClearTime -= dayMills * (nextClearTime / dayMills);
			}
			if (nextClearTime > hourMills) {
				sb.append(nextClearTime / hourMills).append("hours");
				nextClearTime -= hourMills * (nextClearTime / hourMills);
			}
			if (nextClearTime > minuteMills) {
				sb.append(nextClearTime / minuteMills).append("minutes");
				nextClearTime -= minuteMills * (nextClearTime / minuteMills);
			}
			if (nextClearTime > secondMills) {
				sb.append(nextClearTime / secondMills).append("seconds");
				nextClearTime -= secondMills * (nextClearTime / secondMills);
			}
			//LogUtils.v("clear cache will do in " + sb.toString());
		}
	}
	/**
	 * @Title: cleanApplicationData
	 * @Description: 清除本应用所有的数据
	 * @param: @param context
	 * @param: @param isClearNow
	 * @param: @return
	 * @return: boolean 是否马上清除：是-立即清除 否-按照设定的时间清除
	 * @throws:
	 */
	public static boolean cleanApplicationData(Context context,
			boolean isClearNow) {
		long cacheMills = isClearNow ? 0 : SharedPrefsUtil.getValue(context,
				SettingKey.cacheMills, CacheType._3MonthMills.val);
		try {
			// 清除数据库
			DBHelper.getInstance().clearCache(cacheMills);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			List<String> materialPaths = getIgnoreCacheClearList();
			// 清除缓存路径图片
			File imageLoaderDir = ImageLoader.getInstance().getDiskCache()
					.getDirectory();
			if (imageLoaderDir != null && imageLoaderDir.isDirectory()) {
				for (File fileOne : imageLoaderDir.listFiles()) {
					if (fileOne.isFile()) {
						if (!materialPaths.contains(fileOne.getPath())
								&& fileOne.lastModified() + cacheMills <= System
										.currentTimeMillis()) {
							// //LogUtils.i("delete the outdateImage:" + fileOne.getPath());
							fileOne.delete();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	// 获取清除缓存忽略的路径
	private static List<String> getIgnoreCacheClearList() {
		List<TimeCameraImageInfo> materialList = TimeCameraImageInfo.queryAll();
		List<String> retVal = new ArrayList<String>();
		DBKey[] keys = new DBKey[] { DBKey.laypath1, DBKey.laypath2,
				DBKey.laypath3, DBKey.pathThumb, DBKey.pathThumbLarge,
				DBKey.posepath };
		if (materialList != null) {
			for (TimeCameraImageInfo info : materialList) {
				for (DBKey key : keys) {
					File file = ImageLoader.getInstance().getDiskCache()
							.get(info.getFieldStr(key));
					if (file != null) {
						retVal.add(file.getPath());
					}
				}
			}
		}
		return retVal;
	}
	protected static void cleanExternalLogs(Context context) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File external = new File(Environment.getExternalStorageDirectory()
					+ "/Android/data/" + context.getPackageName() + "/log/");
			deleteFolderFile(external.getPath(), true);
		}
	}
	/**
	 * @Title: getApplicationCacheSize
	 * @Description: 获取应用缓存
	 * @param context
	 * @return
	 * @return: String
	 */
	public static String getApplicationCacheSize(Context context) {
		long totalSize = 0;
		// 图片缓存的大小
		File cacheDir = ImageLoader.getInstance().getDiskCache().getDirectory();
		List<String> ignoreFilePaths = getIgnoreCacheClearList();
		if (cacheDir != null && cacheDir.isDirectory()) {
			for (File file : cacheDir.listFiles()) {
				if (!ignoreFilePaths.contains(file.getPath()) && file.isFile()) {
					totalSize += file.length();
				}
			}
		}
		return getFormatSize(totalSize);
	}
	// 获取文件
	// Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/
	// 目录，一般放一些长时间保存的数据
	// Context.getExternalCacheDir() -->
	// SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
	public static long getFolderSize(File file) {
		if (file == null) {
			//LogUtils.e("file is null~", 3);
			return 0;
		}
		long size = 0;
		if (file.isDirectory()) {
			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				size += getFolderSize(fileList[i]);
			}
		} else {
			size += file.length();
		}
		return size;
	}
	/**
	 * 删除指定目录下文件及目录
	 * @param deleteThisPath
	 * @param filepath
	 * @return
	 */
	public static boolean deleteFolderFile(String filePath,
			boolean deleteThisPath) {
		if (!TextUtils.isEmpty(filePath)) {
			try {
				File file = new File(filePath);
				if (!file.exists()) {
					return false;
				}
				if (file.isDirectory()) {// 如果下面还有文件
					File files[] = file.listFiles();
					for (int i = 0; i < files.length; i++) {
						deleteFolderFile(files[i].getAbsolutePath(), true);
					}
				}
				if (deleteThisPath) {
					if (!file.isDirectory()) {// 如果是文件，删除
						file.delete();
					} else {// 目录
						if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
							file.delete();
						}
					}
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}
	/**
	 * 格式化单位
	 * @param size
	 * @return
	 */
	public static String getFormatSize(double size) {
		double kiloByte = size / 1024;
		if (kiloByte < 1) {
			return size + "B";
		}
		double megaByte = kiloByte / 1024;
		if (megaByte < 1) {
			BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
			return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "KB";
		}
		double gigaByte = megaByte / 1024;
		if (gigaByte < 1) {
			BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
			return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "MB";
		}
		double teraBytes = gigaByte / 1024;
		if (teraBytes < 1) {
			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
			return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "GB";
		}
		BigDecimal result4 = new BigDecimal(teraBytes);
		return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
				+ "TB";
	}
	public static String getCacheSize(File file) throws Exception {
		return getFormatSize(getFolderSize(file));
	}
}
