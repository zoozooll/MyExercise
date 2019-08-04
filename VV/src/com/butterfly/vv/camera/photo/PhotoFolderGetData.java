package com.butterfly.vv.camera.photo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.MediaColumns;

import com.butterfly.vv.camera.Utils;
import com.butterfly.vv.camera.base.ImageFolderInfo;

public class PhotoFolderGetData {
	private static final String TAG = "PhotoFolderGetData";
	private HashMap<String, List<String>> mGroupMapFolderInfo = new HashMap<String, List<String>>();
	private List<ImageFolderInfo> listfolder = new ArrayList<ImageFolderInfo>();
	private volatile static PhotoFolderGetData instance;
	private long lastGetDataTime = Long.MIN_VALUE;
	private static final long TIME_UP = 1000 * 60 * 3;

	private PhotoFolderGetData() {
	}
	public static PhotoFolderGetData getInstance() {
		if (instance == null) {
			synchronized (PhotoFolderGetData.class) {
				if (instance == null) {
					instance = new PhotoFolderGetData();
				}
			}
		}
		return instance;
	}
	public List<ImageFolderInfo> getData(ContextWrapper wrapper,
			boolean forceRefresh) {
		//		long begin = SystemClock.elapsedRealtime();
		/*if (forceRefresh || SystemClock.elapsedRealtime() - lastGetDataTime > TIME_UP) {
			listfolder.clear();
		}*/
		//		if (mGroupMapFolderInfo.isEmpty()) {
		if (mGroupMapFolderInfo != null) {
			mGroupMapFolderInfo.clear();
		}
		getAllFolderImages(wrapper);
		listfolder = subImagefolderinfo(wrapper, mGroupMapFolderInfo);
		lastGetDataTime = SystemClock.elapsedRealtime();
		//		}
		//		Log.d(TAG, "getData spends " + (SystemClock.elapsedRealtime() - begin));
		return listfolder;
	}
	public List<String> getImageList(String folderName) {
		return mGroupMapFolderInfo.get(folderName);
	}
	/**
	 * 组装分组界面GridView的数据源，因为我们扫描手机的时候将图片信息放在HashMap中 所以需要遍历HashMap将数据组装成List
	 * @param mGruopMap
	 * @return
	 */
	private synchronized List<ImageFolderInfo> subImagefolderinfo(
			Context context, HashMap<String, List<String>> mGruopMap) {
		if (mGruopMap.size() == 0) {
			return null;
		}
		List<ImageFolderInfo> list = new LinkedList<ImageFolderInfo>();
		Iterator<Map.Entry<String, List<String>>> it = mGruopMap.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<String, List<String>> entry = it.next();
			ImageFolderInfo mImageBean = new ImageFolderInfo();
			String key = entry.getKey();
			List<String> value = entry.getValue();
			mImageBean.setmFolderName(new File(key).getName());
			mImageBean.setmFolderImgCount(value.size());
			mImageBean.setmFolderFirstImgPath(value.get(0));
			mImageBean.setmFolderPath(key);
			if (Utils.getCameraFolder(context).getPath().equals(key)) {
				list.add(0, mImageBean);
			} else if (Utils.getPictureFolder(context).getPath().equals(key)) {
				if (list.size() >= 1
						&& Utils.getCameraFolder(context).getPath()
								.equals(list.get(0).getmFolderPath())) {
					list.add(1, mImageBean);
				} else {
					list.add(0, mImageBean);
				}
			} else if ("cartoon".equals(new File(key).getName())) {
				if (list.size() >= 2
						&& Utils.getPictureFolder(context).getPath()
								.equals(list.get(1).getmFolderPath())) {
					list.add(2, mImageBean);
				} else if (list.size() >= 1
						&& (Utils.getCameraFolder(context).getPath()
								.equals(list.get(0).getmFolderPath()) || Utils
								.getPictureFolder(context).getPath()
								.equals(list.get(0).getmFolderPath()))) {
					list.add(1, mImageBean);
				} else {
					list.add(0, mImageBean);
				}
			} else {
				list.add(mImageBean);
			}
		}
		return list;
	}
	/**
	 * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
	 */
	private synchronized void getAllFolderImages(ContextWrapper wrapper) {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return;
		}
		Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		ContentResolver mContentResolver = wrapper.getContentResolver();
		// 只查询jpeg和png的图片
		Cursor mCursor = mContentResolver.query(mImageUri, null, "("
				+ MediaColumns.MIME_TYPE + "=? or " + MediaColumns.MIME_TYPE
				+ "=? )", new String[] {
				"image/jpeg", "image/png"},
				ImageColumns.BUCKET_DISPLAY_NAME);
		try {
			if (mCursor != null) {
				while (mCursor.moveToNext()) {
					// 获取图片的路径
					String path = mCursor.getString(mCursor
							.getColumnIndex(MediaColumns.DATA));
					/**
					 * 获取每张图片的大小
					 */
					// 获取该图片的父路径名
					String parentName = new File(path).getParentFile()
							.getPath();
					// 根据父路径名将图片放入到mGruopMap中
					if (!mGroupMapFolderInfo.containsKey(parentName)) {
						List<String> chileList = new ArrayList<String>();
						chileList.add(path);
						mGroupMapFolderInfo.put(parentName, chileList);
					} else {
						mGroupMapFolderInfo.get(parentName).add(path);
					}
				}
			}
		} catch (Exception e) {
			if (mCursor != null) {
				mCursor.close();
			}
		}
	}
}
