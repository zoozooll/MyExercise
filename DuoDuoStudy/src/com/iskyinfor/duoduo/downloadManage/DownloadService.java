package com.iskyinfor.duoduo.downloadManage;

/*
 * <pre>
 * TODO	设置项汇总：
 * 1、能够同时开始下载的任务总数  MAX_RUNING_ITEM_COUNT = 3;
 * 2、同一连接断点续传的重复次数 MAX_RETRY_COUNT = 3;
 * 3、连接嗅探时的重复嗅探次数 Http_Sniffer_Count = 3 ;
 * 
 * </pre>
 */

import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

import com.iskyinfor.duoduo.downloadManage.broadcast.DownloadBroadcastManager;
import com.iskyinfor.duoduo.downloadManage.provider.DbConstants;
import com.iskyinfor.duoduo.downloadManage.provider.ProviderInterface;
import com.iskyinfor.duoduo.downloadManage.setting.SettingUtils;
import com.iskyinfor.duoduo.downloadManage.utils.DownloadLog;
import com.iskyinfor.duoduo.downloadManage.utils.NetworkUtil;
import com.iskyinfor.duoduo.downloadManage.utils.SdcardUtil;

/**
 * <pre>
 * 下载服务类的几条铁则
 * 1. RESID的生成规则修改为 URL 的模式， 以最大限度保证RESID的唯一性
 *    相同RESID的记录会覆盖
 *    
 * 2. 一条URL可以多次下载，如果生成相同的RESID的话，则该记录会被覆盖
 * 
 * 3. NotifyTag 是通过与_id保持一致
 * 
 * 4. 取消下载不删除下载记录，而是将下载完成状态标志为完成，TaskState保持为取消下载时的默认状态
 * 
 * 
 * 
 * 
 * TODO 1. NOTIFYTAG的问题： 可能造成通知区被覆盖，可能造成通知区重复通知
 * 
 * TODO 2. 设置一个通知类型状态的开关，每个状态对应一个开关，方便调配
 * 
 * TODO 3. broadcast 与 notification 分开来管理
 * 
 * TODO 4. 要添加下载速度的字段 （即时速度 ，平均速度）
 * 
 * TODO 5. 控制任务的状态切换
 * 
 * TODO 6. 网络唤醒的测试
 * 
 * TODO 7. 新增一个静默模式
 * 
 * TODO 8. 需要发送一个下载成功的通知，方便用户做进一步的处理
 * 
 * TODO 9. 解决自动重试的问题 下载Error状态后无法自动重试
 * 
 * </pre>
 * 
 * @author pKF29007
 */

public class DownloadService extends Service {

	/**
	 * 数据观察者
	 */
	private DownloadContentObserver mObserver;
	/**
	 * 通知管理者
	 */
	private DownloadBroadcastManager mBoradcastManager;

	/**
	 * 同步数据库未下载完的任务到容器中的线程
	 */
	private MonitorThread mMonitorThread;

	/**
	 * 是否需要同步的标示
	 */
	private boolean isUpdate = true;

	/**
	 * 是否为网络状态变化唤醒下载服务
	 */
	private boolean isNetWakeUp = false;

	private Context context;

	/**
	 * 管理下载任务的容器
	 */
	DownloadQuene mTaskQuene = new DownloadQuene();

	/**
	 * 封装了数据库操作
	 */
	ProviderInterface mDataFace = null;

	/**
	 * 数据观察者，用户在下载数据库表记录发生变化时唤醒下载服务
	 */
	private class DownloadContentObserver extends ContentObserver {
		public DownloadContentObserver() {
			super(new Handler());
		}

		public void onChange(final boolean selfChange) {
			updateFromProvider();
		}
	}

	@Override
	public void onCreate() {

		// 检查是否启动日志
		DownloadLog.checkIsLog(this);

		super.onCreate();
		context = DownloadService.this;

		// 数据观察者会在数据变化的时候启动线程
		mObserver = new DownloadContentObserver();
		getContentResolver().registerContentObserver(DbConstants.CONTENT_URI,
				true, mObserver);

		// 下载组件下载管理
		mBoradcastManager = new DownloadBroadcastManager(DownloadService.this);
		mBoradcastManager.initDownloadTaskBroadcast();

		// 清理超过500条的记录
		mDataFace = new ProviderInterface(context);
		mDataFace.trimDatabase();

		// 执行同步
		updateFromProvider();

		SettingUtils.updateSettingValue(this);

		Log.i("peng", "download service oncreate ");

	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.i("peng", "download service onStart ");

		try {
			String action = intent.getAction();
			if (Constants.ACTION_NETCHANGE_WAKEUP_SERVICE.equals(action)) {
				Log
						.i(
								"peng",
								"ACTION_NETCHANGE_WAKEUP_SERVICE call ,set (isNetWakeUp = true ,isUpdate = true) ");
				isNetWakeUp = true;
				isUpdate = true;
			}
			updateFromProvider();
		} catch (Exception ex) {
			if (intent == null) {
				Log.i(DownloadLog.LOGTAG, " intent is null ");
			} else {
				String action2 = intent.getAction();
				if (action2 == null) {
					Log.i(DownloadLog.LOGTAG, " intent:action is null ");
				}
			}
			ex.printStackTrace();
		}
	}

	/**
	 * 启动同步数据库未下载完的任务到容器中的线程
	 */
	private void updateFromProvider() {
		synchronized (this) {
			isUpdate = true;
			if (mMonitorThread == null) {
				mMonitorThread = new MonitorThread();
				mMonitorThread.start();
			}
		}
	}

	/**
	 * 下载服务控制线程类
	 */
	private class MonitorThread extends Thread {

		public MonitorThread() {
			super("MonitorThread");
		}

		public void run() {

			Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
			Long wakeUpTime = Long.MAX_VALUE;

			for (;;) {
				Log.i("peng", "MonitorThread runing forever:"
						+ this.toString());
				synchronized (DownloadService.this) {
					if (mMonitorThread != this) {
						throw new IllegalStateException(
								"multiple UpdateThreads in DownloadService");
					}

					if (wakeUpTime != Long.MAX_VALUE) {
						AlarmManager alarms = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
						if (alarms != null) {
							Intent intent = new Intent(
									Constants.ACTION_RESTART_SERVICE);
							intent.setClassName(context.getPackageName(),
									DownloadReceiver.class.getName());
							alarms
									.set(
											AlarmManager.RTC_WAKEUP,
											wakeUpTime,
											PendingIntent
													.getBroadcast(
															context,
															0,
															intent,
															PendingIntent.FLAG_ONE_SHOT));

						}
					}

					if (!isUpdate) {

						// 没有数据变化的时候，更新线程终止；只可能出现在所有下载任务终止的时候
						mMonitorThread = null;
						Log.e("peng", "MonitorThread runing over:"
								+ this.toString());
						return;
					}

					isUpdate = false;
				}

				List<DownloadTask> list = mDataFace.queryTaskByUnFinishState();
				Log.i("peng", "list:" + list.size() + ":");
				if (list.size() != 0) {
					int idleTaskCount = getIdleTaskCount(list);
					Log.i("pengjun", "idleTaskCount:" + idleTaskCount);

					for (DownloadTask dbItem : list) {
						Log.i("peng", "dblist:" + dbItem.resourceId + ":");

						DownloadTask queneItem = mTaskQuene
								.getDownloadTaskByResid(dbItem.resourceId);

						// 新增下载任务
						if (queneItem == null) {
							addDownloadTask(dbItem);
							// dbItem.logSelf("queneItem looker",
							// "addDownloadTask ");
							isUpdate = true;
							break;
						} else {
							int nowState = dbItem.taskState;
							switch (nowState) {
							// 下载任务完成的处理：
							case DownloadTask.State.SUCCESS:
								// FIXME 发送下载完成的通知和广播
								removeDownloadTask(dbItem);
								// dbItem.logSelf("queneItem looker",
								// "DownloadTask.State.FINISH ");
								break;

							case DownloadTask.State.FAIL:
								// FIXME 发送下载失败的通知和广播
//								removeDownloadTask(dbItem);
								SdcardUtil.deleteFile(dbItem.file);
								// dbItem.logSelf("queneItem looker",
								// "DownloadTask.State.FAIL ");
								break;

							case DownloadTask.State.PAUSE:
								pauseDownloadTask(dbItem);
								// dbItem.logSelf("queneItem looker",
								// "DownloadTask.State.PAUSE ");
								break;

							case DownloadTask.State.CANCEL:
								cancelDownloadTask(dbItem);
								// dbItem.logSelf("queneItem looker",
								// "DownloadTask.State.CANCEL ");
								break;

							case DownloadTask.State.ERROR:
								wakeUpTime = errorDownloadTask(dbItem,
										wakeUpTime, idleTaskCount);
								// dbItem.logSelf("queneItem looker",
								// "DownloadTask.State.ERROR ");
								break;

							case DownloadTask.State.RUNNING:
								runningDownloadTask(dbItem, idleTaskCount);
								// dbItem.logSelf("queneItem looker",
								// "DownloadTask.State.RUNNING ");
								break;

							case DownloadTask.State.WAIT:
								waitDownloadTask(dbItem, idleTaskCount);
								// dbItem.logSelf("queneItem looker",
								// "DownloadTask.State.WAIT ");
								Log.i("peng", "DownloadTask.State.WAIT===:");
								break;

							default:
								break;
							}
						}
					}
					isNetWakeUp = false;
				}

				 else if (!isUpdate) {

					Intent netWakeIntent = new Intent(context,
							DownloadService.class);

					context.stopService(netWakeIntent);

					Log.i("pengjun", "stopService");
				}
			}
		}

		/**
		 * 等待状态的处理： 在条件允许的情况下，启动等待的任务 条件允许的情况是： 1 . 网络状况OK ,后期会添加 针对任务设定的网络状态判断
		 * 2 .
		 * 
		 * @param dbItem
		 * @param idleTaskCount
		 * @param queneItem
		 */
		private void waitDownloadTask(DownloadTask dbItem, int idleTaskCount) {
			DownloadTask queneItem = mTaskQuene
					.getDownloadTaskByResid(dbItem.resourceId);

			mBoradcastManager.sendDownloadBroadcast(context,
					Constants.ACTION_DOWNLOAD_BROADCAST_STATE_WAIT, dbItem);

			int currentNetState = DownloadUtil.getCurrentNetState(context);
			if (NetworkUtil.hasActiveNetwork(context)
					&& (dbItem.netState & currentNetState) != 0) {
				Log.i("peng", "doStartTask" + "");
				doStartTask(queneItem, idleTaskCount);
			
			} else {
				dbItem.errorCode = DownloadTask.ErrorCode.NetWorkStateError;

				mBoradcastManager
						.sendDownloadBroadcast(
								context,
								Constants.ACTION_DOWNLOAD_BROADCAST_STATE_ERROR,
								dbItem);

			}

			queneItem.isSync = true;
		}

		/**
		 * @param wakeUpTime
		 * @param idleTaskCount
		 * @param dbItem
		 * @param queneItem
		 * @return
		 */
		private Long errorDownloadTask(DownloadTask dbItem, Long wakeUpTime,
				int idleTaskCount) {

			Long wakeUp = Long.MAX_VALUE;
			DownloadTask queneItem = mTaskQuene
					.getDownloadTaskByResid(dbItem.resourceId);

			queneItem.taskState = DownloadTask.State.ERROR;

			// if (queneItem.hasActiveThread) {
			// queneItem.stopTask(DownloadTask.State.PAUSE);
			// try {
			// Thread.sleep(100L);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			// }

			if ((dbItem.errorCode == DownloadTask.ErrorCode.DownloadProcessError || dbItem.errorCode == DownloadTask.ErrorCode.NetWorkStateError)
					&& dbItem.failCount <= SettingUtils.MAX_RETRY_COUNT) {

				long now = System.currentTimeMillis();

				if ((isNetWakeUp || (dbItem.retryTime <= now))) {
					int currentNetState = DownloadUtil
							.getCurrentNetState(context);

					if (NetworkUtil.hasActiveNetwork(context)
							&& (dbItem.netState & currentNetState) != 0) {

						if (doStartTask(queneItem, idleTaskCount)) {
							Log.i("pengjun3", "start thread ok");
						} else {
							Log.i("pengjun3", "start thread fail");
						}
					}
				} else {
					// 未达到启动条件
					wakeUp = dbItem.retryTime;
				}
			} else {
				dbItem.taskState = DownloadTask.State.FAIL;
				failDownloadTask(dbItem);
			}

			if (wakeUp < wakeUpTime) {
				wakeUpTime = wakeUp;
			}
			queneItem.isSync = true;
			isUpdate = true;
			return wakeUpTime;
		}
	}

	/**
	 * 下载任务正在运行的操作
	 * 
	 * @param item
	 * @param hasIdleTask
	 */
	private void runningDownloadTask(DownloadTask item, int idleTaskCount) {
		DownloadTask queneItem = mTaskQuene
				.getDownloadTaskByResid(item.resourceId);

		if (!queneItem.hasActiveThread) {
			int currentNetState = DownloadUtil.getCurrentNetState(context);
			if (NetworkUtil.hasActiveNetwork(DownloadService.this)
					&& (item.netState & currentNetState) != 0) {
				if (!doStartTask(queneItem, idleTaskCount)) {
					setWaitState(queneItem);
				}
			}
		}
		queneItem.isSync = true;
	}

	/**
	 * 下载任务暂停的操作
	 * 
	 * @param dbitem
	 */
	private void pauseDownloadTask(DownloadTask dbitem) {

		DownloadTask queneItem = mTaskQuene
				.getDownloadTaskByResid(dbitem.resourceId);

		if (queneItem.taskState != DownloadTask.State.PAUSE) {
			queneItem.taskState = DownloadTask.State.PAUSE;
			if (queneItem.hasActiveThread) {
				queneItem.stopTask(DownloadTask.State.PAUSE);
				try {
					Thread.sleep(100L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {

				queneItem.refreshSelfProgress();
				mDataFace.updateDownloadTask(queneItem);
			}
		}

		// TODO 只是发送广播，如何发送通知
		mBoradcastManager.sendDownloadBroadcast(this,
				Constants.ACTION_DOWNLOAD_BROADCAST_STATE_PAUSE, dbitem);

		queneItem.isSync = true;
	}

	/**
	 * 下载任务取消的操作
	 * 
	 * @param dbItem
	 */
	private void cancelDownloadTask(DownloadTask dbItem) {
		DownloadTask queneItem = mTaskQuene
				.getDownloadTaskByResid(dbItem.resourceId);

		if (queneItem.taskState != DownloadTask.State.CANCEL) {
			queneItem.taskState = DownloadTask.State.CANCEL;

			if (queneItem.hasActiveThread) {
				queneItem.stopTask(DownloadTask.State.CANCEL);
				try {
					Thread.sleep(100L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				SdcardUtil.deleteFile(dbItem.file);
				queneItem.refreshSelfProgress();
				mDataFace.updateDownloadTask(queneItem);
			}
		}

		removeDownloadTask(dbItem);

		mBoradcastManager.sendDownloadBroadcast(this,
				Constants.ACTION_DOWNLOAD_BROADCAST_STATE_CANCEL, dbItem);

		// 清理临时文件
		queneItem.isSync = true;
	}

	/**
	 * 移除下载任务的操作,并标志下载任务为完成专题
	 * 
	 * @param item
	 */
	private void removeDownloadTask(DownloadTask dbItem) {
		mDataFace.updateFinishStateByResid(dbItem.resourceId,
				DownloadTask.FinishState.TASK_FINISH_STATE);
		mTaskQuene.removeDownloadTaskByResid(dbItem.resourceId);

	}

	/**
	 * 下载任务失败的操作
	 * 
	 * @param dbitem
	 */
	private void failDownloadTask(DownloadTask dbitem) {
		mDataFace.updateTaskStateByResId(dbitem.resourceId, dbitem.taskState);

		mBoradcastManager.sendDownloadBroadcast(context,
				Constants.ACTION_DOWNLOAD_BROADCAST_STATE_FAIL, dbitem);

		SdcardUtil.deleteFile(dbitem.file);
	}

	/**
	 * 添加下载任务的操作
	 * 
	 * @param item
	 */
	private void addDownloadTask(DownloadTask item) {
		mTaskQuene.add(item);
		item.isSync = true;
	}

	/**
	 * 设置等待状态的相关设置
	 * 
	 * @param item
	 */
	private void setWaitState(DownloadTask item) {
		item.taskState = DownloadTask.State.WAIT;
		item.errorCode = DownloadTask.ErrorCode.EmptyError;

		mDataFace.updateTaskStateByResId(item.resourceId, item.taskState);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// 释放Observer
		getContentResolver().unregisterContentObserver(mObserver);

		// 清空下载队列
		mTaskQuene.clear();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// XXX bind service not allow
		return null;
	}

	/**
	 * 判断可启动下载任务名额的数量
	 * 
	 * @return
	 */
	private int getIdleTaskCount(List<DownloadTask> list) {
		int count = getRunningCount(list);
		return SettingUtils.MAX_RUNING_ITEM_COUNT - count;
	}

	/**
	 * 获取正在运行的下载任务数
	 * 
	 * @return
	 */
	private int getRunningCount(List<DownloadTask> list) {
		int runningCount = 0;
		DownloadTask item = null;

		for (int i = 0; i < list.size(); i++) {
			item = list.get(i);
			if (item.taskState == DownloadTask.State.RUNNING) {
				runningCount = runningCount + 1;
			}
		}
		return runningCount;
	}

	/**
	 * @param idleTaskCount
	 * @param queneItem
	 */
	private boolean doStartTask(DownloadTask queneItem, int idleTaskCount) {
		boolean result = false;

		if (!queneItem.hasActiveThread && idleTaskCount > 0) {
			idleTaskCount--;
			queneItem.startTask(context);
			try {
				Thread.sleep(100L);
			} catch (InterruptedException e) {
				e.printStackTrace();
				result = false;
			}
			result = true;
		} else {
			result = false;
		}
		return result;
	}

}
