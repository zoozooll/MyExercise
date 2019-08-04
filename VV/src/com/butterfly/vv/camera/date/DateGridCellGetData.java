package com.butterfly.vv.camera.date;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import com.butterfly.vv.camera.MyDateFormat;
import com.butterfly.vv.camera.base.DateImageHolder;
import com.butterfly.vv.camera.base.GetImageFileUtil;
import com.butterfly.vv.camera.base.ImageFolderInfo;
import com.butterfly.vv.camera.base.ImageInfoHolder;

public class DateGridCellGetData {
	private static String TAG = "DateGridCellGetData";
	private volatile static DateGridCellGetData instance;
	private Context mContext;
	private List<ImageInfoHolder> mImageInfoList = new Vector<ImageInfoHolder>();
	private List<DateImageHolder> mAllDateImageList = new Vector<DateImageHolder>();
	private long lastGetDateTime = Long.MIN_VALUE;
	private static final long TIME_UP = 1000 * 60 * 5;

	public static DateGridCellGetData getInstance(Context context) {
		if (instance == null) {
			synchronized (DateGridCellGetData.class) {
				if (instance == null) {
					instance = new DateGridCellGetData(context);
				}
			}
		}
		return instance;
	}
	private DateGridCellGetData(Context context) {
		mContext = context;
	}
	public List<DateImageHolder> getmAllDateImageList() {
		return mAllDateImageList;
	}
	/**
	 * 获取日期相册数据
	 * @Title: getPathAllDateImageInfo
	 * @Description: TODO
	 * @return
	 * @return: List<DateImageHolder>
	 */
	public synchronized List<DateImageHolder> getPathAllDateImageInfo() {
		String cameraPhotoPath = GetImageFileUtil.getPublicDcimPach();
		ImageFolderInfo folderInfo = new ImageFolderInfo();
		folderInfo.setmFolderPath(cameraPhotoPath);
		folderInfo.setmIsRecursion(true);
		mImageInfoList = GetImageFileUtil.getAllImageInfo(mContext);
		// sort by photo shoot time, latest photo is in the head
		Collections.sort(mImageInfoList, new Comparator<ImageInfoHolder>() {
			@Override
			public int compare(ImageInfoHolder lhs, ImageInfoHolder rhs) {
				// 左边比右边，1表示降序
				int result = rhs.mLatestTime.compareTo(lhs.mLatestTime);
				return result;
			}
		});
		int allImageCount = mImageInfoList.size();
		int dateLength = ImageInfoHolder.BASE_DATE_LENGTH;
		String tempDate, tempDate2 = null;
		int length1, length2;
		int startCount = 0;
		int endCount = 0;
		for (int i = 0; i < allImageCount - 1; i++) {
			tempDate = mImageInfoList.get(i).mLatestTime;
			length1 = dateLength < tempDate.length() ? dateLength : tempDate
					.length();
			tempDate = tempDate.substring(0, length1);
			tempDate2 = mImageInfoList.get(i + 1).mLatestTime;
			length2 = dateLength < tempDate2.length() ? dateLength : tempDate2
					.length();
			tempDate2 = tempDate2.substring(0, length2);
			if (tempDate.equals(tempDate2) == false) {
				endCount = i + 1;
				DateImageHolder dateImage = new DateImageHolder();
				for (int j = startCount; j < endCount; j++) {
					dateImage.mDateImageList.add(mImageInfoList.get(j));
				}
				dateImage.mDateString = dateImage.mDateImageList.get(0).mLatestTime;
				mAllDateImageList.add(dateImage);
				startCount = endCount;
			}
		}
		// Log.d(TAG,"getPathAllDateImageInfo last startCount = " + startCount);
		if (startCount < allImageCount) {
			DateImageHolder dateImage = new DateImageHolder();
			for (int j = startCount; j < allImageCount; j++) {
				dateImage.mDateImageList.add(mImageInfoList.get(j));
			}
			dateImage.mDateString = dateImage.mDateImageList.get(0).mLatestTime;
			mAllDateImageList.add(dateImage);
		}
		/*
		 * for (int i = 0; i < mAllDateImageList.size(); i++) { Log.d(TAG,"mAllDateImageList, " +
		 * mAllDateImageList.get(i).mDateString + "count, " + mAllDateImageList.get(i).mImageCount);
		 * }
		 */
		return mAllDateImageList;
	}
	/**
	 * 获取日期相册数据
	 * @Title: getPathAllDateImageInfo
	 * @Description: TODO
	 * @return
	 * @return: List<DateImageHolder>
	 */
	public synchronized List<DateImageHolder> getPathAllDateImageInfo2() {
		//		long begin = SystemClock.elapsedRealtime();
		//		if (begin - lastGetDateTime > TIME_UP) {
		if (mAllDateImageList != null) {
			mAllDateImageList.clear();
		}
		//		}
		if (mAllDateImageList == null || mAllDateImageList.isEmpty()) {
			mImageInfoList = GetImageFileUtil.getAllImageInfo(
					mContext);
			int allImageCount = mImageInfoList != null ? mImageInfoList.size()
					: 0;
			String tempDate = null; // 循环中上一个项的时间值；
			DateImageHolder dateImage = null;
			for (int i = 0; i < allImageCount - 1; i++) {
				ImageInfoHolder curItem = mImageInfoList.get(i);
				if (!(curItem.mLatestTime).equals(tempDate)
						|| dateImage == null) {
					dateImage = new DateImageHolder();
					dateImage.mDateString = curItem.mLatestTime;
					mAllDateImageList.add(dateImage);
					tempDate = curItem.mLatestTime;
				}
				dateImage.mDateImageList.add(curItem);
			}
			/*if (startCount < allImageCount) {
				DateImageHolder dateImage = new DateImageHolder();
				for (int j = startCount; j < allImageCount; j++) {
					dateImage.mDateImageList.add(mImageInfoList.get(j));
				}
				dateImage.mImageCount = dateImage.mDateImageList.size();
				dateImage.mDateString = dateImage.mDateImageList.get(0).mLatestTime;
				mAllDateImageList.add(dateImage);
			}*/
			//		step2 = SystemClock.elapsedRealtime();
			//		Log.d(TAG, "getPathAllDateImageInfo2 spends3 " + (step2 - step1));
			//		step1 = step2;
			//		Log.d(TAG, "getPathAllDateImageInfo2 spends " + (SystemClock.elapsedRealtime() - begin));
			lastGetDateTime = SystemClock.elapsedRealtime();
		}
		return mAllDateImageList;
	}
	public List<ImageInfoHolder> getFolderImageFile(
			List<ImageFolderInfo> listfolder2) {
		GetImageFileUtil fUtil;
		for (int i = 0; i < listfolder2.size(); i++) {
			File f = new File(listfolder2.get(i).getmFolderPath());
			long modifiedTime = f.lastModified();
			ImageInfoHolder holder = new ImageInfoHolder();
			holder.mImageModifiedTime = MyDateFormat.getDateByTimeMillis(
					modifiedTime, "yyyy:MM:dd HH:mm:ss");
			holder.mImagePath = f.getPath();
			mImageInfoList.add(holder); // 将文件的路径添加到list集合中
		}
		// sort by photo shoot time, latest photo is in the head
		Collections.sort(mImageInfoList, new Comparator<ImageInfoHolder>() {
			@Override
			public int compare(ImageInfoHolder lhs, ImageInfoHolder rhs) {
				// 左边比右边，1表示降序
				int result = rhs.mLatestTime.compareTo(lhs.mLatestTime);
				// Log.d(TAG, "result = " + result);
				return result;
			}
		});
		Log.i("VV", "mImageInfoList=" + mImageInfoList);
		return mImageInfoList;
	}
	/**
	 * 组装分组界面GridView的数据源，因为我们扫描手机的时候将图片信息放在HashMap中 所以需要遍历HashMap将数据组装成List
	 * @param mGruopMap
	 * @return
	 */
	public List<ImageFolderInfo> subImagefolderinfo(
			HashMap<String, List<String>> mGruopMap) {
		if (mGruopMap.size() == 0) {
			return null;
		}
		List<ImageFolderInfo> list = new ArrayList<ImageFolderInfo>();
		Iterator<Map.Entry<String, List<String>>> it = mGruopMap.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<String, List<String>> entry = it.next();
			ImageFolderInfo mImageBean = new ImageFolderInfo();
			String key = entry.getKey();
			List<String> value = entry.getValue();
			mImageBean.setmFolderName(key);
			list.add(mImageBean);
		}
		Collections.sort(list, new Comparator<ImageFolderInfo>() {
			@Override
			public int compare(ImageFolderInfo lhs, ImageFolderInfo rhs) {
				// TODO Auto-generated method stub
				int result = rhs.getmFolderName().compareTo(
						lhs.getmFolderName());
				return result;
			}
		});
		return list;
	}
	public List<ImageInfoHolder> getmImageInfoList() {
		return mImageInfoList;
	}
	public int getImageInfoIndex(ImageInfoHolder info) {
		int i = 0;
		for (ImageInfoHolder item : mImageInfoList) {
			if (info.equals(item)) {
				return i;
			}
			i++;
		}
		return i;
	}
}
