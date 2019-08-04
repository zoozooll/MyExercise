/**
 * 
 */
package com.mogoo.ping.ctrl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

import com.mogoo.ping.R;
import com.mogoo.ping.app.MessageDialog;
import com.mogoo.ping.model.ApksDao;
import com.mogoo.ping.model.DataBaseConfig;
import com.mogoo.ping.model.SharedPreferencesManager;
import com.mogoo.ping.network.NetworkWorking;
import com.mogoo.ping.utils.SoftDownloader;
import com.mogoo.ping.utils.UsedDataManager;
import com.mogoo.ping.utils.Utilities;
import com.mogoo.ping.vo.ApkItem;
import com.mogoo.ping.vo.UsedActivityItem;

/**
 * @author Aaron Lee
 * TODO
 * @Date 下午8:27:16  2012-11-1
 */
public class GridSoftwaresController1 implements OnItemClickListener {
	private static final String TAG = "GridSoftwaresController";
	private static final long SYNC_PER_MILLION = 1000 *60 * 3;
	private static final int REMOTE_RESULT_SUCCESS = 1;
	private static final int REMOTE_RESULT_TIMEDOWN = 2;
	private static final int LOCAL_RESULT_USED = 3;
	private static final int REMOTE_RESULT_FAIL = 0;
	
	public static final int TAG_APPLICATIONS_LASTED = 1;
	public static final int TAG_APPLICATIONS_RECOMEND = 1 << 1;
	public static final int TAG_GAME_LASTED = 1 << 2;
	public static final int TAG_GAME_RECOMEND = 1 << 3;
	public static final int TAG_APKS_USED = 1 << 4;
	
	private Context mActivity;
	private GridView mGridView;
	private BaseAdapter mAdapter;
	private int mTag;
	private Callable<Integer> mSyncContentThread;
	private Handler mResultHandler;
	private FutureTask<Integer> future;
	private boolean isShowAtFront;
	
	public GridSoftwaresController1(Context mActivity, GridView mGridView, int flag) {
		super();
		this.mActivity = mActivity;
		this.mGridView = mGridView;
		this.mTag = flag;
		init();
	}
	

	private void init() {
		mGridView.setOnItemClickListener(this);
		if (mTag == TAG_APKS_USED) {
			mSyncContentThread = new LoadUsedTask();
		} else {
			mSyncContentThread = new LoadRemoteDataTask(mActivity);
		}
		
		mResultHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case REMOTE_RESULT_SUCCESS:{
					if (mAdapter != null) {
						((SoftwaresAdapter)mAdapter).changeCursor((Cursor)msg.obj);
					}
					String format = mActivity.getResources().getString(R.string.toast_update_success);
					CharSequence dateStr = format + DateFormat.format(mActivity.getResources().getString(R.string.toast_update_data_format), System.currentTimeMillis());
					/*Toast toast = Toast.makeText(mActivity, dateStr, Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.BOTTOM, 0, 0);
					toast.show();*/
					ToastController.makeToast(mActivity, dateStr);
					break;
				}
				case REMOTE_RESULT_TIMEDOWN: {
					break;
				}
				case REMOTE_RESULT_FAIL: {
					break;
				}
				case LOCAL_RESULT_USED: {
					List<UsedActivityItem> data =  (List<UsedActivityItem>) msg.obj;
					((SoftwaresUsedAdapter)mAdapter).setmData(data);
					mAdapter.notifyDataSetChanged();
					break;
				}
				default:
					
					break;
				}
				super.handleMessage(msg);
			}
			
		};
		future = new FutureTask<Integer>(mSyncContentThread) {
			
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
	}
	
	/*public void setAdapter(boolean force) {
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
	}*/

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
		case TAG_APPLICATIONS_LASTED:
			if (mAdapter == null) {
				Cursor c = ApksDao.getInstance(mActivity).queryAllCursorTable(
						DataBaseConfig.ApplicationsLastedTable.TABLE_NAME);
				mAdapter = new SoftwaresAdapter(mActivity, c);
				mGridView.setAdapter(mAdapter);
			}
			break;
		case TAG_APPLICATIONS_RECOMEND:
			if (mAdapter == null) {
				Cursor c = ApksDao.getInstance(mActivity).queryAllCursorTable(
						DataBaseConfig.ApplicationsRecomendTable.TABLE_NAME);
				mAdapter = new SoftwaresAdapter(mActivity, c);
				mGridView.setAdapter(mAdapter);
			}
			break;	
		case TAG_GAME_LASTED:
			if (mAdapter == null) {
				Cursor c = ApksDao.getInstance(mActivity).queryAllCursorTable(
						DataBaseConfig.GamesLastedTable.TABLE_NAME);
				mAdapter = new SoftwaresAdapter(mActivity, c);
				mGridView.setAdapter(mAdapter);
			}
			break;
		case TAG_GAME_RECOMEND:
			if (mAdapter == null) {
				Cursor c = ApksDao.getInstance(mActivity).queryAllCursorTable(
						DataBaseConfig.GamesRecomendTable.TABLE_NAME);
				mAdapter = new SoftwaresAdapter(mActivity, c);
				mGridView.setAdapter(mAdapter);
			}
			break;
		case TAG_APKS_USED:
			if (mAdapter == null) {
				mAdapter = new SoftwaresUsedAdapter<UsedActivityItem>(mActivity, null);
				mGridView.setAdapter(mAdapter);
			}
			break;
		default:
			break;
		}
		new Thread(future).start();
	}
	
	/*public void refreshContents(Object... newCursor) {
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
	}*/
	
	

	private void startSavedActivity(Intent intent) {
		try {
			mActivity.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			ToastController.makeToast(mActivity, "软件已经卸载");
		}
	}
	
	/**
	 * @return the isShowAtFront
	 */
	public boolean isShowAtFront() {
		return isShowAtFront;
	}


	/**
	 * @param isShowAtFront the isShowAtFront to set
	 */
	public void setShowAtFront(boolean isShowAtFront) {
		this.isShowAtFront = isShowAtFront;
	}


	public void onStart() {
		if (isShowAtFront) {
			startToRefreshContents();
		}
	}
	
	public void onStop() {
		
	}
	
	public void onDestroy() {
		
	}
	
	/**
	 * 
	 * @author Aaron Lee
	 * @Date ����3:00:13  2012-9-26
	 */
	public class LoadRemoteDataTask implements Callable<Integer> {
		private Context mContext;
		private List<ApkItem> mData;
		private boolean isRunning;
		
		public LoadRemoteDataTask(Context mContext) {
			super();
			this.mContext = mContext;
		}

		@Override
		public Integer call(){
			int flag = 0;
			SharedPreferencesManager spm = SharedPreferencesManager.getSharedPreferencesManager(mContext);
			final long lastUpdate = spm.getLastUpdateTimeMillions(mTag);
			final long now = System.currentTimeMillis();
			if (now - lastUpdate > SYNC_PER_MILLION && !isRunning) {
				isRunning = true;
				flag = resolveApkItemsFromRemote(mContext, mTag);
			} else {
				flag = REMOTE_RESULT_TIMEDOWN;
				mResultHandler.sendEmptyMessage(flag);
			}
			isRunning = false;
			return flag;
		}
		private int resolveApkItemsFromRemote(Context context ,int flag) {
			try {
				if (NetworkWorking.checkInternet(context) == -1) {
					Log.d(TAG, "no connect");
					return REMOTE_RESULT_FAIL;
				} 
				if (mData == null) {
					mData = new ArrayList<ApkItem>();
				} else {
					mData.clear();
				}
				synchronized (mData) {
					
					switch (flag) {
					case TAG_APPLICATIONS_LASTED:
					case TAG_APPLICATIONS_RECOMEND:
					case TAG_GAME_LASTED:
					case TAG_GAME_RECOMEND:{
						mData.addAll(SoftDownloader.getImageInfos(UrlController.getPathRemotePathGamesRecomend("1", "28"), flag));
						insertApksIntoDB(context, DataBaseConfig.GamesRecomendTable.TABLE_NAME);
						break;
					}

					default:
						return REMOTE_RESULT_FAIL;
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				return REMOTE_RESULT_FAIL;
			}
			return REMOTE_RESULT_SUCCESS;
		}
		
		private void insertApksIntoDB(Context context, String table) {
			ApksDao dao = ApksDao.getInstance(context);
			dao.clearData(table);
			dao.addItems(table, mData);
			SharedPreferencesManager spm = SharedPreferencesManager.getSharedPreferencesManager(mContext);
			spm.updateLastUpdateTimeMillions(mTag, System.currentTimeMillis());
			Message msg = new Message();
			msg.what = REMOTE_RESULT_SUCCESS;
			msg.arg1 = mTag;
			msg.obj = dao.queryAllCursorTable(table);
			mResultHandler.sendMessage(msg);
		}
	} 
	
	private class LoadUsedTask implements Callable<Integer> {

		@Override
		public Integer call() throws Exception {
			List< UsedActivityItem> list = UsedDataManager.getFullyUsedApks(mActivity);
			Message msg = new Message();
			msg.what = LOCAL_RESULT_USED;
			msg.obj = list;
			mResultHandler.sendMessage(msg);
			return msg.what;
		}
		
	}
}
