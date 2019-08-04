package com.mogoo.ping.ctrl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import com.mogoo.ping.R;
import com.mogoo.ping.app.MessageDialog;
import com.mogoo.ping.model.ApksDao;
import com.mogoo.ping.model.DataBaseConfig;
import com.mogoo.ping.model.SharedPreferencesManager;
import com.mogoo.ping.network.NetworkWorking;
import com.mogoo.ping.utils.SoftDownloader;
import com.mogoo.ping.utils.Utilities;
import com.mogoo.ping.vo.ApkItem;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.GridView;

@Deprecated
public class GridSoftwaresController implements OnItemClickListener {
	
	private static final String TAG = "GridSoftwaresController";
	private static final long SYNC_PER_MILLION = 1000 * 60 * 3;
	
	public static final int TAG_APPLICATIONS_LASTED = 1;
	public static final int TAG_APPLICATIONS_RECOMEND = 1 << 1;
	public static final int TAG_GAME_LASTED = 1 << 2;
	public static final int TAG_GAME_RECOMEND = 1 << 3;
	
	private Activity mActivity;
	private GridView mGridView;
	private BaseAdapter mAdapter;
	private int mTag;
	private RemoteApksManager mRemoteManager;
	private Callable<Integer> mSyncContentThread;
	private Handler mResultHandler;
	private FutureTask<Integer> future = new FutureTask<Integer>(mSyncContentThread) {
		
		@Override
		protected void done() {
			try {
				int get = get();
				if (get == 1) {
					// sync data success
				} else if (get == 2) {
					// it is not delay time to sync the remote data
					
				} else {
					//mSyncHandler.postDelayed(this, SYNC_PER_MILLION_MIN);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		
	};
	
	public GridSoftwaresController(Activity mActivity, GridView mGridView, BaseAdapter mAdapter, int flag) {
		super();
		this.mActivity = mActivity;
		this.mGridView = mGridView;
		this.mAdapter = mAdapter;
		this.mTag = flag;
		init();
	}

	private void init() {
		mRemoteManager  =  RemoteApksManager.getInstance(mActivity);
		mGridView.setOnItemClickListener(this);
		mResultHandler = new Handler() {

			/* (non-Javadoc)
			 * @see android.os.Handler#handleMessage(android.os.Message)
			 */
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
			}
			
		};
	}
	
	public void setAdapter(boolean force) {
		if (mGridView == null || mAdapter == null) {
			throw new NullPointerException("GridView or Adapter is not found");
		}
		if (mGridView.getAdapter() != null) {
			if (force) {
				mGridView.setAdapter(mAdapter);
				return;
			} else {
				mAdapter.notifyDataSetChanged();
			}
		}else {
			mGridView.setAdapter(mAdapter);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent.getId() == R.id.grid_content_softwares_lasted || parent.getId() == R.id.grid_content_softwares_recommend) {
			final String packageName = (String) view.getTag(R.id.tab_packagename);
			final Intent launchIntent = Utilities.launcheredAble(mActivity, packageName);
			Log.i("IMPORTANT DOWNLOAD LOG", "package name "+packageName);
			if (launchIntent != null) {
				startSavedActivity(launchIntent);
			}else {
				//final String localPath = (String) view.getTag(R.id.tab_local_path);
				//final String remotePath = (String) view.getTag(R.id.tab_remote_path);
				final String appId = (String) view.getTag(R.id.tab_app_id);
				final int appType = Integer.valueOf(view.getTag(R.id.tab_app_type).toString());
				final long appDownloadId = Long.valueOf(view.getTag(R.id.tab_app_downloadid).toString());
				//starDownloadSoftware(localPath, remotePath, appId);
				//mDialogDetailedController.showDialog(localPath, remotePath, appId);
				Intent intent = new Intent(mActivity, MessageDialog.class);
				intent.putExtra(MessageDialog.MSG_DETAILED_APKID, appId);
				intent.putExtra(MessageDialog.MSG_DETAILED_APKTYPE, appType);
				intent.putExtra(MessageDialog.MSG_DETAILED_APK_DOWNLOAD_ID, appDownloadId);
				startSavedActivity(intent);
			}
		} else if (parent.getId() == R.id.grid_content_softwares_used) {
			startSavedActivity((Intent) view.getTag(R.id.tab_launch_intent));
		}
	}
	
	public void startToRefreshContents() {
		switch (mTag) {
		case RemoteApksManager.TAG_APPLICATIONS_LASTED:
			
		case RemoteApksManager.TAG_APPLICATIONS_RECOMEND:
					
		case RemoteApksManager.TAG_GAME_LASTED:
			
		case RemoteApksManager.TAG_GAME_RECOMEND:
			new Thread(future).start();
			break;
		default:
			refreshContents();
			break;
		}
	}
	
	public void refreshContents(Object... newCursor) {
		//Log.d("aaron", "newCursor change");
		if (mAdapter instanceof CursorAdapter) {
			try{
				((CursorAdapter)mAdapter).changeCursor((Cursor)newCursor[0]);
				mAdapter.notifyDataSetChanged();
			} catch (ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
			}
		} else {
			mAdapter.notifyDataSetChanged();
		}
	}
	
	

	private void startSavedActivity(Intent intent) {
		try {
			mActivity.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			ToastController.makeToast(mActivity, "软件已经卸载");
		}
	}
	
	
	/**
	 * @return the mSyncContentThread
	 */
	public Callable<Integer> getSyncContentThread() {
		return mSyncContentThread;
	}


	/**
	 * @param mSyncContentThread the mSyncContentThread to set
	 */
	public void setSyncContentThread( Callable<Integer> mSyncContentThread) {
		this.mSyncContentThread = mSyncContentThread;
	}


	public void onStart() {
		
	}
	
	public void onStop() {
		
	}
	
	public void onDestroy() {
		
	}
	
}
