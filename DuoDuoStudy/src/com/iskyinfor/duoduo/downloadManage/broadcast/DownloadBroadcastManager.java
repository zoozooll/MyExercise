package com.iskyinfor.duoduo.downloadManage.broadcast;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.iskyinfor.duoduo.downloadManage.Constants;
import com.iskyinfor.duoduo.downloadManage.DownloadTask;
import com.iskyinfor.duoduo.downloadManage.notification.DownloadNotificationManager;
import com.iskyinfor.duoduo.downloadManage.provider.ProviderInterface;

public class DownloadBroadcastManager {

	private Context context;
	private DownloadNotificationManager notificationManager;

	public DownloadBroadcastManager(Context ctx) {
		context = ctx;
		notificationManager = new DownloadNotificationManager(context);
	}

	public void sendDownloadBroadcast(Context context, String action,
			DownloadTask item) {
		if ((item.silentMode & DownloadTask.SilentMode.BROADCAST) != 0) {
			Intent intent = new Intent();
			intent.setAction(action);
			Bundle bundle = new Bundle();
			bundle.putSerializable(Constants.TAG_DOWNLOADBUNDLE,
					getDownloadBundle(item));
			intent.putExtra(Constants.INTENT_BROADCAST_DOWNLOADTASK_FLAG,
					bundle);
			context.sendBroadcast(intent);
		}
	}

	/**
	 * ��ʼ��֪ͨ
	 */
	public void initDownloadTaskBroadcast() {
		ProviderInterface dataFace = new ProviderInterface(context);
		List<DownloadTask> list = dataFace.queryTaskByUnFinishState();
		DownloadTask item = null;
		for (int i = 0; i < list.size(); i++) {
			item = list.get(i);

			switch (item.taskState) {
			case DownloadTask.State.SUCCESS:

				sendDownloadBroadcast(context,
						Constants.ACTION_DOWNLOAD_BROADCAST_STATE_FINISH, item);

//				notificationManager.sendFinishNotification(item);
				break;
			case DownloadTask.State.PAUSE:
				sendDownloadBroadcast(context,
						Constants.ACTION_DOWNLOAD_BROADCAST_STATE_PAUSE, item);

//				notificationManager.sendPauseNotification(item);
				break;
			case DownloadTask.State.ERROR:
				sendDownloadBroadcast(context,
						Constants.ACTION_DOWNLOAD_BROADCAST_STATE_ERROR, item);

//				notificationManager.sendErrorNotification(item);
				break;

			case DownloadTask.State.RUNNING:

				sendDownloadBroadcast(context,
						Constants.ACTION_DOWNLOAD_BROADCAST_STATE_RUNNING, item);
//				notificationManager.sendRunningNotification(item);
				break;

			case DownloadTask.State.WAIT:

				sendDownloadBroadcast(context,
						Constants.ACTION_DOWNLOAD_BROADCAST_STATE_WAIT, item);
//				notificationManager.sendWaitStartNotification(item);
				break;

			default:
				break;
			}

		}
	}

	public DownloadBundle getDownloadBundle(DownloadTask item) {
		DownloadBundle downloadItem = new DownloadBundle();

		downloadItem.setProgress(item.progress);
		downloadItem.setTaskState(item.taskState);
		downloadItem.setResId(item.resourceId);
		downloadItem.setCurrentSize(item.currentSize);
		downloadItem.setTotalSize(item.totalSize);

		downloadItem.setNotifyTag(item.notifyTag);
		downloadItem.setDownType(item.downType);

		downloadItem.setFilePath(item.filePath);
		downloadItem.setFileType(item.fileType);
		downloadItem.setPackageName(item.packageName);
		downloadItem.setErrorCode(item.errorCode);
		downloadItem.setName(item.name);
		downloadItem.setExtendName(item.extendName);

		return downloadItem;

	}
}
