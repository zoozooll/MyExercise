package com.mogoo.ping.ctrl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.mogoo.ping.R;
import com.mogoo.ping.image.ImageDownloader;
import com.mogoo.ping.model.DataBaseConfig;
import com.mogoo.ping.utils.ImageTools;
import com.mogoo.ping.utils.SoftDownloader;
import com.mogoo.ping.utils.Utilities;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class SoftwaresAdapter extends ResourceCursorAdapter {
	
	private Context mContext;
	private ImageDownloader mImageDownloader;

	public SoftwaresAdapter(Context context, Cursor c,
			boolean autoRequery) {
		super(context, R.layout.content_apk_item, c, autoRequery);
		mContext = context;
		mImageDownloader = ImageDownloader.getInstance(context);
	}

	public SoftwaresAdapter(Context context, Cursor c) {
		super(context, R.layout.content_apk_item, c, true);
		mContext = context;
		mImageDownloader = ImageDownloader.getInstance(context);
	}
	
	@Override
	public void bindView(View convertView, Context context, Cursor cursor) {
		LinearLayout  view = (LinearLayout) convertView;
		final TextView tvw_griditem_title = (TextView) view.findViewById(R.id.tvw_griditem_title);
		final ImageView  ivw_griditem_icon = (ImageView) view.findViewById(R.id.ivw_griditem_icon);
		tvw_griditem_title.setText(cursor.getString(cursor
				.getColumnIndexOrThrow(DataBaseConfig.ApkListTable.COLUMN_NAME)));
		final String tempURL = cursor
				.getString(cursor
						.getColumnIndexOrThrow(DataBaseConfig.ApkListTable.COLUMN_ICONURL_REMOTE));
		mImageDownloader.download(tempURL, ivw_griditem_icon, false);
		view.setTag(R.id.tab_packagename, cursor.getString(cursor
				.getColumnIndexOrThrow(DataBaseConfig.ApkListTable.COLUMN_PACKAGE_NAME)));
		/*view.setTag(R.id.tab_local_path, cursor.getString(cursor
				.getColumnIndexOrThrow(DataBaseConfig.ApkListTable.COLUMN_ADDRESS_LOCAL)));
		view.setTag(R.id.tab_remote_path, cursor.getString(cursor
				.getColumnIndexOrThrow(DataBaseConfig.ApkListTable.COLUMN_ADDRESS_REMOTE)));*/
		view.setTag(R.id.tab_app_id, cursor.getString(cursor
				.getColumnIndexOrThrow(DataBaseConfig.ApkListTable.COLUMN_ID)));
		view.setTag(R.id.tab_app_type, cursor.getInt(cursor
				.getColumnIndexOrThrow(DataBaseConfig.ApkListTable.COLUMN_APK_TYPE)));
		view.setTag(R.id.tab_app_downloadid, cursor.getLong(cursor
				.getColumnIndexOrThrow(DataBaseConfig.ApkListTable.COLUMN_APK_DOWNLOADID)));
		//showAsyncImage(context, ivw_griditem_icon, cursor);
	
	}
	
	private void showAsyncImage(Context context, ImageView view, Cursor cursor) {
		// show default images
		Drawable icon = null;
		String tempURL = cursor
				.getString(cursor
						.getColumnIndexOrThrow(DataBaseConfig.ApkListTable.COLUMN_ICONURL_REMOTE));

		String localFile = SoftDownloader.getLocalFileCache(RemoteApksManager.IMAGE_TEMP_URL, tempURL);

		if (tempURL != null && new File(localFile).exists()) {
			final int size = (int)Utilities.dpiToPixle(mContext.getResources(), 60);
				icon = new BitmapDrawable(ImageTools.getBitmapFromFile(
						localFile, size, size));
		} else {
			icon = context.getResources().getDrawable(R.drawable.ic_launcher);
			// start the thread to show async images
			LoadImageTask task = new LoadImageTask();
			task.execute(view, tempURL);
		}
		view.setImageDrawable(icon);
	}

	private final class LoadImageTask extends AsyncTask<Object, Integer, Uri>{  
        ImageView imageView;  
        String imagepath;  
        @Override  
        protected Uri doInBackground(Object... params) {  
        	imageView = (ImageView) params[0];  
            imagepath = (String) params[1];  
            try {
                return SoftDownloader.getImage(imagepath ,  
						RemoteApksManager.IMAGE_TEMP_URL);  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
            return null;  
        }  
        @Override  
        protected void onPostExecute(Uri result) {
        	if (result != null) {
        		Bitmap top = null;
				try {
					//top = Drawable.createFromStream(mContext.getContentResolver().openInputStream(result), null);
					final int size = (int)Utilities.dpiToPixle(mContext.getResources(), 60);
					top =  ImageTools.getBitmapFromFile(result.getPath() , size, size);
					if (top == null)
					imageView.setImageDrawable(new BitmapDrawable(top));
				}  catch (NullPointerException e) {
					e.printStackTrace();
				} catch (Exception ex){
					ex.printStackTrace();
				}
        	}
        }  
    } 
	

}
