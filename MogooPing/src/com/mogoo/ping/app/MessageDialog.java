package com.mogoo.ping.app;


import java.io.File;
import java.net.SocketTimeoutException;
import java.nio.MappedByteBuffer;
import java.util.List;

import org.apache.http.ParseException;


import android.R.bool;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
//import android.content.pm.IPackageInstallObserver;
//import android.content.pm.PackageParser;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.mogoo.ping.R;
import com.mogoo.ping.ctrl.RemoteApksManager;
import com.mogoo.ping.ctrl.ToastController;
import com.mogoo.ping.ctrl.UrlController;
import com.mogoo.ping.image.ImageDownloader;
import com.mogoo.ping.model.ApksDao;
import com.mogoo.ping.model.DataBaseConfig;
import com.mogoo.ping.model.DataBaseConfig.ApkListTable;
import com.mogoo.ping.network.NetworkWorking;
import com.mogoo.ping.utils.ShowDetailed;
import com.mogoo.ping.utils.Utilities;
import com.mogoo.ping.vo.MessageBody;
import static com.mogoo.ping.app.ToolUtils.*;
//import android.provider.Downloads;

public class MessageDialog extends Activity {
	
	
	private static final String SAVE_PATH = "push";
	private static final String TAG = "MessageDialog";
	private static final int SHOW_PICS = 0x12222;
	private static final int SHOW_ROOT = 0x12221;
	public static final String MSG_DETAILED_APKID = "MSG_DETAILED_APKID";
	public static final String MSG_DETAILED_APKTYPE = "MSG_DETAILED_APKTYPE";
	public static final String MSG_DETAILED_APK_DOWNLOAD_ID = "MSG_DETAILED_APK_DOWNLOAD_ID";
	
	private View root;
	private ImageView mImage;
	private TextView mTitle;
	private TextView mType;
	private TextView mVersion;
	private TextView mSize;
	private TextView mContent;
	private Button mStartDown;
	private Button mCancel;
	private LinearLayout mPhoto;
	private ImageDownloader mImagedownload;
	private Handler mHandler;
	private List<String> appDescribe;
	private File base;
	private MessageBody mBody;
	private int mApkType;
	private String mApkId;
	private long mDownloadId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.loading_dialog);
		findViews();
		setOnClickListener();
		if(ToolUtils.getSDKpath() != null && !ToolUtils.getSDKpath().equals("")){
			base = Environment.getExternalStoragePublicDirectory(SAVE_PATH);		
			base.mkdirs();
		}
		
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				
				if(msg.what == SHOW_PICS){
					for(int i = 0 ; i < appDescribe.size() ; ++i)
					{
						mPhoto.addView(create(appDescribe.get(i), MessageDialog.this));
					}
				} else if (msg.what == SHOW_ROOT) {
					showRootViews();
				}
				super.handleMessage(msg);
			}

			
			
		};
		
		Intent intent = getIntent();
		//mBody = (MessageBody) intent.getSerializableExtra("MSG_EXTRA");
		mApkId = intent.getStringExtra(MSG_DETAILED_APKID);
		mApkType = intent.getIntExtra(MSG_DETAILED_APKTYPE, 0);
		mDownloadId = intent.getLongExtra(MSG_DETAILED_APK_DOWNLOAD_ID, 0);
		mImagedownload = ImageDownloader.getInstance(this);
		
		new DetailedData().execute(mApkId);
	}

	/**
	 * @Author lizuokang
	 * TODO
	 * @Date 下午8:20:34  2012-10-10
	 */
	private void setDataContent() {
		mBody.setmAppType(mApkType);
		mTitle.setText(mBody.getmAppName());
		mType.setText(this.getString(R.string.app_type)+"  " + getString(mBody.getmAppType()));
		mVersion.setText(this.getString(R.string.app_version) + "  "+mBody.getmVerName());
		try{
			mSize.setText(this.getString(R.string.app_size) + "  "+ToolUtils.getSizeStr(Integer.parseInt(mBody.getmSize())));
		}catch(Exception ex){
			mSize.setText(this.getString(R.string.app_size) + "  ");
		}
		
		mContent.setText("  " + mBody.getmAppDescribe());
		mImagedownload.download(mBody.getmAppImage(), mImage, false);
		
		appDescribe = mBody.getmPreviewLists();
		if(appDescribe != null && appDescribe.size() > 0){
			mHandler.sendEmptyMessage(SHOW_PICS);
		}
	}
	
	private void showRootViews() {
		setContentView(root);
	}

	/**
	 * 设置点击事件
	 */
	private void setOnClickListener() {
		MyClickListener clickListener = new MyClickListener();
		mStartDown.setOnClickListener(clickListener);
		mCancel.setOnClickListener(clickListener);
	}

	/**
	 * @param rootView the view you want to display
	 */
	private void findViews() {
		root = LayoutInflater.from(this).inflate(R.layout.push_dialog, null);
		mImage = (ImageView)root.findViewById(R.id.image);
		mTitle = (TextView)root.findViewById(R.id.title);
		mType = (TextView)root.findViewById(R.id.type);
		mVersion = (TextView)root.findViewById(R.id.version);
		mSize = (TextView)root.findViewById(R.id.size);
		mContent = (TextView)root.findViewById(R.id.content);
		mPhoto = (LinearLayout)root.findViewById(R.id.photo);
		mStartDown = (Button)root.findViewById(R.id.app_download);
		mCancel = (Button)root.findViewById(R.id.app_cancel);
	}
	
	public View create(String imageUrl,Context context)
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		View row = inflater.inflate(R.layout.image_shot_item, null);
		
		ImageView iv = (ImageView) row.findViewById(R.id.app_iv);
		iv.setImageResource(R.drawable.appdetail_shot_default);
		
		ImageDownloader idl = ImageDownloader.getInstance(context);
		//idl.setDefaultRes(R.drawable.appdetail_shot_default, R.drawable.appdetail_shot_default, R.drawable.appdetail_shot_default);
		idl.download(imageUrl, iv, false);
		
		return row;
	}
	
	private void starDownloadSoftware(String title, String remotePath, String appId) {
		Log.d(TAG, "appId "+appId);
		Log.d(TAG, "mDownloadId "+mDownloadId);
		int status = getDownloadState(this, mApkId, getTheTypeTableName(mApkType));
		if ( status == DownloadManager.STATUS_RUNNING || status == DownloadManager.STATUS_PAUSED) {
			//Toast.makeText(this, R.string.toast_download_running, Toast.LENGTH_SHORT).show();
			ToastController.makeToast(this, R.string.toast_download_running);
			return;
		} else if (status == DownloadManager.STATUS_FAILED) {
			return;
		}
		if (!Utilities.isCanUseSdCard()) {
			//Toast.makeText(this, R.string.toast_sdcard_nomounted, Toast.LENGTH_SHORT).show();
			ToastController.makeToast(this, R.string.toast_sdcard_nomounted);
			return;
		}
		String localPath = RemoteApksManager.getApkDownloadLocalTempName(title);
		File localApkFile = new File(RemoteApksManager.APK_TEMP_URL + localPath);
		long id = NetworkWorking.downloadAndInstallApk(this, title, localPath, remotePath);
		Log.d(TAG, "downloadAndInstallApk id "+id);
		updateDownloadIdIntoModel(id);
		try {
			if (id > 0){
				Log.i(TAG, "request download app "+UrlController.getPathRegisterMogooDesktop(appId));
				NetworkWorking.requestByGet(UrlController.getPathRegisterMogooDesktop(appId));
			} else if (id == 0){
				Log.d(TAG, "installing installSaveApk "+RemoteApksManager.APK_TEMP_URL + localPath);
				ApplicationInfo info = Utilities.getApkCode(this, localApkFile);
				final String packageName = info.packageName;
				if (!TextUtils.isEmpty(packageName) && TextUtils.equals(packageName, mBody.getPackageName()));
				Utilities.installSaveApk(this, Uri.fromFile(localApkFile));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	private void updateDownloadIdIntoModel(long downloadId) {
		ApksDao dao = ApksDao.getInstance(this);
		String table = getTheTypeTableName(mApkType);
		ContentValues values = new ContentValues();
		values.put(DataBaseConfig.ApkListTable.COLUMN_APK_DOWNLOADID, downloadId);
		dao.updateSingleItem(table, values, mApkId);
	}

	private class MyClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.app_download) {
				starDownloadSoftware(mBody.getmAppName()+"_"+mBody.getmVerName(), mBody.getmDownUrl(), mApkId);
				MessageDialog.this.finish();
			} else if(v.getId() == R.id.app_cancel) {
				MessageDialog.this.finish();
			}
		}
		
	}
	
	
	private class DetailedData extends AsyncTask<String, Void, MessageBody> {

		@Override
		protected MessageBody doInBackground(String... params) {
			Log.d(TAG, "async appid "+params[0]);
			final String path = UrlController.getDetailed(params[0]);
			MessageBody body = null;
			try {
				body= ShowDetailed.getDetailedInfos(path);
			}catch (SocketTimeoutException e){
				Log.w(TAG, "time out", e);
				return null;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return body;
		}

		@Override
		protected void onPostExecute(MessageBody result) {
			if (result != null) {
				mBody = result;
				setDataContent();
				showRootViews();
			} else {
				mBody = null;
				//Toast.makeText(MessageDialog.this, R.string.toast_noconnect, Toast.LENGTH_SHORT).show();
				ToastController.makeToast(MessageDialog.this, R.string.toast_noconnect);
				finish();
			}
		}
		
	}
	
/*	class PackageInstallObserver extends IPackageInstallObserver.Stub {
		public void packageInstalled(String packageName, int returnCode) {
		}
	}*/
	
}
