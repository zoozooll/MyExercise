package com.butterfly.vv.camera.renew;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.butterfly.vv.camera.base.GetImageFileUtil;
import com.butterfly.vv.camera.base.ImageFolderInfo;
import com.butterfly.vv.camera.base.ImageInfoHolder;
import android.util.Log;
import android.content.Context;

public class CameraCellGetData {
	private static String TAG = "CameraCellGetData";
	private Context mContext;
	private List<ImageInfoHolder> mImageInfoList = new ArrayList<ImageInfoHolder>(); // 图片文件的路径

	public CameraCellGetData(Context context) {
		mContext = context;
	}
	public List<ImageInfoHolder> getPathAllNewImageInfo() {
		ImageFolderInfo folderInfo = new ImageFolderInfo();
		String cameraPhotoPath = GetImageFileUtil.getPublicDcimPach();
		folderInfo.setmFolderPath(cameraPhotoPath);
		folderInfo.setmIsRecursion(true);
		mImageInfoList = GetImageFileUtil.getAllImageInfo(mContext);
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
		/*
		 * for(ImageInfoHolder h1: mImageInfoList){ Log.d(TAG,"mImageInfoList:" + h1.mLatestTime); }
		 */
		Log.d(TAG, "getPathAllNewImageInfo size = " + mImageInfoList.size());
		return mImageInfoList;
	}
}
