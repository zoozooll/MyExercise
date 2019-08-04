package com.butterfly.vv.camera.base;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.media.ExifInterface;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;

import com.butterfly.vv.camera.MyDateFormat;
import com.butterfly.vv.camera.Utils;

public class GetImageFileUtil {
	public static String TAG = "GetImageFileUtil";
	public static final String BASE_PHOTO_PATH = "/"
			+ Environment.DIRECTORY_DCIM;
	public static final String IGNORE_FOLDER = ".thumbnails";
	private static final String[] mImageFormatSet = new String[] { ".jpg",
			".png", ".gif" };

	public static List<ImageInfoHolder> getAllImageInfo(Context context) {
		return getImageFiles(context, 0);
	}
	public static List<ImageInfoHolder> getAllImageInfoExceptSmall(
			Context context, int smallSize) {
		return getImageFiles(context, smallSize);
	}
	// 遍历指定的路径
	public static List<ImageInfoHolder> getImageFiles(Context context,
			int smallSize) {
		// 通过ContentResolver查询所有图片信息
		List<ImageInfoHolder> result = null;
		Cursor cursor = context.getContentResolver().query(
				Media.EXTERNAL_CONTENT_URI,
				null,
				"(" + MediaColumns.MIME_TYPE + "=? or "
						+ MediaColumns.MIME_TYPE + "=? )",
				new String[] { "image/jpeg", "image/png" },
				MediaColumns.DATE_ADDED + " desc ");
		if (cursor != null) {
			result = new ArrayList<ImageInfoHolder>();
			while (cursor.moveToNext()) {
				// 获取图片绝对路径
				String path = cursor.getString(cursor
						.getColumnIndex(MediaColumns.DATA));
				// MediaColumns's timestamp unit is in seconds but not million seconds
				long filelong = cursor.getLong(cursor
						.getColumnIndex(MediaColumns.DATE_ADDED)) * 1000L;
				String thumbImage = cursor.getString(cursor
						.getColumnIndex("mini_thumb_magic"));
				if (path == null) {
					continue;
				}
				/**
				 * 获取每张图片的大小
				 */
				int imagesizecount = cursor.getInt(cursor
						.getColumnIndex(MediaColumns.SIZE));
				// long modifiedTime = Long.valueOf(data_modified);
				// 获取图片的路径
				// 根据父路径名将图片放入到mGruopMap中
				ImageInfoHolder holder = new ImageInfoHolder();
				holder.mImageModifiedTime = MyDateFormat.getDateByTimeMillis(
						filelong, "yyyy:MM:dd");
				holder.mImagePath = path;
				holder.mImageSize = Utils.formatDataSize(imagesizecount);
				holder.mImageSizeKB = (int) Utils
						.formatDataSizeInKB(imagesizecount);
				// getImageExif(holder);
				if (holder.mImageDateTime == null) {
					// 这个值要作为排序比较用，不能为空
					holder.mImageDateTime = "00";
					holder.mLatestTime = holder.mImageModifiedTime;
				} /*
					* else if (MyDateFormat.isDateLatest(holder.mImageDateTime,
					* holder.mImageModifiedTime)) { holder.mLatestTime = holder.mImageDateTime; }
					* else { holder.mLatestTime = holder.mImageModifiedTime; }
					*/
				result.add(holder); // 将文件的路径添加到list集合中
			}
		}
		return result;
	}
	// 判断是否为图片文件
	public static boolean isImageFile(String path) {
		for (String format : mImageFormatSet) { // 遍历数组
			if (path.endsWith(format)) {
				return true;
			}
		}
		return false;
	}
	public static void getImageExif(ImageInfoHolder holder) {
		long begin = SystemClock.elapsedRealtime();
		ExifInterface exif = null;
		// Log.d(TAG,"mImagePath = " + holder.mImagePath);
		try {
			exif = new ExifInterface(holder.mImagePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Log.d(TAG,"exif is null?  " + (exif == null));
		if (exif != null) {
			holder.mImageDateTime = exif
					.getAttribute(ExifInterface.TAG_DATETIME);
			holder.mImageDeviceModel = exif
					.getAttribute(ExifInterface.TAG_MODEL);
			holder.mImageWidth = exif
					.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
			holder.mImageHeight = exif
					.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
			holder.mExposureTime = null;// exif.getAttribute(ExifInterface.TAG_EXPOSURE_TIME);
			holder.mPhotoAperture = null;// exif.getAttribute(ExifInterface.TAG_APERTURE);
			holder.mPhotoFlash = exif.getAttribute(ExifInterface.TAG_FLASH);
			holder.mFocalLength = exif
					.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
			holder.mPhotoIso = null;// exif.getAttribute(ExifInterface.TAG_ISO);
			holder.mDeviceMake = exif.getAttribute(ExifInterface.TAG_MAKE);
			holder.mWhiteBalance = exif
					.getAttribute(ExifInterface.TAG_WHITE_BALANCE);
			holder.mLongitude = exif
					.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
			holder.mLatitude = exif
					.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
			/*
			 * if (mContext != null && holder.mLatitude != null && holder.mLongitude != null) { if
			 * (mLocUtil == null) { mLocUtil = new LocationUtil(mContext); } holder.mPhotoCity =
			 * mLocUtil.getLocationCityByNormalLatLon(holder.mLatitude, holder.mLongitude); }
			 */
		} else {
			// 这个值要作为排序比较用，不能为空
			holder.mLatestTime = "00";
		}
		Log.d(TAG, "getImageExif " + (SystemClock.elapsedRealtime() - begin));
	}
	private static long getImageFileSizeInByte(String imgPath) {
		File f1 = new File(imgPath);
		long length = 0;
		if (f1 != null && !f1.isDirectory()) {
			length = f1.length();
		}
		/*
		 * InputStream iStream = null; try { iStream = new FileInputStream(new File(imgPath)); }
		 * catch (FileNotFoundException e) { e.printStackTrace(); } int size = 0; try { size =
		 * iStream.available(); } catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		return length;
	}
	public static long getImageFileSizeInKB(String imgPath) {
		File f1 = new File(imgPath);
		long length = 0;
		long fileSizeKB = 0;
		if (f1 != null && f1.isDirectory() == false) {
			length = f1.length();
		}
		fileSizeKB = Utils.formatDataSizeInKB(length);
		return fileSizeKB;
	}
	private static String getFolderNameFromPath(String path) {
		String pathTemp = null;
		if (path.charAt(path.length() - 1) == '/') {
			pathTemp = path.substring(0, path.length() - 1);
		} else {
			pathTemp = path;
		}
		int index1 = pathTemp.lastIndexOf("/");
		return pathTemp.substring(index1 + 1);
	}
	/*
	 * public static String getSdCardPath() { String [] rootPath = {"/mnt/", "/mnt/storage/",
	 * "/mnt/storage/emulated/"}; String sdPath = null; boolean isSdCardExist =
	 * Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); Log.d(TAG,
	 * "getSdCardPath, isSdCardExist = " + isSdCardExist); if (isSdCardExist) { sdPath =
	 * Environment.getExternalStorageDirectory().getAbsolutePath(); } else { boolean isFound =
	 * false; String sdPath2, tempPath; for (int count = 0; count < rootPath.length; count++) { File
	 * files = new File(rootPath[count]); File[] fileArray = files.listFiles(); Log.d(TAG,
	 * "getSdCardPath, fileArray.length = " + fileArray.length); if (fileArray.length > 0) { for
	 * (int i = 0; i < fileArray.length; i++) { sdPath2 = fileArray[i].getAbsolutePath(); tempPath =
	 * sdPath2 + BASE_PHOTO_PATH; File tempFile = new File(tempPath); if (tempFile.exists()) {
	 * sdPath = sdPath2; isFound = true; break; } } } if (isFound) { break; } } } Log.d(TAG,
	 * "getSdCardPath, sdPath = " + sdPath); return sdPath; }
	 */
	public static boolean isExternalStorageExist() {
		boolean tempExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		return tempExist;
	}
	public static String getPublicDcimPach() {
		String photoPath = null;
		if (isExternalStorageExist()) {
			photoPath = Environment.getExternalStoragePublicDirectory(
					Environment.DIRECTORY_DCIM).getAbsolutePath();
		} else {
			photoPath = getExternalSdCardPath() + BASE_PHOTO_PATH;
		}
		Log.d(TAG, "getPublicDcimPach, photoPath = " + photoPath);
		return photoPath;
	}
	public static String getPublicExternalSdCardPach() {
		String sdPath = null;
		if (isExternalStorageExist()) {
			sdPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
		} else {
			sdPath = getExternalSdCardPath();
		}
		Log.d(TAG, "getPublicExternalSdCardPach, sdPath = " + sdPath);
		return sdPath;
	}
	/**
	 * 遍历 "system/etc/vold.fstab” 文件，获取全部的Android的挂载点信息
	 * @return
	 */
	public static ArrayList<String> getDevMountList() {
		String mountStr = Utils.readFile("/etc/vold.fstab");
		Log.d(TAG, "getDevMountList, mountStr = " + mountStr);
		String[] toSearch = mountStr.split(" ");
		ArrayList<String> out = new ArrayList<String>();
		for (int i = 0; i < toSearch.length; i++) {
			if (toSearch[i].contains("dev_mount")) {
				if (new File(toSearch[i + 2]).exists()) {
					out.add(toSearch[i + 2]);
				}
			}
		}
		return out;
	}
	/**
	 * 获取扩展SD卡存储目录 如果有外接的SD卡，并且已挂载，则返回这个外置SD卡目录 否则：返回内置SD卡目录
	 * @return
	 */
	public static String getExternalSdCardPath() {
		boolean isSdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		Log.d(TAG, "getExternalSdCardPath, isSdCardExist = " + isSdCardExist);
		if (isSdCardExist) {
			File sdCardFile = new File(Environment
					.getExternalStorageDirectory().getAbsolutePath());
			return sdCardFile.getAbsolutePath();
		}
		String path = null;
		File sdCardFile = null;
		ArrayList<String> devMountList = getDevMountList();
		for (String devMount : devMountList) {
			Log.d(TAG, "getExternalSdCardPath, devMount = " + devMount);
			File file = new File(devMount);
			if (file.isDirectory() && file.canWrite()) {
				path = file.getAbsolutePath();
				String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss")
						.format(new Date());
				File testWritable = new File(path, "test_" + timeStamp);
				if (testWritable.mkdirs()) {
					testWritable.delete();
				} else {
					path = null;
				}
			}
		}
		if (path != null) {
			sdCardFile = new File(path);
			String sdCardPath = sdCardFile.getAbsolutePath();
			Log.d(TAG, "getExternalSdCardPath, sdCardPath = " + sdCardPath);
			return sdCardPath;
		}
		return null;
	}
}
