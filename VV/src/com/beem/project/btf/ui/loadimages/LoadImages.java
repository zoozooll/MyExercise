package com.beem.project.btf.ui.loadimages;

import java.io.File;
import com.beem.project.btf.utils.ThreadUtils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;

public class LoadImages {
	private Context mContext;

	public LoadImages(Context mContext) {
		this.mContext = mContext;
		loadingimages();
	}
	private void loadingimages() {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			//LogUtils.i("暂无外部存储,不能扫描手机图片");
			return;
		}
		// 显示进度条
		// mProgressDialog = ProgressDialog.show(this, null, "正在加载...");
		ThreadUtils.executeTask(new Runnable() {
			@Override
			public void run() {
				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = mContext
						.getContentResolver();
				// 只查询jpeg和png的图片
				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaColumns.MIME_TYPE + "=? or "
								+ MediaColumns.MIME_TYPE + "=?", new String[] {
								"image/jpeg", "image/png" },
						MediaColumns.DATE_MODIFIED);
				while (mCursor.moveToNext()) {
					// 获取图片的路径
					String path = mCursor.getString(mCursor
							.getColumnIndex(MediaColumns.DATA));
					// 获取该图片的父路径名
					String parentName = new File(path).getParentFile()
							.getName();
					//LogUtils.i("---path=" + path);
				}
				mCursor.close();
			}
		});
	}
}
