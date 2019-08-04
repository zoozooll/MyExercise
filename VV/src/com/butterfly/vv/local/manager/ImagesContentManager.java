// û�õ�
package com.butterfly.vv.local.manager;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.Log;

public class ImagesContentManager {
	public static ImagesContentManager mContentManager = null;
	private Context context;

	public ImagesContentManager(Context con) {
		// TODO Auto-generated constructor stub
		this.context = con;
	}
	public static ImagesContentManager getInstance(Context con) {
		if (mContentManager == null) {
			mContentManager = new ImagesContentManager(con);
		}
		return mContentManager;
	}
	public void test() {
		Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		try {
			Cursor cursor = context.getContentResolver().query(uri, null,
					ImageColumns.BUCKET_DISPLAY_NAME, null, null);
			// cursor.moveToFirst();
			// String imageFilePath = cursor.getString(1);
			String imageFilePath = "";
			Log.i("Multi", cursor.getCount() + "-->" + imageFilePath);
			;
			int k = 1;
			while (cursor.moveToNext()) {
				// String s= PhoneLookup.DISPLAY_NAME;
				String getColumnNames[] = cursor.getColumnNames();
				for (int i = 0; i < getColumnNames.length; i++) {
					// Log.i("xxx",getColumnNames[i]+":"+cursor.getString(i));
				}
				// Log.i("Multi",
				// k+++" "+cursor.getPosition()+"-->"+cursor.getString(1));
			}
			cursor.close();
			Options options = new Options();
			options.inSampleSize = getSampleSize(imageFilePath);
			if (options.inSampleSize > 0) {
				// mBitmap = BitmapFactory.decodeFile(imageFilePath,
				// options);
				// Log.i("Multi", "while-->");
			} else {
				// mBitmap = BitmapFactory.decodeFile(imageFilePath);
				// mivPhoto.setImageBitmap(mBitmap);
				Log.i("Multi", "2-->");
			}
		} catch (Exception e) {
		}
	}
	public int getSampleSize(String path) {
		File file = new File(path);
		if (file.exists()) {
			return (int) (file.length() / (200 * 1024));
		}
		return 0;
	}
}
