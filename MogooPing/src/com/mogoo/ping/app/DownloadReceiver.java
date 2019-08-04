/**
 * 
 */
package com.mogoo.ping.app;

import java.io.File;
import java.io.FileNotFoundException;

import com.mogoo.ping.R;
import com.mogoo.ping.ctrl.RemoteApksManager;
import com.mogoo.ping.ctrl.ToastController;
import com.mogoo.ping.model.ApksDao;
import com.mogoo.ping.utils.Utilities;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @author Aaron Lee
 * TODO
 * @Date ����3:01:14  2012-9-27
 */
public class DownloadReceiver extends BroadcastReceiver {

	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		if ("com.mogoo.ping".equals(intent.getPackage())) {
			long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
			Log.d("aaron", "receive complement "+id);
			DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
			DownloadManager.Query query = new DownloadManager.Query().setFilterById(id);
			Cursor cursor = dm.query(query);
			cursor.moveToFirst();
			boolean isSuccese = false;
			if (!cursor.isAfterLast()) {
				Log.i("IMPORTANT DOWNLOAD LOG", "download status "+cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)));
				String localApkUriStr = cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI));
				if (localApkUriStr != null && cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
					Uri localApkUri = Uri.parse(localApkUriStr);
					Log.d("IMPORTANT DOWNLOAD LOG", "uri path "+localApkUri.getPath());
					File file = new File(localApkUri.getPath());
					Log.d("aaron", "file path "+file.getPath());
					if (file.exists()){
						try {
							ApplicationInfo info = Utilities.getApkCode(context, file);
							final String packageName = info.packageName;
							if (!TextUtils.isEmpty(packageName)) {
								isSuccese = true;
								Utilities.installSaveApk(context, localApkUri);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
					}
				} else {
				}
			}
			if (!isSuccese)
				ToastController.makeToast(context, R.string.toast_download_fail);
			cursor.close();
		}
	}

}
