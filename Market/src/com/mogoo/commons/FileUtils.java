package com.mogoo.commons;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import com.mogoo.market.MarketApplication;

import android.content.SharedPreferences;
import android.os.StatFs;
import android.util.Log;

public class FileUtils 
{
	private static final String TAG = "FileComponent";
	
	// ********************************文件操作接口***************************//
	/**
	 * 功能：获取sd卡路径
	 *
	 * @return sd卡路径
	 */
	public static String getExternalStoragePath() {
		String state = android.os.Environment.getExternalStorageState();
		if (android.os.Environment.MEDIA_MOUNTED.equals(state)) {
			if (android.os.Environment.getExternalStorageDirectory().canWrite()) {
				return android.os.Environment.getExternalStorageDirectory()
						.getPath();
			} else {
				
					Log.d(TAG,
							"------------lyl---------getExternalStoragePath() :sdcard can't be write");
			}

		}

		return null;
	}

	/**
	 * 功能：获取SD卡剩余容量
	 */
	public static long getAvailableBlocks() {
		String sdcardPath = getExternalStoragePath();
		if (sdcardPath == null) {
			return 0l;
		}

		StatFs statFs = new StatFs(sdcardPath);
		long blocSize = statFs.getBlockSize();
		// 获取BLOCK数量
		// long totalBlocks = statFs.getBlockCount();
		// 可使用的Block的数量
		long availaBlock = statFs.getAvailableBlocks();
		// long total = totalBlocks * blocSize;
		long availableSpare = availaBlock * blocSize;
		return availableSpare;
		// return total;
	}

	/**
	 * 功能：保存文件
	 *
	 * @param path
	 *            文件路径
	 * @param name
	 *            文件名
	 * @return
	 */
	public static void saveFile(String path, String name,String strContent) {
		File filePath = new File(path);
		if (!filePath.exists()) {
			filePath.mkdirs();
		}

		File file = new File(filePath, name);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
			Log.v(TAG, "file.getPath():" + file.getPath());
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file, false);
			fos.write(strContent.getBytes("UTF-8"));
			fos.close();
		} catch (FileNotFoundException e) {
			
				Log.d(TAG, "the file can not be found");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * try{ String path = file.getAbsolutePath(); Log.d(TAG,"PATH=="+path);
		 * Runtime.getRuntime().exec("attrib " +"+h "+"+r "+"+s "+"+a " +path);
		 * }catch(Exception e){ //e.printStackTrace(); Log.d(TAG,"PATH=="); }
		 */
	}

	
	
	/**
	 * 功能：读取文件内容
	 *
	 * @param path
	 *            文件路径
	 * @param name
	 *            文件名
	 * @return
	 */
	public static String readFile(String path, String name) {
		StringBuffer stringBuf = new StringBuffer();

		File file = new File(path, name);
		if (!file.exists()) {
			Log.d(TAG,"file is not exist="+path+name);
			return null;
		}
		InputStream in = null;
		try {
			in = new FileInputStream(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStreamReader reader = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(reader,8*1024);
		String data = null;
		try {
			while ((data = br.readLine()) != null) {
				stringBuf.append(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuf.toString();
	}
	
	
	
	/**
	 * 功能：保存文件
	 *
	 * @param path
	 *            文件路径
	 * @param name
	 *            文件名
	 * @return
	 */
	public static void savePropertiesFile(String path, String name,Properties props) 
	{
		if (props==null)
		{
			return;
		}
		
		File filePath = new File(path);
		if (!filePath.exists()) {
			filePath.mkdirs();
		}

		File file = new File(filePath, name);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
			Log.v(TAG, "file.getPath():" + file.getPath());
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file, false);
			//fos.write(strContent.getBytes("UTF-8"));
			props.store(fos, "");
			fos.close();
		} catch (FileNotFoundException e) {
			
				Log.d(TAG, "the file can not be found");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	/**
	 * 功能：读取文件内容
	 *
	 * @param path
	 *            文件路径
	 * @param name
	 *            文件名
	 * @return
	 */
	public static Properties readPropertiesFile(String path, String name) 
	{


		File file = new File(path, name);
		if (!file.exists()) {
			Log.d(TAG,"file is not exist="+path+name);
			return null;
		}
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			
			Properties props = new Properties();
			props.load(in);
			return props;
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}


	 public static SharedPreferences getSharedPreferencesEx(String name, int mode) 
	 {
		 //return SharedPreferencesEx.getSharedPreferences(name,mode);
		 return MarketApplication.getInstance().getSharedPreferences(name, mode);
	 }
	    
}
