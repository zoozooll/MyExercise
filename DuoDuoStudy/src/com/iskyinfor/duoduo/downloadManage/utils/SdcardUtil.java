package com.iskyinfor.duoduo.downloadManage.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.iskyinfor.duoduo.StaticData;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class SdcardUtil {
	/**
	 * 根据文件路径创建文件目录
	 */
	public static void mkdirByPath(String filePath) {
		File saveDir = new File(filePath);
		if (!saveDir.exists() && !saveDir.isDirectory()) {
			saveDir.mkdirs();
		}
	}

	/**
	 * 重命名文件为相同目录下的指定名称文件
	 */
	public static final File renameFile(File file, String name) {
		File apkFile = new File(file.getParentFile() + File.separator + name);
		file.renameTo(apkFile);
		return apkFile;
	}

	/**
	 * 清理文件,存在且不是目录即删除
	 */
	public static void deleteFile(File file) {
		if (file != null && file.exists() && !file.isDirectory()) {
			file.delete();
		}
	}

	/**
	 * 检查Sdcard是否已挂载.
	 * 
	 * @return
	 */
	public static boolean checkSdcardExist() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state) ? true : false;
	}

	/**
	 * 判断Sdcard是否可用,Shared状态为 USB共享状态,不支持SD卡的读写.
	 * 
	 * @return
	 */
	public static boolean checkSdcardUsed() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_SHARED.equals(state) ? true : false;
	}

	/**
	 * 判断是否有足够的剩余空间
	 * 
	 * @param saveDir
	 * @param totalSize
	 * @return
	 */
	public static boolean isAvailableBlocks(File saveDir, Long totalSize) {
		StatFs statFs = new StatFs(saveDir.getPath());
		long blockSize = statFs.getBlockSize();
		long availableBlocks = statFs.getAvailableBlocks();
		if (availableBlocks * blockSize >= totalSize) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 判断是否本地加载图片 true就为本地加载,false就需要联网加载  
	 * @param file 
	 * @return
	 */
	public static boolean isNativeLoad(File dir ,String file) {
		String [] filesName = dir.list();
		if (filesName == null || filesName.length == 0 || file == null )
		{
			return false;
		} 
		else 
		{
			// SDCard 没有找到图片 。需要联网加载图片
			for (String string : filesName) {
				//在本地找到图片了
				if (string.contains(file))
				{
					return true;
				}
		}
			return false;
		}
	}
	
	public static Bitmap nativeLoad(File dir , String filename) 
	{
		FileInputStream fis = null;
		Bitmap bitmap = null;
		dir = new File(dir.getAbsolutePath() + File.separator + filename);
		
		try 
		{
			fis = new FileInputStream(dir);
			bitmap = BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		return bitmap;
		
	}
	public static Bitmap nativeLoad(String fileName) 
	{
		FileInputStream fis = null;
		Bitmap bitmap = null;
		
	File file=new File(StaticData.IMAGE_DOWNLOAD_ADDR+File.separator + fileName);
	Log.i("liu", "file.path===:"+file.getPath());
	if(file.exists()){
		try 
		{
			fis =new FileInputStream(file) ;
			bitmap = BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
		return bitmap;
		
	}

	
}
