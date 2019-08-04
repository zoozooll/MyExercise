package com.beem.project.btf.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.util.EncodingUtils;

import com.beem.project.btf.BeemApplication;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

/**
 * @author le yang 自己归纳的关于文件操作的一系列方法
 */
public class FileUtil {
	/** 从Raw目录中读取文件 参数写法为R.raw.test */
	public static String getFileForRaw(int resid) {
		String res = "";
		try {
			//得到资源中的Raw数据流  
			InputStream in = BeemApplication.getContext().getResources()
					.openRawResource(resid);
			//得到数据的大小  
			int length = in.available();
			byte[] buffer = new byte[length];
			//读取数据  
			in.read(buffer);
			//依test.txt的编码类型选择合适的编码，如果不调整会乱码   
			res = EncodingUtils.getString(buffer, "BIG5");
			//关闭      
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	/** ------------------------------assets文件相关方法#start------------------------------ */
	/** 从Assets目录中读取文件 参数写法为test.txt */
	public static String getFileForAssets(Context context, String fileName) {
		String res = "";
		try {
			//得到资源中的asset数据流  
			InputStream in = context.getResources().getAssets().open(fileName);
			int length = in.available();
			byte[] buffer = new byte[length];
			in.read(buffer);
			in.close();
			res = EncodingUtils.getString(buffer, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	/**
	 * @param context,src(assets目录下的文件夹名),sdcard输出路径
	 * @return Boolean
	 */
	public static Boolean CopyAssetsDir(Context context, String src, String des) {
		Boolean isSuccess = true;
		String[] files;
		try {
			files = context.getResources().getAssets().list(src);
		} catch (IOException e1) {
			return false;
		}
		if (files.length == 0) {
			//如果是文件
			isSuccess = CopyAssetsFile(context, src, des);
			if (!isSuccess)
				return isSuccess;
		} else {
			File srcfile = new File(des + "/" + src);
			if (!srcfile.exists()) {
				srcfile.mkdir();
			}
			for (int i = 0; i < files.length; i++) {
				isSuccess = CopyAssetsDir(context, src + "/" + files[i], des);
				if (!isSuccess)
					return isSuccess;
			}
		}
		return isSuccess;
	}
	/**
	 * @param context,src(assets目录下的文件名),sdcard输出路径
	 * @return Boolean
	 */
	private static Boolean CopyAssetsFile(Context context, String filename,
			String des) {
		Boolean isSuccess = true;
		AssetManager assetManager = context.getAssets();
		InputStream in = null;
		OutputStream out = null;
		try {
			in = assetManager.open(filename);
			String newFileName = des + "/" + filename;
			out = new FileOutputStream(newFileName);
			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			in.close();
			in = null;
			out.flush();
			out.close();
			out = null;
		} catch (Exception e) {
			e.printStackTrace();
			isSuccess = false;
		}
		return isSuccess;
	}
	/** ------------------------------assets文件相关方法#end------------------------------ */
	/** 写入文件到sd卡里 */
	public static void writeFileSdcardFile(String fileName, String write_str)
			throws IOException {
		try {
			FileOutputStream fout = new FileOutputStream(fileName);
			byte[] bytes = write_str.getBytes();
			fout.write(bytes);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/** 从sd卡里读文件 */
	public static String readFileSdcardFile(String fileName) throws IOException {
		String res = "";
		try {
			FileInputStream fin = new FileInputStream(fileName);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	/**
	 * 判断SDCard是否可用
	 * @return
	 */
	public static boolean isSDCardEnable() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}
	/**
	 * 获取 获取扩展SD卡设备状态
	 */
	public static String getExternalStorageState() {
		return Environment.getExternalStorageState();
	}
	/**
	 * 获取SD卡的剩余容量 单位byte
	 * @return
	 */
	public static long getSDCardAllSize() {
		if (isSDCardEnable()) {
			StatFs stat = new StatFs(getSDCardPath());
			// 获取空闲的数据块的数
			long availableBlocks = (long) stat.getAvailableBlocks() - 4;
			// 获取单个数据块的大小
			long freeBlocks = stat.getAvailableBlocks();
			return freeBlocks * availableBlocks;
		}
		return 0;
	}
	/**
	 * 获取指定路径空间的剩余可用容量字节数，单位byte
	 * @param filePath
	 * @return 容量字节 SDCard可用空间，内部存储可用空间
	 */
	public static long getFreeBytes(String filePath) {
		// 如果是sd卡的下的路径，则获取sd卡可用容量
		if (filePath.startsWith(getSDCardPath())) {
			filePath = getSDCardPath();
		} else {// 如果是内部存储的路径，则获取内存存储的可用容量
			filePath = Environment.getDataDirectory().getAbsolutePath();
		}
		StatFs stat = new StatFs(filePath);
		long availableBlocks = (long) stat.getAvailableBlocks() - 4;
		return stat.getBlockSize() * availableBlocks;
	}
	/** -----------------------------------获取各种储存路径---------------------------- */
	/**
	 * 获取SD卡路径
	 * @return 示例:/storage/sdcard0/
	 */
	public static String getSDCardPath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator;
	}
	/**
	 * 获取系统存储路径(根目录)
	 * @return 示例:/system
	 */
	public static String getRootDirectoryPath() {
		return Environment.getRootDirectory().getAbsolutePath();
	}
	/**
	 * 获取应用所在存储目录
	 * @return 示例:/data/data/com.example.logtest/files
	 */
	public static String getAppPath(Context context) {
		return context.getFilesDir().getAbsolutePath();
	}
	/**
	 * 获取应用私有存储路径,随应用卸载而删除
	 * @return 示例:
	 */
	public static String getExternalFilesDir(Context context) {
		return context.getExternalFilesDir(null).getAbsolutePath();
	}
	/** --------------------------以下为file的创建删除等操作-------------------------------------- */
	/**
	 * 在SD卡上创建目录
	 * @param dir 目录路径
	 * @return 目录绝对路径
	 */
	public static String creatSDDir(String dir) {
		if (!isSDCardEnable()) {
			return null;
		}
		File dirFile = new File(getSDCardPath() + dir + File.separator);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		return dirFile.getAbsolutePath();
	}
	/**
	 * 在SD卡上创建文件
	 * @param dir 目录路径
	 * @param fileName
	 * @return 文件绝对路径
	 * @throws IOException
	 */
	public static String createFileInSDCard(String dir, String fileName)
			throws IOException {
		if (!isSDCardEnable()) {
			return null;
		}
		//在创建文件前先创建文件目录
		creatSDDir(dir);
		File file = new File(getSDCardPath() + dir + File.separator + fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		return file.getAbsolutePath();
	}
	public static Uri getCameraPhotoUri(Context c) {
		if (isSDCardEnable()) {
			return Uri.fromFile(new File(c.getExternalCacheDir(),
					"cameraTemp.jpg"));
		} else {
			return Uri.fromFile(new File(c.getCacheDir(), "cameraTemp.jpg"));
		}
	}
	public static Uri getCropTempPhotoUri(Context c) {
		if (isSDCardEnable()) {
			return Uri.fromFile(new File(c.getExternalCacheDir(),
					"cropTemp.jpg"));
		} else {
			return Uri.fromFile(new File(c.getCacheDir(), "cropTemp.jpg"));
		}
	}
	public static File getHttpDebugFolder() {
		File folder = new File(Environment.getExternalStorageDirectory(), "httpTemp");
		if (!folder.exists()) {
			folder.mkdirs();
		}
		return folder;
	}
	/**
	 * 删除文件
	 */
	public static boolean deleteFile(String path) {
		if (TextUtils.isEmpty(path)) {
			return true;
		}
		File file = new File(path);
		if (!file.exists()) {
			return true;
		}
		if (file.isFile()) {
			return file.delete();
		}
		if (!file.isDirectory()) {
			return false;
		}
		for (File f : file.listFiles()) {
			if (f.isFile()) {
				f.delete();
			} else if (f.isDirectory()) {
				deleteFile(f.getAbsolutePath());
			}
		}
		return file.delete();
	}
}
