package com.mogoo.ping.ctrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadFactory;

import android.content.Context;
import android.content.IntentSender.SendIntentException;
import android.database.Cursor;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.mogoo.ping.MainActivity;
import com.mogoo.ping.R;
import com.mogoo.ping.model.ApksDao;
import com.mogoo.ping.model.DataBaseConfig;
import com.mogoo.ping.model.SharedPreferencesManager;
import com.mogoo.ping.network.NetworkWorking;
import com.mogoo.ping.utils.SoftDownloader;
import com.mogoo.ping.utils.MD5;
import com.mogoo.ping.utils.Utilities;
import com.mogoo.ping.vo.ApkItem;

@Deprecated
public class RemoteApksManager {
	
	private final static String TAG = "RemoteApksManager";
//	private final static String REMOTE_PATH_APPLICATIONS_LASTED = "http://www.imogoo.cn/MAS/Store/newappinfo.do?akey=123&page=1&pagesize=2&uid=123&ct=MGP";
//	private final static String REMOTE_PATH_APPLICATIONS_RECOMEND = "http://www.imogoo.cn/MAS/Store/getapprank.do?akey=123&page=1&pagesize=2&uid=123&ct=MGP";
//	private final static String REMOTE_PATH_GAME_LASTED = "http://www.imogoo.cn/MAS/Store/newgameinfo.do?akey=123&page=1&pagesize=2&uid=123&ct=MGP";
//	private final static String REMOTE_PATH_GAME_RECOMEND = "http://www.imogoo.cn/MAS/Store/getgamerank.do?akey=123&page=1&pagesize=2&uid=123&ct=MGP";
	public static final int SYNC_PER_MILLION = 1000 * 60 * 3;
	public static final int SYNC_PER_MILLION_MIN = 1000 * 60 * 3;
	public static final int TAG_APPLICATIONS_LASTED = 1;
	public static final int TAG_APPLICATIONS_RECOMEND = 1 << 1;
	public static final int TAG_GAME_LASTED = 1 << 2;
	public static final int TAG_GAME_RECOMEND = 1 << 3;
	public static final String IMAGE_TEMP_URL = Environment.getExternalStorageDirectory().getPath()+"/MOGOO_PING/images/";
	public static final String APK_TEMP_URL = Environment.getExternalStorageDirectory().getPath()+"/MOGOO_PING/softwares/";
	
	private Context mContext;
	private Runnable applicationsLastedThread;
	private Runnable applicationsRecomendThread;
	private Runnable gamesLastedThread;
	private Runnable gamesRecomendThread;
	private OnRemoteDataSyncListener mOnRemoteDataSyncListener;
	
	private Handler mSyncHandler = new Handler() ;
	private Timer mTimer;
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:{
				if (mOnRemoteDataSyncListener != null) {
					mOnRemoteDataSyncListener.onSyncSuccess((Cursor)msg.obj, msg.arg1);
				}
				break;
			}
			default:
				if (mOnRemoteDataSyncListener != null) {
					mOnRemoteDataSyncListener.onSyncFail();
				}
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	private static RemoteApksManager instance;
	
	private RemoteApksManager(Context context) {
		mContext = context;
		applicationsLastedThread = new SyncTaskThread( TAG_APPLICATIONS_LASTED);
		applicationsRecomendThread = new SyncTaskThread(TAG_APPLICATIONS_RECOMEND);
		gamesLastedThread = new SyncTaskThread(TAG_GAME_LASTED);
		gamesRecomendThread = new SyncTaskThread(TAG_GAME_RECOMEND);
	}
	
	public static RemoteApksManager getInstance(Context context) {
		if (instance == null) {
			instance = new RemoteApksManager(context);
		}
		return instance;
	}
	
	/**
	 * 
	 * @Author lizuokang
	 * @param flag
	 * @Date ����2:59:56  2012-9-26
	 */
	public void startSync(int flag) {
		if ((flag & TAG_APPLICATIONS_LASTED) == TAG_APPLICATIONS_LASTED) {
			mSyncHandler.post(applicationsLastedThread);
		}
		if ((flag & TAG_APPLICATIONS_RECOMEND) == TAG_APPLICATIONS_RECOMEND) {
			mSyncHandler.post(applicationsRecomendThread);
		}
		if ((flag & TAG_GAME_LASTED) == TAG_GAME_LASTED) {
			mSyncHandler.post(gamesLastedThread);
		}
		if ((flag & TAG_GAME_RECOMEND) == TAG_GAME_RECOMEND) {
			mSyncHandler.post(gamesRecomendThread);
		}
	}
	
	/**
	 * 
	 * @Author lizuokang
	 * �Ƴ�ʱ���µ��̣߳�
	 * @Date ����4:39:27  2012-10-10
	 */
	public void invaliteSync() {
		mSyncHandler.removeCallbacks(applicationsLastedThread);
		mSyncHandler.removeCallbacks(applicationsRecomendThread);
		mSyncHandler.removeCallbacks(gamesLastedThread);
		mSyncHandler.removeCallbacks(gamesRecomendThread);
	}
	
	/**
	 * 
	 * @author Aaron Lee
	 * @Date ����3:00:13  2012-9-26
	 */
	public static class LoadRemoteDataTask implements Callable<Integer> {
		private Context mContext;
		private int mTag;
		private List<ApkItem> mData;
		private boolean isRunning;
		private Handler mHandler;
		
		public LoadRemoteDataTask(Context mContext, int tag) {
			super();
			this.mContext = mContext;
			mTag = tag;
		}

		@Override
		public Integer call() throws Exception {
			int flag = 0;
			SharedPreferencesManager spm = SharedPreferencesManager.getSharedPreferencesManager(mContext);
			final long lastUpdate = spm.getLastUpdateTimeMillions(mTag);
			final long now = System.currentTimeMillis();
			if (now - lastUpdate > SYNC_PER_MILLION) {
				flag = resolveApkItemsFromRemote(mContext, mTag);
			} else {
				flag = 2;
			}
			return flag;
		}
		private int resolveApkItemsFromRemote(Context context ,int flag) {
			try {
				if (NetworkWorking.checkInternet(context) == -1) {
					//Toast.makeText(context, R.string.toast_noconnect, Toast.LENGTH_SHORT);
					Log.d(TAG, "no connect");
					//mHandler.sendEmptyMessage(2);
					return 0;
				} 
				if (mData == null) {
					mData = new ArrayList<ApkItem>();
				} else {
					mData.clear();
				}
				synchronized (mData) {
					
					switch (flag) {
					case TAG_APPLICATIONS_LASTED:{
						mData.addAll(SoftDownloader.getImageInfos(UrlController.getPathRemotePathApplicationsLast("1", "28"), flag));
						insertApksIntoDB(context, DataBaseConfig.ApplicationsLastedTable.TABLE_NAME);
						break;
					}
					case TAG_APPLICATIONS_RECOMEND:{
						mData.addAll(SoftDownloader.getImageInfos(UrlController.getPathRemotePathApplicationsRecomend("1", "28"), flag));
						insertApksIntoDB(context, DataBaseConfig.ApplicationsRecomendTable.TABLE_NAME);
						break;
					}
					case TAG_GAME_LASTED:{
						mData.addAll(SoftDownloader.getImageInfos(UrlController.getPathRemotePathGamesLast("1", "28"), flag));
						insertApksIntoDB(context, DataBaseConfig.GamesLastedTable.TABLE_NAME);
						break;
						
					}case TAG_GAME_RECOMEND:{
						mData.addAll(SoftDownloader.getImageInfos(UrlController.getPathRemotePathGamesRecomend("1", "28"), flag));
						insertApksIntoDB(context, DataBaseConfig.GamesRecomendTable.TABLE_NAME);
						break;
					}

					default:
						return 0;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
			return 1;
		}
		
		private void insertApksIntoDB(Context context, String table) {
			ApksDao dao = ApksDao.getInstance(context);
			dao.clearData(table);
			dao.addItems(table, mData);
			SharedPreferencesManager spm = SharedPreferencesManager.getSharedPreferencesManager(mContext);
			spm.updateLastUpdateTimeMillions(mTag, System.currentTimeMillis());
			Message msg = new Message();
			msg.what = 1;
			msg.arg1 = mTag;
			msg.obj = dao.queryAllCursorTable(table);
			mHandler.sendMessage(msg);
		}
	} 
	
	public OnRemoteDataSyncListener getmOnRemoteDataSyncListener() {
		return mOnRemoteDataSyncListener;
	}

	public void setmOnRemoteDataSyncListener(
			OnRemoteDataSyncListener mOnRemoteDataSyncListener) {
		this.mOnRemoteDataSyncListener = mOnRemoteDataSyncListener;
	}
	
	public interface OnRemoteDataSyncListener{
		public void onSyncSuccess(Cursor cursor, int tag);
		public void onSyncFail();
	}
	
	public static String getApkDownloadLocalTempName(String name) {
		return MD5.getMD5(name)+".apk";
	}
	
	public class SyncTaskThread implements Runnable {

		private int flag;
		private LoadRemoteDataTask task;
		private Handler mSyncHandler;
		
		public SyncTaskThread(int flag) {
			super();
			task = new LoadRemoteDataTask(mContext, flag);
		}
		
		
		@Override
		public void run() {
			
			Log.d(TAG, "SyncTaskThread "+task);
			FutureTask<Integer> future = new FutureTask<Integer>(task) {
				
				@Override
				protected void done() {
					try {
						int get = get();
						if (get == 1) {
							//mSyncHandler.postDelayed(this, SYNC_PER_MILLION);
						} else if (get == 2) {
							
						} else {
							//mSyncHandler.postDelayed(this, SYNC_PER_MILLION_MIN);
							mSyncHandler.sendEmptyMessage(2);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
				}
				
			};
			new Thread(future).start();
		}
		
	}
}
