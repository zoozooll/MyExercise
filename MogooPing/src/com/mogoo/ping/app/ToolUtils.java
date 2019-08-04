package com.mogoo.ping.app;

import java.io.File;

import com.mogoo.ping.R;
import com.mogoo.ping.model.ApksDao;
import com.mogoo.ping.model.DataBaseConfig;
import com.mogoo.ping.model.DataBaseConfig.ApkListTable;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

public class ToolUtils {
	private static final String DOWNLOAD_INFO = "download";
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
	
	public static String getSDKpath() {
		String sdcardStr = "";
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			sdcardStr = Environment.getExternalStorageDirectory() + "";
		} else {
		}
		return sdcardStr;
	}
	
	public static String getFileAbsolutePath(String savePath, String fileName) {
		String path = getSDKpath()+"/"+savePath+"/"+ fileName;
		return path;
	}
	
	public static void installApk(Context context, String apkPath) {
		Intent intentInstall = new Intent(Intent.ACTION_VIEW);
		intentInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intentInstall.setDataAndType(Uri.fromFile(new File(apkPath)),
				"application/vnd.android.package-archive");
		context.startActivity(intentInstall);
	}
	
	public static String getFileNameWithVersion(String appName, String version){
		String fileName = appName+"_"+version+".apk";
		return fileName.replace(" ", "");
	}
	
	public static SharedPreferences getPreference(Context context){
		return context.getSharedPreferences(DOWNLOAD_INFO, Context.MODE_PRIVATE);
	}
	public static void saveDownloadFileName(Context context, String fileName){
		SharedPreferences preference = getPreference(context);
		Editor edit = preference.edit();
		edit.putBoolean(fileName, true);
		edit.commit();
	}
	public static void removeDownloadFileName(Context context, String fileName){
		SharedPreferences preference = getPreference(context);
		Editor edit = preference.edit();
		edit.remove(fileName);
		edit.commit();
	}
	public static void removeAllDownloadFile(Context context) {
		SharedPreferences preference = getPreference(context);
		Editor editor = preference.edit();
		editor.clear();
		editor.commit();
	}
	public static boolean getDownloadFileName(Context context, String fileName, boolean defaultValue){
		SharedPreferences preference = getPreference(context);
		return preference.getBoolean(fileName, defaultValue);
	}
	
	/**
	 * byte to String
	 * 
	 * @param bytes 
	 * @return
	 */
	public static String convertByteToString(String bytes) {
		if (bytes == null || "".equals(bytes.trim())) {
			return "";
		}
		String[] strArray = bytes.split(",");
		int length = strArray.length;
		int[] intData = new int[length];
		byte[] byteData = new byte[length];
		for (int i = 0; i < length; i++) {
			intData[i] = Integer.parseInt(strArray[i]);
			byteData[i] = (byte) intData[i];
		}

		String strResult = new String(byteData);
		return strResult;
	}
	
	/**
	 * 
	 *  */
	public static int getNotifyId(String downUrl) {
		String str = downUrl;
		int notifyId = 0;
		if(str != null && str != ""){
			int start = str.lastIndexOf("ct");
			int end = str.lastIndexOf("&");
			String son_str = str.substring(start+3, end);
			try {
				int start_son = son_str.indexOf("_");
				notifyId = Integer.parseInt(son_str.substring(start_son+1));
			} catch (NumberFormatException e){
				System.out.println("when convert the String to int occur an exception, so use the default id");
				e.printStackTrace();
				notifyId = (int)System.currentTimeMillis();
			}
		}
		return notifyId;
	}
	
	/**
	 * @Author lizuokang
	 * TODO
	 * @param table
	 * @return
	 * @Date 上午10:23:16  2012-10-15
	 */
	static String getTheTypeTableName(int apkType) {
		String table = null;
		switch (apkType) {
		case R.string.apptype_showstring_appslasted:
			table = DataBaseConfig.ApplicationsLastedTable.TABLE_NAME;
			break;
		case R.string.apptype_showstring_appsrecommend:
			table = DataBaseConfig.ApplicationsRecomendTable.TABLE_NAME;
			break;
		case R.string.apptype_showstring_gameslasted:
			table = DataBaseConfig.GamesLastedTable.TABLE_NAME;
			break;
		case R.string.apptype_showstring_gamesrecomend:
			table = DataBaseConfig.GamesRecomendTable.TABLE_NAME;
			break;

		default:
			break;
		}
		return table;
	}
	
	static int getDownloadState(Context context,String apkId, String table) {
		int flag = 0;
		long downloadId = 0;
		Cursor cursor1 = ApksDao.getInstance(context).querySingleItems(table, new String[]{ApkListTable.COLUMN_APK_DOWNLOADID}, DataBaseConfig.ApkListTable.COLUMN_ID + " = ? ", new String[]{apkId});
		cursor1.moveToFirst();
		if (!cursor1.isAfterLast()) {
			downloadId = cursor1.getLong(0);
		}
		cursor1.close();
		DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
		Cursor cursor = dm.query(query);
		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			flag = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
		}
		cursor.close();
		return flag;
	}
	
	
}
